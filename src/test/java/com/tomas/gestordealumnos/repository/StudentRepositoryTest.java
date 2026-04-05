package com.tomas.gestordealumnos.repository;

import com.tomas.gestordealumnos.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findByUuidShouldReturnAStudent() {

        Student student = buildDefaultStudent();
        UUID studentUuid = student.getUuid();

        studentRepository.save(student);

        Optional<Student> result = studentRepository.findByUuid(studentUuid);

        assertTrue(result.isPresent());
        assertEquals(student.getUuid(), result.get().getUuid());
        assertEquals(student.getDni(), result.get().getDni());
    }

    @Test
    void findByUuidShouldReturnEmptyOptionalWhenUuidDoesNotExists() {

        Optional<Student> result = studentRepository.findByUuid(UUID.randomUUID());

        assertFalse(result.isPresent());
    }

    @Test
    void existsByDniShouldReturnTrueWhenDniExists() {

        Student student = buildDefaultStudent();
        String studentDni = student.getDni();

        studentRepository.save(student);

        boolean result = studentRepository.existsByDni(studentDni);

        assertTrue(result);
    }

    @Test
    void existsByDniShouldReturnFalseWhenDniDoesNotExists() {

        boolean result = studentRepository.existsByDni("42985762");

        assertFalse(result);
    }

    @Test
    void existsByUuidShouldReturnTrueWhenUuidExists() {

        Student student = buildDefaultStudent();
        UUID studentUuid = student.getUuid();

        studentRepository.save(student);

        boolean result = studentRepository.existsByUuid(studentUuid);

        assertTrue(result);
    }

    @Test
    void existsByUuidShouldReturnFalseWhenUuidDoesNotExists() {

        boolean result = studentRepository.existsByDni("42985762");

        assertFalse(result);
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
