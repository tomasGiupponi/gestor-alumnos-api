package com.tomas.gestordealumnos.mapper;

import com.tomas.gestordealumnos.dto.StudentDto;
import com.tomas.gestordealumnos.model.Student;

public class StudentMapper {

    //From Student to StudentDto
    public static StudentDto toDto(Student student) {
        if (student == null) return null;

        return StudentDto.builder()
                .id(student.getUuid())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dni(student.getDni())
                .build();
    }

    //From StudentDto to Student
    public static Student toEntity(StudentDto dto) {
        if (dto == null) return null;

        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dni(dto.getDni())
                .build();
    }
}
