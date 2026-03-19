package com.tomas.gestordealumnos.service.impl;

import com.tomas.gestordealumnos.dto.StudentDto;
import com.tomas.gestordealumnos.exception.StudentNotFoundException;
import com.tomas.gestordealumnos.mapper.StudentMapper;
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

        if(studentRepository.existsByDni(studentDto.getDni())) {
            throw new IllegalArgumentException("Dni already exists");
        }

        Student student = StudentMapper.toEntity(studentDto);

        return StudentMapper.toDto(studentRepository.save(student));
    }

    @Override
    public List<StudentDto> findAllStudents() {

        return studentRepository.findAll().stream().map(StudentMapper::toDto).toList();
    }

    @Override
    public StudentDto findStudentById(UUID id) {

        Student student = findStudentByUuid(id);

        return StudentMapper.toDto(student);
    }

    @Override
    public StudentDto updateStudent(UUID id, StudentDto studentDto) {

        Student existingStudent = findStudentByUuid(id);

        existingStudent.setFirstName(studentDto.getFirstName());
        existingStudent.setLastName(studentDto.getLastName());
        existingStudent.setDni(studentDto.getDni());

        return StudentMapper.toDto(studentRepository.save(existingStudent));
    }

    @Override
    public void deleteStudentById(UUID id) {

        Student student = findStudentByUuid(id);

        studentRepository.delete(student);
    }

    private Student findStudentByUuid(UUID uuid) {

        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }
}
