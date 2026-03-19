package com.tomas.gestordealumnos.service.impl;

import com.tomas.gestordealumnos.dto.CourseDto;
import com.tomas.gestordealumnos.exception.CourseNotFoundException;
import com.tomas.gestordealumnos.mapper.CourseMapper;
import com.tomas.gestordealumnos.model.Course;
import com.tomas.gestordealumnos.repository.CourseRepository;
import com.tomas.gestordealumnos.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) { this.courseRepository = courseRepository; }

    @Override
    public CourseDto saveCourse(CourseDto courseDto) {

        if(courseRepository.existsByCode(courseDto.getCode())) {
            throw new IllegalArgumentException("Code already exists");
        }

        Course course = CourseMapper.toEntity(courseDto);

        return CourseMapper.toDto(courseRepository.save(course));
    }

    @Override
    public List<CourseDto> findAllCourses() {

        return courseRepository.findAll().stream().map(CourseMapper::toDto).toList();
    }

    @Override
    public CourseDto findCourseByCode(String code) {

        Course course = findCourseEntityByCode(code);

        return CourseMapper.toDto(course);
    }

    @Override
    public CourseDto updateCourse(String code, CourseDto courseDto) {

        Course course = findCourseEntityByCode(code);

        course.setCode(courseDto.getCode());
        course.setName(courseDto.getName());

        return CourseMapper.toDto(courseRepository.save(course));
    }

    @Override
    public void deleteCourseByCode(String code) {

        Course course = findCourseEntityByCode(code);

        courseRepository.delete(course);
    }

    private Course findCourseEntityByCode(String code) {

        return courseRepository.findByCode(code)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
    }
}
