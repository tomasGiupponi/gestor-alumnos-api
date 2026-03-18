package com.tomas.gestordealumnos.mapper;

import com.tomas.gestordealumnos.dto.CourseDto;
import com.tomas.gestordealumnos.dto.StudentDto;
import com.tomas.gestordealumnos.model.Course;
import com.tomas.gestordealumnos.model.Student;

public class Mapper {

    /*

    STUDENT MAPPER

    */
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

    /*

    COURSE MAPPER

    */
    //From Course to CourseDto
    public static CourseDto toDto(Course course) {
        if (course == null) return null;

        return CourseDto.builder()
                .code(course.getCode())
                .name(course.getName())
                .build();
    }

    //From CourseDto to Course
    public static Course toEntity(CourseDto dto) {
        if (dto == null) return null;

        return Course.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .build();
    }
}
