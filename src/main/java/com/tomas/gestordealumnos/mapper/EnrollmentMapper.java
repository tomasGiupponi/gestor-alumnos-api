package com.tomas.gestordealumnos.mapper;

import com.tomas.gestordealumnos.dto.EnrollmentDto;
import com.tomas.gestordealumnos.model.Enrollment;

public class EnrollmentMapper {

    //From Enrollment to EnrollmentDto
    public static EnrollmentDto toDto(Enrollment enrollment) {

        if (enrollment == null) return null;

        return EnrollmentDto.builder()
                .studentUuid(enrollment.getStudent().getUuid())
                .courseCode(enrollment.getCourse().getCode())
                .finalGrade(enrollment.getFinalGrade())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .build();
    }
}
