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

    @Test
    void saveCourseShouldSaveACourseAndReturnACourseDto() {

        Course course = buildDefaultCourse();
        CourseDto courseDto = buildDefaultCourseDto();

        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDto result = courseServiceImpl.saveCourse(courseDto);

        assertNotNull(result);
        assertEquals(courseDto.getCode(), result.getCode(), "courseDto code must be equal to result code");
        assertEquals(courseDto, result, "courseDto must be equal to result");
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void saveCourseShouldThrowExceptionWhenCourseCodeAlreadyExists() {

        Course course = buildDefaultCourse();
        CourseDto courseDto = buildDefaultCourseDto();
        String courseCode = course.getCode();

        when(courseRepository.existsByCode(courseCode)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> courseServiceImpl.saveCourse(courseDto), "should throw IllegalArgumentException");

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void findCourseByCodeShouldReturnACourseDto() {

        Course course = buildDefaultCourse();
        String courseCode = course.getCode();

        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.of(course));

        CourseDto result = courseServiceImpl.findCourseByCode(courseCode);

        assertNotNull(result);
        assertEquals(course.getCode(), result.getCode(), "course code must be equal to result code");
        verify(courseRepository, times(1)).findByCode(courseCode);
    }

    @Test
    void findCourseByCodeShouldThrowExceptionWhenCourseCodeDoesNotExist() {

        Course course = buildDefaultCourse();
        String courseCode = course.getCode();

        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseServiceImpl.findCourseByCode(courseCode), "should throw CourseNotFoundException");
    }

    @Test
    void findAllCoursesShouldReturnAListOfCourseDto() {

        Course course1 = buildDefaultCourse();
        Course course2 = buildAnotherDefaultCourse();

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

        Course course = buildDefaultCourse();
        Course updatedCourse = buildUpdatedDefaultCourse();
        CourseDto updatedCourseDto = buildUpdatedDefaultCourseDto();

        String courseCode = course.getCode();

        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        CourseDto result = courseServiceImpl.updateCourse(courseCode, updatedCourseDto);

        assertNotNull(result);
        assertEquals(course.getCode(), result.getCode(), "course code must be equal to result code");
        assertEquals(updatedCourseDto.getName(), result.getName(), "course name must be equal to result name");
        verify(courseRepository, times(1)).findByCode(courseCode);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void updateCourseShouldThrowCourseNotFoundExceptionWhenCourseCodeDoesNotExist() {

        Course course = buildDefaultCourse();
        CourseDto updatedCourseDto = buildUpdatedDefaultCourseDto();
        String courseCode = course.getCode();

        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseServiceImpl.updateCourse(courseCode, updatedCourseDto));
    }

    @Test
    void deleteCourseByCodeShouldDeleteACourse() {

        Course course = buildDefaultCourse();
        String courseCode = course.getCode();

        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.of(course));

        courseServiceImpl.deleteCourseByCode(courseCode);

        verify(courseRepository, times(1)).findByCode(courseCode);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void deleteCourseByCodeShouldThrowCourseNotFoundExceptionWhenCourseCodeDoesNotExist() {

        Course course = buildDefaultCourse();
        String courseCode = course.getCode();

        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseServiceImpl.deleteCourseByCode(courseCode));
    }

    private Course buildDefaultCourse() {

        return Course.builder()
                .code("01-LYT1")
                .name("Lengua y Literatura 1")
                .build();
    }

    private CourseDto buildDefaultCourseDto() {

        return CourseDto.builder()
                .code("01-LYT1")
                .name("Lengua y Literatura 1")
                .build();
    }

    private Course buildAnotherDefaultCourse() {

        return Course.builder()
                .code("01-MAT1")
                .name("Matemática 1")
                .build();
    }

    private Course buildUpdatedDefaultCourse() {

        return Course.builder()
                .code("01-LYT1")
                .name("LENGUA Y LITERATURA 1")
                .build();
    }

    private CourseDto buildUpdatedDefaultCourseDto() {

        return CourseDto.builder()
                .code("01-LYT1")
                .name("LENGUA Y LITERATURA 1")
                .build();
    }
}
