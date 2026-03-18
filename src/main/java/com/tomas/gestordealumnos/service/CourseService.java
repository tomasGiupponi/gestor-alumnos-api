package com.tomas.gestordealumnos.service;

import com.tomas.gestordealumnos.dto.CourseDto;

import java.util.List;

public interface CourseService {

    CourseDto saveCourse(CourseDto courseDto);
    List<CourseDto> findAllCourses();
    CourseDto findCourseByCode(String code);
    CourseDto updateCourse(String code, CourseDto courseDto);
    void deleteCourseByCode(String code);
}
