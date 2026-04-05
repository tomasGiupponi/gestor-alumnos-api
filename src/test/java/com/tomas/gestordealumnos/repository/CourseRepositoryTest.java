package com.tomas.gestordealumnos.repository;

import com.tomas.gestordealumnos.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findByCodeShouldReturnACourse() {

        Course course = buildDefaultCourse();
        String courseCode = course.getCode();

        courseRepository.save(course);

        Optional<Course> result = courseRepository.findByCode(courseCode);

        assertTrue(result.isPresent());
        assertEquals(course.getCode(), result.get().getCode());
        assertEquals(course.getName(), result.get().getName());
    }

    @Test
    void findByCodeShouldReturnEmptyOptionalWhenCodeDoesNotExist() {

        Optional<Course> result = courseRepository.findByCode("01-MAT1");

        assertFalse(result.isPresent());
    }

    @Test
    void existsByCodeShouldReturnTrueWhenCodeExists() {

        Course course = buildDefaultCourse();
        String courseCode = course.getCode();

        courseRepository.save(course);

        boolean result = courseRepository.existsByCode(courseCode);

        assertTrue(result);
    }

    @Test
    void existsByCodeShouldReturnFalseWhenCodeDoesNotExist() {

        boolean result = courseRepository.existsByCode("01-MAT1");

        assertFalse(result);
    }

    private Course buildDefaultCourse() {

        return Course.builder()
                .code("01-BIO1")
                .name("Biología 1")
                .build();
    }
}
