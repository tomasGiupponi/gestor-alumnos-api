package com.tomas.gestordealumnos.service.impl;

import com.tomas.gestordealumnos.dto.CourseDto;
import com.tomas.gestordealumnos.exception.CourseNotFoundException;
import com.tomas.gestordealumnos.model.Course;
import com.tomas.gestordealumnos.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    String code = "01-LYT1";
    String name = "Lengua y Literatura 1";

    @Test
    void saveCourseShouldSaveACourseAndReturnACourseDto() {

        CourseDto courseDto = CourseDto.builder()
                .code(code)
                .name(name)
                .build();

        Course savedCourse = Course.builder()
                .code(code)
                .name(name)
                .build();

        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        CourseDto result = courseServiceImpl.saveCourse(courseDto);

        assertNotNull(result);
        assertEquals(courseDto.getCode(), result.getCode(), "courseDto code must be equal to result code");
        assertEquals(courseDto, result, "courseDto must be equal to result");
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldThrowExceptionWhenCourseCodeAlreadyExists() {

        CourseDto courseDto = CourseDto.builder()
                .code(code)
                .name(name)
                .build();

        when(courseRepository.existsByCode(code)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> courseServiceImpl.saveCourse(courseDto), "should throw IllegalArgumentException");

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void findCourseByCodeShouldReturnACourseDto() {

        Course course = Course.builder()
                .code(code)
                .name(name)
                .build();

        when(courseRepository.findByCode(code)).thenReturn(Optional.of(course));

        CourseDto result = courseServiceImpl.findCourseByCode(code);

        assertNotNull(result);
        assertEquals(course.getCode(), result.getCode(), "course code must be equal to result code");
        verify(courseRepository, times(1)).findByCode(code);
    }

    @Test
    void shouldThrowExceptionWhenCourseCodeDoesNotExist() {

        when(courseRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseServiceImpl.findCourseByCode(code), "should throw CourseNotFoundException");
    }

    @Test
    void findAllCoursesShouldReturnAListOfCourseDto() {

        Course course1 = Course.builder()
                .code(code)
                .name(name)
                .build();

        Course course2 = Course.builder()
                .code("01-MAT1")
                .name("Matemática 1")
                .build();

        List<Course> courses = List.of(course1, course2);

        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDto> result = courseServiceImpl.findAllCourses();

        assertNotNull(result);
        assertEquals(2, result.size(), "size of the list result should be 2");
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void findAllCoursesShouldReturnANullListWhenNoCourseFound() {

        when(courseRepository.findAll()).thenReturn(List.of());

        assertEquals(List.of(), courseServiceImpl.findAllCourses());
    }

    @Test
    void updateCourseShouldReturnAnUpdatedCourseDto() {

        Course existingCourse = Course.builder()
                .code(code)
                .name(name)
                .build();

        CourseDto updateRequestDto = CourseDto.builder()
                .code(code)
                .name("LENGUA Y LITERATURA 1")
                .build();

        Course savedUpdatedCourse = Course.builder()
                .code(code)
                .name("LENGUA Y LITERATURA 1")
                .build();

        when(courseRepository.findByCode(code)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(savedUpdatedCourse);

        CourseDto result = courseServiceImpl.updateCourse(code, updateRequestDto);

        assertNotNull(result);
        assertEquals(existingCourse.getCode(), result.getCode(), "course code must be equal to result code");
        assertEquals(updateRequestDto.getName(), result.getName(), "course name must be equal to result name");
        verify(courseRepository, times(1)).findByCode(code);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void updateCourseShouldThrowCourseNotFoundExceptionWhenCourseCodeDoesNotExist() {

        CourseDto courseDto = CourseDto.builder()
                .code("01-MAT1")
                .name("Matemática 1").build();

        when(courseRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseServiceImpl.updateCourse(code, courseDto));
    }

    @Test
    void deleteCourseByCodeShouldDeleteACourse() {

        Course course = Course.builder()
                .code(code)
                .name(name).build();

        when(courseRepository.findByCode(code)).thenReturn(Optional.of(course));

        courseServiceImpl.deleteCourseByCode(code);

        verify(courseRepository, times(1)).findByCode(code);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void deleteCourseByCodeShouldThrowCourseNotFoundExceptionWhenCourseCodeDoesNotExist() {

        when(courseRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseServiceImpl.deleteCourseByCode(code));
    }
}
