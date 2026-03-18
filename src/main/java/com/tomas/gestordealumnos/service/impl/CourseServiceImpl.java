package com.tomas.gestordealumnos.service.impl;

import com.tomas.gestordealumnos.dto.CourseDto;
import com.tomas.gestordealumnos.mapper.Mapper;
import com.tomas.gestordealumnos.model.Course;
import com.tomas.gestordealumnos.repository.CourseRepository;
import com.tomas.gestordealumnos.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) { this.courseRepository = courseRepository; }

    @Override
    public CourseDto saveCourse(CourseDto courseDto) {

        if(courseRepository.existsByCode(courseDto.getCode())) {
            throw new IllegalArgumentException("Code already exists");
        }

        Course course = Mapper.toEntity(courseDto);

        return Mapper.toDto(courseRepository.save(course));
    }

    
}
