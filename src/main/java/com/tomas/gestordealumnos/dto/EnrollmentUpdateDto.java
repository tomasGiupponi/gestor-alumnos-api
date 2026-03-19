package com.tomas.gestordealumnos.dto;

import com.tomas.gestordealumnos.enumeration.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentUpdateDto {

    @NotNull(message = "Status enrollment is required")
    private EnrollmentStatus enrollmentStatus;

    private Double finalGrade;
}
