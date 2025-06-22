package ssdd.practicaWeb.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO (
        Long id,
        String name,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        List<String> roles,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required")
        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        String email,

        @NotBlank(message = "Password is required")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Schema(description = "Password for authentication (write-only field)", accessMode = Schema.AccessMode.WRITE_ONLY)
        String password,

        double weight,
        double goalWeight,
        int height,
        String gender,
        int age,
        String morphology,
        String caloricPhase){

}
