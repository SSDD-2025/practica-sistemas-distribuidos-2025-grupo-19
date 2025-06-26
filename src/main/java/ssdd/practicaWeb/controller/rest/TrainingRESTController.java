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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ssdd.practicaWeb.dto.TrainingDTO;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.model.Training;
import ssdd.practicaWeb.service.TrainingService;
import ssdd.practicaWeb.service.UserService;

import java.net.URI;
import java.util.*;


@RestController
@RequestMapping("/api/trainings")
public class TrainingRESTController {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all trainings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all trainings", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainings not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/all")
    public Collection<TrainingDTO> getAllTrainings(){
            return trainingService.getAllDtoTrainings();
    }

    @Operation(summary = "Get a training by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{trainingId}")
    public TrainingDTO getTrainingByIdd(@PathVariable long trainingId) {
        return trainingService.getDtoTraining(trainingId);
    }

    @Operation(summary = "Get paginated trainings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/paginated")
    public ResponseEntity<List<TrainingDTO>> getPaginatedTrainings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int limit) {

        List<TrainingDTO> trainingDTOs = trainingService.getPaginatedTrainingsDTO(page, limit);
        return ResponseEntity.ok(trainingDTOs);
    }

    @Operation(summary = "Get all trainings of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all trainings", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainings not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myTrainings")
    public Collection<TrainingDTO> getUserTrainings() {
        return trainingService.getAllDtoUserTrainings();
    }


    @Operation(summary = "Create a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<TrainingDTO> createTraining(@RequestBody TrainingDTO trainingDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = authentication != null ? trainingService.getAuthenticationUser() : null;

        Training training = trainingService.toDomain(trainingDTO);
        if(authentication!=null && !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))){
            training = trainingService.createTraining(training, trainingService.getAuthenticationUser());

            if(user != null) {
                training.setUser(user);
            }
        }else{
            training = trainingService.createTraining(training, null);
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(training.getId()).toUri();

        return ResponseEntity.created(location).body(trainingService.toDTO(training));

    }

    @Operation(summary = "Replace a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training replaced", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public TrainingDTO updateTraining(@PathVariable Long id, @RequestBody TrainingDTO trainingDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (trainingService.isOwner(id, authentication) || authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            return trainingService.replaceTraining(id, trainingDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para editar este entrenamiento");
        }
    }

    @Operation(summary = "Delete a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TrainingDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public TrainingDTO deleteTraining(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication!=null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return trainingService.toDTO(trainingService.deleteTraining(id));
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para eliminar este entrenamiento");
        }
    }

    @PostMapping("/subscribed/{id}")
    public ResponseEntity<String> subscribeTraining(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            boolean alreadySubscribed = trainingService.subscribeTrainingDTO(id, email);

            if (alreadySubscribed) {
                return ResponseEntity.ok("The user was already subscribed to this training.");
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body("User subscribed from training successfully");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @Operation(summary = "Unsubscribe a user from a training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User unsubscribed from training successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Training or User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @PostMapping("/unsubscribed/{id}")
    public ResponseEntity<String> unsubscribeTraining(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            boolean unsubscribed = trainingService.unsubscribeTrainingDTO(id, email);

            if (unsubscribed) {
                return ResponseEntity.ok("User unsubscribed from training successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User is not subscribed to this training. Subscribe first before unsubscribing.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}

