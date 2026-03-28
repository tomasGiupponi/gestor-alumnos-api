package com.tomas.gestordealumnos.assembler;

import com.tomas.gestordealumnos.dto.CourseDto;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;

public class CourseModelAssemblerTest {

    private final CourseModelAssembler assembler = new CourseModelAssembler();

    @Test
    void shouldAddLinksToCourseDto() {

        CourseDto courseDto = CourseDto.builder()
                .code("01-LYT1")
                .name("Lengua y Literatura 1")
                .build();

        EntityModel<CourseDto> entityModel = assembler.toModel(courseDto);

        assertNotNull(entityModel);
        assertEquals(courseDto, entityModel.getContent(), "entityModel must contain courseDto");
        assertEquals(2, entityModel.getLinks().toList().size(), "amount of links should be 2");
        assertTrue(entityModel.hasLink(IanaLinkRelations.SELF), "entityModel must contain self");

        Link self = entityModel.getRequiredLink(IanaLinkRelations.SELF);
        assertTrue(self.getHref().endsWith("/"+courseDto.getCode()), "entityModel must end with /"+courseDto.getCode());
    }

    @Test
    void shouldThrowExceptionWhenCourseDtoIsNull() {

        assertThrows(NullPointerException.class, () -> assembler.toModel(null), "should throw NullPointerException when courseDto is null");
    }

    @Test
    void shouldThrowExceptionWhenCourseDtoCodeIsEmpty() {

        CourseDto courseDto = CourseDto.builder()
                .code(null)
                .name("Matemática 1")
                .build();

        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(courseDto), "should throw IllegalArgumentException when courseDto code is null");
    }
}
