package com.tomas.gestordealumnos.repository;

import com.tomas.gestordealumnos.enumeration.EnrollmentStatus;
import com.tomas.gestordealumnos.model.Course;
import com.tomas.gestordealumnos.model.Enrollment;
import com.tomas.gestordealumnos.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findByStudentUuidShouldReturnAListOfEnrollmentsWhenStudentUuidExists() {

        Enrollment enrollment = buildDefaultEnrollment();
        UUID studentUuid = enrollment.getStudent().getUuid();

        enrollmentRepository.save(enrollment);

        List<Enrollment> enrollments = enrollmentRepository.findByStudentUuid(studentUuid);

        assertEquals(1, enrollments.size());
        assertEquals(enrollment, enrollments.get(0));
    }

    @Test
    void findByStudentUuidShouldReturnAnEmptyListWhenStudentUuidDoesNotExist() {

        List<Enrollment> enrollments = enrollmentRepository.findByStudentUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        assertEquals(0, enrollments.size());
    }

    @Test
    void findByCourseCodeShouldReturnAListOfEnrollmentsWhenCourseCodeExists() {

        Enrollment enrollment = buildDefaultEnrollment();
        String courseCode = enrollment.getCourse().getCode();

        enrollmentRepository.save(enrollment);

        List<Enrollment> enrollments = enrollmentRepository.findByCourseCode(courseCode);

        assertEquals(1, enrollments.size());
        assertEquals(enrollment, enrollments.get(0));
    }

    @Test
    void findByCourseCodeShouldReturnAnEmptyListWhenCourseCodeDoesNotExist() {

        List<Enrollment> enrollments = enrollmentRepository.findByCourseCode("01-BIO1");

        assertEquals(0, enrollments.size());
    }

    @Test
    void existsByStudentUuidAndCourseCodeShouldReturnTrueWhenStudentUuidAndCourseCodeExists() {

        Enrollment enrollment = buildDefaultEnrollment();
        UUID studentUuid = enrollment.getStudent().getUuid();
        String courseCode =  enrollment.getCourse().getCode();

        enrollmentRepository.save(enrollment);

        boolean result = enrollmentRepository.existsByStudentUuidAndCourseCode(studentUuid, courseCode);

        assertTrue(result);
    }

    @Test
    void existsByStudentUuidAndCourseCodeShouldReturnFalseWhenStudentUuidOrCourseCodeDoesNotExists() {

        Enrollment enrollment = buildDefaultEnrollment();
        UUID studentUuid = enrollment.getStudent().getUuid();

        enrollmentRepository.save(enrollment);

        // used a different courseCode
        boolean result = enrollmentRepository.existsByStudentUuidAndCourseCode(studentUuid, "01-MAT1");

        assertFalse(result);
    }

    private Enrollment buildDefaultEnrollment() {

        Student student = buildDefaultStudent();
        studentRepository.save(student);

        Course course = buildDefaultCourse();
        courseRepository.save(course);

        return new Enrollment(student, course, EnrollmentStatus.PENDING);
    }

    private Course buildDefaultCourse() {

        return Course.builder()
                .code("01-BIO1")
                .name("Biología 1")
                .build();
    }

    private Student buildDefaultStudent() {

        return Student.builder()
                .uuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .firstName("Carlos")
                .lastName("Rodriguez")
                .dni("45180765")
                .build();
    }
}
