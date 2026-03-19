package com.tomas.gestordealumnos.controller;

import com.tomas.gestordealumnos.assembler.CourseModelAssembler;
import com.tomas.gestordealumnos.dto.CourseDto;
import com.tomas.gestordealumnos.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseModelAssembler courseModelAssembler;

    public CourseController(CourseService courseService, CourseModelAssembler courseModelAssembler) {
        this.courseService = courseService;
        this.courseModelAssembler = courseModelAssembler;
    }

    @PostMapping
    public ResponseEntity<CourseDto> addCourse(@Valid @RequestBody CourseDto courseDto) {

        return new ResponseEntity<>(courseService.saveCourse(courseDto), HttpStatus.CREATED);
    }

    @GetMapping("{code}")
    public ResponseEntity<EntityModel<CourseDto>> findCourse(@PathVariable String code) {

        CourseDto courseDto = courseService.findCourseByCode(code);
        EntityModel<CourseDto> courseModel = courseModelAssembler.toModel(courseDto);

        return new ResponseEntity<>(courseModel, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CourseDto>>> findAllCourses() {

        List<CourseDto> coursesDto = courseService.findAllCourses();
        CollectionModel<EntityModel<CourseDto>> collectionModel = courseModelAssembler.toCollectionModel(coursesDto);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("{code}")
    public ResponseEntity<EntityModel<CourseDto>> updateCourse(@PathVariable String code, @Valid @RequestBody CourseDto courseDto) {

        CourseDto updatedCourseDto = courseService.updateCourse(code, courseDto);
        EntityModel<CourseDto> courseModel = courseModelAssembler.toModel(updatedCourseDto);

        return new ResponseEntity<>(courseModel, HttpStatus.OK);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<String> deleteCourse(@PathVariable String code) {

        courseService.deleteCourseByCode(code);

        return new ResponseEntity<>("Course deleted successfully.",HttpStatus.OK);
    }
}
