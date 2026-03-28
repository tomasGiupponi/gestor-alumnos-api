package com.tomas.gestordealumnos.mapper;

import com.tomas.gestordealumnos.dto.CourseDto;
import com.tomas.gestordealumnos.model.Course;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseMapperTest {

    @Test
    void shouldMapCourseToCourseDto() {

        Course course = Course.builder()
                .code("01-LYT1")
                .name("Lengua y Literatura 1")
                .enrollments(null)
                .build();

        CourseDto courseDto = CourseMapper.toDto(course);

        assertEquals(course.getCode(), courseDto.getCode(), "course code should match");
        assertEquals(course.getName(), courseDto.getName(), "course name should match");
        assertNull(course.getEnrollments(), "course enrollments should match");
    }

    @Test
    void shouldMapCourseDtoToCourse() {

        CourseDto courseDto = CourseDto.builder()
                .code("01-LYT1")
                .name("Lengua y Literatura 1")
                .build();

        Course course = CourseMapper.toEntity(courseDto);

        assertEquals(courseDto.getCode(), course.getCode(), "course code should match");
        assertEquals(courseDto.getName(), course.getName(), "course name should match");
    }

    @Test
    void shouldReturnNullWhenCourseIsNull() {

        Course course = null;

        CourseDto courseDto = CourseMapper.toDto(course);

        assertNull(courseDto, "should return null when course is null");
    }

    @Test
    void shouldReturnNullWhenCourseDtoIsNull() {

        CourseDto courseDto = null;

        Course course =  CourseMapper.toEntity(courseDto);

        assertNull(course, "should return null when courseDto is null");
    }
}
