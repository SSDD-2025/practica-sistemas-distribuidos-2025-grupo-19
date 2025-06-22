package ssdd.practicaWeb.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.model.Nutrition;
import ssdd.practicaWeb.dto.NutritionDTO;
import ssdd.practicaWeb.service.NutritionService;
import ssdd.practicaWeb.service.UserService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/nutritions")
public class NutritionRESTController {

    @Autowired
    private NutritionService nutritionService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all nutritions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all nutritions", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Nutrition.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parametes", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nutritions not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @GetMapping("/all")
    public Collection <NutritionDTO> getNutritions () {

        return nutritionService.getAllNutritionsDTO();
    }

    @Operation(summary = "Get a nutrition by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutrition found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NutritionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nutrition not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @GetMapping("/{id}")
    public NutritionDTO getNutrition(@PathVariable Long id) {

        return nutritionService.getNutritionDTO(id);
    }

    @Operation(summary = "Get all nutritions of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all nutritions", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NutritionDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nutritions not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myNutritions")
    public Collection<NutritionDTO> getUserNutritions() {
        return nutritionService.getAllDtoUserNutritions();
    }


    @Operation(summary = "Create a new nutrition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Nutrition created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NutritionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict - Nutrition already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<NutritionDTO> createNutrition(@RequestBody NutritionDTO nutritionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authentication != null ? nutritionService.getAuthenticationUser() : null;

        Nutrition nutrition = nutritionService.toDomain(nutritionDTO);
        if(authentication!=null && !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))){
            nutrition = nutritionService.createNutrition(nutrition, nutritionService.getAuthenticationUser());

            if(user != null) {
                nutrition.setUser(user);
            }
        }else{
            nutrition = nutritionService.createNutrition(nutrition, null);
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nutrition.getId()).toUri();

        return ResponseEntity.created(location).body(nutritionService.toDTO(nutrition));
    }


    @Operation(summary = "Edit a nutrition by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutrition updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NutritionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to edit", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nutrition not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public NutritionDTO updateNutrition(@PathVariable Long id, @RequestBody NutritionDTO nutritionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (nutritionService.isOwner(id, authentication) || authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            return nutritionService.toDTO(nutritionService.updateNutrition(id, nutritionService.toDomain(nutritionDTO)));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tienes permisos para editar esta dieta");
        }
    }

    @Operation(summary = "Delete a nutrition by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nutrition deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = NutritionDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to delete", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nutrition not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @DeleteMapping("/{id}")
    public NutritionDTO deleteNutrition(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication!=null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return nutritionService.toDTO(nutritionService.deleteNutrition(id));
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para eliminar esta dieta");
        }
    }

    @Operation(summary = "Subscribe a user to a nutrition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User subscribed to nutrition successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nutrition or User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @PostMapping("/subscribed/{nutritionId}")
    public ResponseEntity<String> subscribeNutrition(@PathVariable Long nutritionId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            boolean alreadySubscribed = nutritionService.subscribeNutritionDTO(nutritionId, email);

            if (alreadySubscribed) {
                return ResponseEntity.ok("The user was already subscribed to this nutrition.");
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body("User subscribed from nutrition successfully");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @Operation(summary = "Unsubscribe a user from a nutrition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User unsubscribed from nutrition successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nutrition or User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @PostMapping("/unsubscribed/{nutritionId}")
    public ResponseEntity<String> unsubscribeNutrition(@PathVariable Long nutritionId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            boolean unsubscribed = nutritionService.unsubscribeNutritionDTO(nutritionId, email);

            if (unsubscribed) {
                return ResponseEntity.ok("User unsubscribed from nutrition successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User is not subscribed to this nutrition. Subscribe first before unsubscribing.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}