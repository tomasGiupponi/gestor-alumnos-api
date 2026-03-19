package com.tomas.gestordealumnos.dto;

import com.tomas.gestordealumnos.enumeration.EnrollmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDto {

    @NotNull(message = "Student UUID is required")
    private UUID studentUuid;

    @NotBlank(message = "Course code is required")
    private String courseCode;

    private Double finalGrade;
    private LocalDate enrollmentDate;
    private EnrollmentStatus status;
}
