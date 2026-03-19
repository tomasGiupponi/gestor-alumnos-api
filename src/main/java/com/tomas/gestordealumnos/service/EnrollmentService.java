package com.tomas.gestordealumnos.service;

import com.tomas.gestordealumnos.dto.EnrollmentDto;
import com.tomas.gestordealumnos.dto.EnrollmentUpdateDto;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {

    EnrollmentDto createEnrollment(EnrollmentDto enrollmentDto);
    List<EnrollmentDto> getEnrollmentsByStudent(UUID studentUuid);
    List<EnrollmentDto> getEnrollmentsByCourse(String courseCode);
    EnrollmentDto updateEnrollment(Long id, EnrollmentUpdateDto enrollmentUpdateDto);
}
