package com.tomas.gestordealumnos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 25, message = "First name must be between 3 and 25 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 25, message = "First name must be between 3 and 25 characters")
    private String lastName;

    @NotBlank(message = "DNI is required")
    @Pattern(regexp = "^[0-9]{8}$", message = "DNI must contain only numbers and be 8 digits long")
    private String dni;
}
