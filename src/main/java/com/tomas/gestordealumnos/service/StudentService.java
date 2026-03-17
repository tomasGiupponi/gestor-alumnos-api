package com.tomas.gestordealumnos.service;

import com.tomas.gestordealumnos.dto.StudentDto;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    StudentDto saveStudent(StudentDto studentDto);
    List<StudentDto> findAllStudents();
    StudentDto findStudentById(UUID id);
    StudentDto updateStudent(UUID id, StudentDto studentDto);
    void deleteStudentById(UUID id);
}
