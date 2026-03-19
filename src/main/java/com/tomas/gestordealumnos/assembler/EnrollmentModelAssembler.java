package com.tomas.gestordealumnos.assembler;

import com.tomas.gestordealumnos.controller.CourseController;
import com.tomas.gestordealumnos.controller.StudentController;
import com.tomas.gestordealumnos.dto.EnrollmentDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EnrollmentModelAssembler implements RepresentationModelAssembler<EnrollmentDto, EntityModel<EnrollmentDto>> {

    @Override
    public EntityModel<EnrollmentDto> toModel(EnrollmentDto enrollmentDto) {
        return EntityModel.of(enrollmentDto,
                linkTo(methodOn(StudentController.class).findStudentById(enrollmentDto.getStudentUuid())).withRel("student"),
                linkTo(methodOn(CourseController.class).findCourse(enrollmentDto.getCourseCode())).withRel("course"));
    }
}
