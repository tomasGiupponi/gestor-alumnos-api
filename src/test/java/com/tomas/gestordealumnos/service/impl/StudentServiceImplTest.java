package com.tomas.gestordealumnos.service.impl;

import com.tomas.gestordealumnos.dto.StudentDto;
import com.tomas.gestordealumnos.exception.StudentNotFoundException;
import com.tomas.gestordealumnos.model.Student;
import com.tomas.gestordealumnos.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void saveStudentShouldReturnAStudentDto() {

        Student student = buildDefaultStudent();
        StudentDto studentDto = buildDefaultStudentDto();

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDto savedStudentDto = studentService.saveStudent(studentDto);

        assertNotNull(savedStudentDto);
        assertEquals(studentDto, savedStudentDto, "saved studentDto must be equal to studentDto");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void saveStudentShouldThrowIllegalArgumentExceptionWhenAlreadyDniExists() {

        StudentDto studentDto = buildDefaultStudentDto();
        String studentDni = studentDto.getDni();

        when(studentRepository.existsByDni(studentDni)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> studentService.saveStudent(studentDto));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void findAllStudentsShouldReturnAListOfAllStudentsDto() {

        Student student1 = buildDefaultStudent();
        Student student2 = buildAnotherDefaultStudent();
        StudentDto studentDto1 = buildDefaultStudentDto();
        StudentDto studentDto2 = buildAnotherDefaultStudentDto();

        List<Student> students = List.of(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDto> studentDtos = studentService.findAllStudents();

        assertNotNull(studentDtos);
        assertEquals(2, studentDtos.size(), "size of List studentDtos should be 2");
        assertEquals(studentDto1, studentDtos.get(0));
        assertEquals(studentDto2, studentDtos.get(1));
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void findAllStudentsShouldReturnAnEmptyListWhenNoStudentsFound() {

        when(studentRepository.findAll()).thenReturn(List.of());

        List<StudentDto> result = studentService.findAllStudents();

        assertEquals(List.of(), result, "The List should be empty");
    }

    @Test
    void findStudentByIdShouldReturnAStudentDto() {

        Student student = buildDefaultStudent();
        StudentDto studentDto = buildDefaultStudentDto();
        UUID studentUuid = student.getUuid();

        when(studentRepository.findByUuid(studentUuid)).thenReturn(Optional.of(student));

        StudentDto result = studentService.findStudentById(studentUuid);

        assertNotNull(result);
        assertEquals(studentDto, result, "saved studentDto must be equal to the studentDto found");
        verify(studentRepository, times(1)).findByUuid(studentUuid);
    }

    @Test
    void findStudentByIdShouldThrowStudentNotFoundExceptionWhenIdDoesNotExist() {

        Student student = buildDefaultStudent();
        UUID studentUuid = student.getUuid();

        when(studentRepository.findByUuid(studentUuid)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.findStudentById(studentUuid), "should throw StudentNotFoundException");
    }

    @Test
    void updateStudentShouldReturnAnUpdatedStudentDto() {

        Student student = buildDefaultStudent();
        Student savedUpdatedStudent = buildUpdatedDefaultStudent();
        StudentDto updateRequestDto = buildUpdatedDefaultStudentDto();

        UUID studentUuid = student.getUuid();

        when(studentRepository.findByUuid(studentUuid)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(savedUpdatedStudent);

        StudentDto result = studentService.updateStudent(studentUuid, updateRequestDto);

        assertNotNull(result);
        assertEquals(updateRequestDto, result, "updated StudentDto should be equal to the result");
        verify(studentRepository, times(1)).findByUuid(studentUuid);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void updateStudentShouldThrowStudentNotFoundExceptionWhenIdDoesNotExist() {

        StudentDto updateRequestDto = buildUpdatedDefaultStudentDto();
        UUID studentUuid = updateRequestDto.getId();

        when(studentRepository.findByUuid(studentUuid)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(studentUuid, updateRequestDto));
    }

    @Test
    void deleteStudentByIdShouldDeleteAStudent() {

        Student student = buildDefaultStudent();
        UUID studentUuid = student.getUuid();

        when(studentRepository.findByUuid(studentUuid)).thenReturn(Optional.of(student));

        studentService.deleteStudentById(studentUuid);
        verify(studentRepository, times(1)).findByUuid(studentUuid);
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    void deleteStudentByIdShouldThrowStudentNotFoundExceptionWhenIdDoesNotExist() {

        Student student = buildDefaultStudent();
        UUID studentUuid = student.getUuid();

        when(studentRepository.findByUuid(studentUuid)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudentById(studentUuid));
        verify(studentRepository, times(1)).findByUuid(studentUuid);
        verify(studentRepository, never()).delete(student);
    }

    private Student buildDefaultStudent() {

        return Student.builder()
                .uuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .firstName("Carlos")
                .lastName("Rodriguez")
                .dni("45180765")
                .build();
    }

    private StudentDto buildDefaultStudentDto() {

        return StudentDto.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .firstName("Carlos")
                .lastName("Rodriguez")
                .dni("45180765")
                .build();
    }

    private Student buildAnotherDefaultStudent() {

        return Student.builder()
                .uuid(UUID.fromString("f315e797-4335-4ceb-8baa-86d4912d9768"))
                .firstName("Pedro")
                .lastName("Catán")
                .dni("46082567")
                .build();
    }

    private StudentDto buildAnotherDefaultStudentDto() {

        return StudentDto.builder()
                .id(UUID.fromString("f315e797-4335-4ceb-8baa-86d4912d9768"))
                .firstName("Pedro")
                .lastName("Catán")
                .dni("46082567")
                .build();
    }

    private Student buildUpdatedDefaultStudent() {

        return Student.builder()
                .uuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .firstName("Carlos")
                .lastName("Rodriguez Gimenez")
                .dni("45180765")
                .build();
    }

    private StudentDto buildUpdatedDefaultStudentDto() {

        return StudentDto.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .firstName("Carlos")
                .lastName("Rodriguez Gimenez")
                .dni("45180765")
                .build();
    }
}
