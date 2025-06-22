package ssdd.practicaWeb.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssdd.practicaWeb.dto.TrainingDTO;
import ssdd.practicaWeb.model.Food;
import ssdd.practicaWeb.dto.FoodDTO;
import ssdd.practicaWeb.service.FoodService;
import ssdd.practicaWeb.service.TrainingService;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodRESTController {

    @Autowired
    private FoodService foodService;


    @Operation(summary = "Get all foods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all foods", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Foods not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/all")
    public Collection<FoodDTO> getAllFoods(){
        return foodService.getAllDtoFoods();
    }

    @Operation(summary = "Get a food by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Food not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{foodId}")
    public FoodDTO getTrainingByIdd(@PathVariable long foodId) {
        return foodService.getDtoFood(foodId);
    }

    @Operation(summary = "Create a food")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Food created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<FoodDTO> createTraining(@RequestBody FoodDTO foodDTO)
           {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

               Food food = foodService.toDomain(foodDTO);
        if(authentication!=null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))){
            food = foodService.createFood(food);

        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para editar este alimento");
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(food.getId()).toUri();

        return ResponseEntity.created(location).body(foodService.toDTO(food));

    }

    @Operation(summary = "Replace a food")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food replaced", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Food not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{foodId}")
    public FoodDTO replacePost(@PathVariable long foodId, @RequestBody FoodDTO foodDTO)
            {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Food food = foodService.toDomain(foodDTO);
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            return foodService.toDTO(foodService.updateFood(foodId,food));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para editar este entrenamiento");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a food")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Food not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{foodId}")
    public FoodDTO deleteFood(@PathVariable long foodId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication!=null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return foodService.toDTO(foodService.deleteFood(foodId));
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para eliminar este entrenamiento");
        }
    }
}
