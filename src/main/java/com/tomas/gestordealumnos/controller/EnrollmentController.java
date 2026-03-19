package com.tomas.gestordealumnos.controller;

import com.tomas.gestordealumnos.assembler.EnrollmentModelAssembler;
import com.tomas.gestordealumnos.dto.EnrollmentDto;
import com.tomas.gestordealumnos.dto.EnrollmentUpdateDto;
import com.tomas.gestordealumnos.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final EnrollmentModelAssembler enrollmentModelAssembler;

    public EnrollmentController(EnrollmentService enrollmentService, EnrollmentModelAssembler enrollmentModelAssembler) {
        this.enrollmentService = enrollmentService;
        this.enrollmentModelAssembler = enrollmentModelAssembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<EnrollmentDto>> createEnrollment(@Valid @RequestBody EnrollmentDto enrollmentDto) {

        EnrollmentDto savedEnrollmentDto = enrollmentService.createEnrollment(enrollmentDto);
        EntityModel<EnrollmentDto> enrollmentDtoModel = enrollmentModelAssembler.toModel(savedEnrollmentDto);

        return new ResponseEntity<>(enrollmentDtoModel, HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentUuid}")
    public ResponseEntity<CollectionModel<EntityModel<EnrollmentDto>>> getEnrollmentByStudent(@PathVariable UUID studentUuid) {

        List<EnrollmentDto> enrollmentsDto = enrollmentService.getEnrollmentsByStudent(studentUuid);
        CollectionModel<EntityModel<EnrollmentDto>> collectionModel = enrollmentModelAssembler.toCollectionModel(enrollmentsDto);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping("/course/{courseCode}")
    public ResponseEntity<CollectionModel<EntityModel<EnrollmentDto>>> getEnrollmentByCourse(@PathVariable String courseCode) {

        List<EnrollmentDto> enrollmentsDto = enrollmentService.getEnrollmentsByCourse(courseCode);
        CollectionModel<EntityModel<EnrollmentDto>> collectionModel = enrollmentModelAssembler.toCollectionModel(enrollmentsDto);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EnrollmentDto>> updateEnrollment(@PathVariable Long id,@Valid @RequestBody EnrollmentUpdateDto enrollmentUpdateDto) {

        EnrollmentDto updatedEnrollment =  enrollmentService.updateEnrollment(id, enrollmentUpdateDto);

        return new ResponseEntity<>(enrollmentModelAssembler.toModel(updatedEnrollment), HttpStatus.OK);
    }
}
