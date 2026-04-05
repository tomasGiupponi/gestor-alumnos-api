package com.tomas.gestordealumnos.service.impl;

import com.tomas.gestordealumnos.dto.EnrollmentDto;
import com.tomas.gestordealumnos.dto.EnrollmentUpdateDto;
import com.tomas.gestordealumnos.enumeration.EnrollmentStatus;
import com.tomas.gestordealumnos.exception.CourseNotFoundException;
import com.tomas.gestordealumnos.exception.EnrollmentNotFoundException;
import com.tomas.gestordealumnos.exception.StudentNotFoundException;
import com.tomas.gestordealumnos.model.Course;
import com.tomas.gestordealumnos.model.Enrollment;
import com.tomas.gestordealumnos.model.Student;
import com.tomas.gestordealumnos.repository.CourseRepository;
import com.tomas.gestordealumnos.repository.EnrollmentRepository;
import com.tomas.gestordealumnos.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentServiceImpl;


    @Test
    void createEnrollmentShouldSaveAnEnrollmentAndReturnAnEnrollmentDto () {

        Student student = buildDefaultStudent();
        Course course = buildDefaultCourse();

        UUID studentUuid = student.getUuid();
        String courseCode = course.getCode();

        Enrollment enrollment = buildDefaultEnrollment(student, course);
        EnrollmentDto enrollmentDto = buildDefaultEnrollmentDto(studentUuid, courseCode);


        when(studentRepository.findByUuid(studentUuid)).thenReturn(Optional.of(student));
        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        EnrollmentDto result = enrollmentServiceImpl.createEnrollment(enrollmentDto);

        assertNotNull(result);
        assertEquals(enrollmentDto, result, "result must be equal to enrollmentDto");
        verify(studentRepository, times(1)).findByUuid(studentUuid);
        verify(courseRepository, times(1)).findByCode(courseCode);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void createEnrollmentShouldThrowStudentNotFoundExceptionWhenStudentUuidDoesNotExist () {

        Course course = buildDefaultCourse();

        String  courseCode = course.getCode();
        UUID unexistingStudentUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        EnrollmentDto enrollmentDto = buildDefaultEnrollmentDto(unexistingStudentUuid, courseCode);

        when(studentRepository.findByUuid(unexistingStudentUuid)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> enrollmentServiceImpl.createEnrollment(enrollmentDto));

        verify(studentRepository, times(1)).findByUuid(unexistingStudentUuid);
        verify(courseRepository, never()).findByCode(courseCode);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void createEnrollmentShouldThrowCourseNotFoundExceptionWhenCourseCodeDoesNotExist () {

        Student student = buildDefaultStudent();

        String unexistingCourseCode = "01-MAT1";
        UUID studentUuid = student.getUuid();

        EnrollmentDto enrollmentDto = buildDefaultEnrollmentDto(studentUuid, unexistingCourseCode);

        when(studentRepository.findByUuid(studentUuid)).thenReturn(Optional.of(student));
        when(courseRepository.findByCode(unexistingCourseCode)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> enrollmentServiceImpl.createEnrollment(enrollmentDto));

        verify(studentRepository, times(1)).findByUuid(studentUuid);
        verify(courseRepository, times(1)).findByCode(unexistingCourseCode);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void createEnrollmentShouldThrowIllegalArgumentExceptionWhenStudentAndCourseAreAlreadyEnrolled () {

        Student student = buildDefaultStudent();
        Course course = buildDefaultCourse();

        UUID studentUuid = student.getUuid();
        String courseCode = course.getCode();

        EnrollmentDto enrollmentDto = buildDefaultEnrollmentDto(studentUuid, courseCode);

        when(enrollmentRepository.existsByStudentUuidAndCourseCode(studentUuid, courseCode)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> enrollmentServiceImpl.createEnrollment(enrollmentDto));
        verify(studentRepository, never()).findByUuid(studentUuid);
        verify(courseRepository, never()).findByCode(courseCode);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void getEnrollmentsByStudentShouldReturnAListOfEnrollmentDto () {

        Student student = buildDefaultStudent();
        Course course = buildDefaultCourse();

        UUID studentUuid = student.getUuid();

        Enrollment enrollment = buildDefaultEnrollment(student, course);

        when(studentRepository.existsByUuid(studentUuid)).thenReturn(true);
        when(enrollmentRepository.findByStudentUuid(studentUuid)).thenReturn(List.of(enrollment));

        List<EnrollmentDto> result = enrollmentServiceImpl.getEnrollmentsByStudent(studentUuid);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(studentRepository, times(1)).existsByUuid(studentUuid);
        verify(enrollmentRepository, times(1)).findByStudentUuid(studentUuid);
    }

    @Test
    void getEnrollmentsByStudentShouldThrowStudentNotFoundExceptionWhenStudentUuidDoesNotExist () {

        UUID unexistingStudentUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        when(studentRepository.existsByUuid(unexistingStudentUuid)).thenReturn(false);

        assertThrows(StudentNotFoundException.class, () -> enrollmentServiceImpl.getEnrollmentsByStudent(unexistingStudentUuid));
        verify(studentRepository, times(1)).existsByUuid(unexistingStudentUuid);
        verify(enrollmentRepository, never()).findByStudentUuid(unexistingStudentUuid);
    }

    @Test
    void getEnrollmentsByCourseShouldReturnAListOfEnrollmentDto () {

        Student student = buildDefaultStudent();
        Course course = buildDefaultCourse();

        String courseCode = course.getCode();

        Enrollment enrollment = buildDefaultEnrollment(student, course);

        when(courseRepository.existsByCode(courseCode)).thenReturn(true);
        when(enrollmentRepository.findByCourseCode(courseCode)).thenReturn(List.of(enrollment));

        List<EnrollmentDto> result = enrollmentServiceImpl.getEnrollmentsByCourse(courseCode);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(courseRepository, times(1)).existsByCode(courseCode);
        verify(enrollmentRepository, times(1)).findByCourseCode(courseCode);
    }

    @Test
    void getEnrollmentsByCourseShouldThrowCourseNotFoundExceptionWhenCourseCodeDoesNotExist () {

        String courseCode = "01-MAT1";

        when(courseRepository.existsByCode(courseCode)).thenReturn(false);

        assertThrows(CourseNotFoundException.class, () -> enrollmentServiceImpl.getEnrollmentsByCourse(courseCode));
        verify(courseRepository, times(1)).existsByCode(courseCode);
        verify(enrollmentRepository, never()).findByCourseCode(courseCode);
    }

    @Test
    void updateEnrollmentShouldReturnAnUpdatedEnrollmentDto() {

        Student student = buildDefaultStudent();
        Course course = buildDefaultCourse();
        Enrollment enrollment = buildDefaultEnrollment(student, course);
        EnrollmentUpdateDto updateRequestEnrollmentDto = buildDefaultEnrollmentUpdateDto();
        enrollment.setId(1L);
        Long enrollmentId = enrollment.getId();

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        EnrollmentDto updatedEnrollmentDto = enrollmentServiceImpl.updateEnrollment(enrollmentId, updateRequestEnrollmentDto);

        assertNotNull(updatedEnrollmentDto);
        assertEquals(updateRequestEnrollmentDto.getEnrollmentStatus(), updatedEnrollmentDto.getStatus(), "status must be equal");
        assertEquals(updateRequestEnrollmentDto.getFinalGrade(), updatedEnrollmentDto.getFinalGrade(), "finalGrade must be equal");
        verify(enrollmentRepository, times(1)).findById(enrollmentId);
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void updateEnrollmentShouldThrowEnrollmentNotFoundExceptionWhenIdDoesNotExist () {

        EnrollmentUpdateDto enrollmentUpdateDto = buildDefaultEnrollmentUpdateDto();
        Long unexistingId = 1L;

        assertThrows(EnrollmentNotFoundException.class, () -> enrollmentServiceImpl.updateEnrollment(unexistingId, enrollmentUpdateDto));
        verify(enrollmentRepository, times(1)).findById(unexistingId);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    private Student buildDefaultStudent() {

        return Student.builder()
                .uuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .firstName("Facundo")
                .lastName("Gomez")
                .dni("42186324")
                .build();
    }

    private Course buildDefaultCourse() {

        return Course.builder()
                .code("01-MAT1")
                .name("Matemática 1")
                .build();
    }

    private Enrollment buildDefaultEnrollment(Student student, Course course) {

        return new Enrollment(student, course, EnrollmentStatus.PENDING);
    }

    private EnrollmentDto buildDefaultEnrollmentDto(UUID studentUuid, String courseCode) {

        return EnrollmentDto.builder()
                .studentUuid(studentUuid)
                .courseCode(courseCode)
                .enrollmentDate(LocalDate.now())
                .status(EnrollmentStatus.PENDING)
                .build();
    }

    private EnrollmentUpdateDto buildDefaultEnrollmentUpdateDto() {

        return EnrollmentUpdateDto.builder()
                .enrollmentStatus(EnrollmentStatus.APPROVED)
                .finalGrade(8.3)
                .build();
    }
}
