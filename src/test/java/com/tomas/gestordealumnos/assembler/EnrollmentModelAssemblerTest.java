package com.tomas.gestordealumnos.assembler;

import com.tomas.gestordealumnos.dto.EnrollmentDto;
import com.tomas.gestordealumnos.enumeration.EnrollmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentModelAssemblerTest {

    EnrollmentModelAssembler assembler = new EnrollmentModelAssembler();

    @Test
    void shouldAddLinksToEnrollmentDto() {

        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .studentUuid(UUID.randomUUID())
                .courseCode("01-LYT1")
                .status(EnrollmentStatus.PENDING)
                .build();

        EntityModel<EnrollmentDto> entityModel = assembler.toModel(enrollmentDto);

        assertNotNull(entityModel);
        assertEquals(enrollmentDto, entityModel.getContent(), "entityModel must contain enrollmentDto");
        assertEquals(2, entityModel.getLinks().toList().size(), "amount of links must be 2");

        assertTrue(entityModel.hasLink("student"), "entityModel must contain student link");
        Link studentLink = entityModel.getRequiredLink("student");
        assertTrue(studentLink.getHref().endsWith("/"+enrollmentDto.getStudentUuid()));

        assertTrue(entityModel.hasLink("course"), "entityModel must contain course link");
        Link courseLink = entityModel.getRequiredLink("course");
        assertTrue(courseLink.getHref().endsWith("/"+enrollmentDto.getCourseCode()));
    }

    @Test
    void shouldThrowExceptionWhenStudentDtoUuidIsNull() {

        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .studentUuid(null)
                .courseCode("01-LYT1")
                .build();

        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(enrollmentDto));
    }

    @Test
    void shouldThrowExceptionWhenCourseDtoCodeIsNull() {

        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .studentUuid(UUID.randomUUID())
                .courseCode(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(enrollmentDto));
    }

    @Test
    void shouldThrowExceptionWhenCourseDtoCodeIsNullAndStudentDtoUuidIsNull() {

        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .studentUuid(null)
                .courseCode(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(enrollmentDto));
    }
}
