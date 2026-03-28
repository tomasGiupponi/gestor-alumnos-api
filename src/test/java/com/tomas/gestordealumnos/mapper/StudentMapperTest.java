package com.tomas.gestordealumnos.mapper;

import com.tomas.gestordealumnos.dto.StudentDto;
import com.tomas.gestordealumnos.model.Student;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentMapperTest {

    @Test
    void shouldMapStudentDtoToStudent() {

        Student student = Student.builder()
                .uuid(UUID.randomUUID())
                .firstName("Pepe")
                .lastName("Perez")
                .dni("12345678")
                .build();

        StudentDto studentDto = StudentMapper.toDto(student);

        assertEquals(student.getFirstName(), studentDto.getFirstName(), "firstName should match");
        assertEquals(student.getLastName(), studentDto.getLastName(), "lastName should match");
        assertEquals(student.getDni(), studentDto.getDni(), "dni should match");
    }

    @Test
    void shouldMapStudentToStudentDto() {

        StudentDto studentDto = StudentDto.builder()
                .firstName("Pepe")
                .lastName("Perez")
                .dni("12345678")
                .build();

        Student student = StudentMapper.toEntity(studentDto);

        assertEquals(studentDto.getFirstName(), student.getFirstName(), "firstName should match");
        assertEquals(studentDto.getLastName(), student.getLastName(), "lastName should match");
        assertEquals(studentDto.getDni(), student.getDni(), "dni should match");
    }

    @Test
    void shouldReturnNullWhenStudentIsNull() {
        Student student = null;

        StudentDto studentDto = StudentMapper.toDto(student);

        assertNull(studentDto, "should return null when student is null");
    }

    @Test
    void shouldReturnNullWhenStudentDtoIsNull() {
        StudentDto studentDto = null;

        Student student = StudentMapper.toEntity(studentDto);

        assertNull(student, "should return null when student is null");
    }
}
