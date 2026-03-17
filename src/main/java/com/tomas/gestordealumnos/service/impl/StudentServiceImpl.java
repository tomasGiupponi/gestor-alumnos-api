package com.tomas.gestordealumnos.service.impl;

import com.tomas.gestordealumnos.dto.StudentDto;
import com.tomas.gestordealumnos.exception.StudentNotFoundException;
import com.tomas.gestordealumnos.mapper.Mapper;
import com.tomas.gestordealumnos.model.Student;
import com.tomas.gestordealumnos.repository.StudentRepository;
import com.tomas.gestordealumnos.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) { this.studentRepository = studentRepository; }

    @Override
    public StudentDto saveStudent(StudentDto studentDto) {

        Student student = Mapper.toEntity(studentDto);

        return Mapper.toDto(studentRepository.save(student));
    }

    @Override
    public List<StudentDto> findAllStudents() {

        return studentRepository.findAll().stream().map(Mapper::toDto).toList();
    }

    @Override
    public StudentDto findStudentById(UUID id) {

        Student student = studentRepository.findByUuid(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        return Mapper.toDto(student);
    }

    @Override
    public StudentDto updateStudent(UUID id, StudentDto studentDto) {

        Student existingStudent = studentRepository.findByUuid(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        existingStudent.setFirstName(studentDto.getFirstName());
        existingStudent.setLastName(studentDto.getLastName());
        existingStudent.setDni(studentDto.getDni());

        return Mapper.toDto(studentRepository.save(existingStudent));
    }

    @Override
    public void deleteStudentById(UUID id) {

        Student student = studentRepository.findByUuid(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        studentRepository.delete(student);
    }
}
