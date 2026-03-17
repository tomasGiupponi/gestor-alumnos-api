package com.tomas.gestordealumnos.assembler;

import com.tomas.gestordealumnos.controller.StudentController;
import com.tomas.gestordealumnos.dto.StudentDto;
import com.tomas.gestordealumnos.model.Student;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<StudentDto, EntityModel<StudentDto>> {

    @Override
    public EntityModel<StudentDto> toModel(StudentDto studentDto) {
        return EntityModel.of(studentDto,
                linkTo(methodOn(StudentController.class).findStudentById(studentDto.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).findAllStudents()).withRel("students"));
    }
}
