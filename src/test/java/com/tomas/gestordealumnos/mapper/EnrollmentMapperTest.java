package com.tomas.gestordealumnos.mapper;

import com.tomas.gestordealumnos.dto.EnrollmentDto;
import com.tomas.gestordealumnos.enumeration.EnrollmentStatus;
import com.tomas.gestordealumnos.model.Course;
import com.tomas.gestordealumnos.model.Enrollment;
import com.tomas.gestordealumnos.model.Student;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentMapperTest {

    @Test
    void shouldMapEnrollmentToEnrollmentDto() {

        // --- ARRANGE ---
        Student student = Student.builder()
                .uuid(UUID.randomUUID())
                .firstName("Pepe")
                .lastName("Perez")
                .build();

        Course course = Course.builder()
                .code("01-LYT1")
                .name("Lengua y Literatura 1")
                .build();

        Enrollment enrollment = new Enrollment(student, course, EnrollmentStatus.PENDING);

        // --- ACT ---
        EnrollmentDto enrollmentDto = EnrollmentMapper.toDto(enrollment);

        // --- ASSERT ---
        assertEquals(student.getUuid(), enrollmentDto.getStudentUuid(), "student UUID should match");
        assertEquals(course.getCode(), enrollmentDto.getCourseCode(), "course code should match");
        assertEquals(EnrollmentStatus.PENDING, enrollmentDto.getStatus(), "status should be PENDING");
    }

    @Test
    void shouldReturnNullWhenEnrollmentIsNull() {
        // --- ARRANGE ---
        Enrollment enrollmentNull = null;

        // --- ACT ---
        EnrollmentDto enrollmentDto = EnrollmentMapper.toDto(enrollmentNull);

        // --- ASSERT ---
        assertNull(enrollmentDto, "should return null when enrollment is null");
    }
}
