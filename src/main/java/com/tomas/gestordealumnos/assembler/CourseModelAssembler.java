package com.tomas.gestordealumnos.assembler;

import com.tomas.gestordealumnos.controller.CourseController;
import com.tomas.gestordealumnos.dto.CourseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CourseModelAssembler implements RepresentationModelAssembler<CourseDto, EntityModel<CourseDto>> {

    @Override
    public EntityModel<CourseDto> toModel(CourseDto courseDto) {
        return EntityModel.of(courseDto,
                linkTo(methodOn(CourseController.class).findCourse(courseDto.getCode())).withSelfRel(),
                linkTo(methodOn(CourseController.class).findAllCourses()).withRel("courses"));
    }
}
