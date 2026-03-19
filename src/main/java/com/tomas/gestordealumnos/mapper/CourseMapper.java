package com.tomas.gestordealumnos.mapper;

import com.tomas.gestordealumnos.dto.CourseDto;
import com.tomas.gestordealumnos.model.Course;

public class CourseMapper {

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
