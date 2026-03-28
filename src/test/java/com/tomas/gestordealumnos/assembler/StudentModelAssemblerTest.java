package com.tomas.gestordealumnos.assembler;

import com.tomas.gestordealumnos.dto.StudentDto;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentModelAssemblerTest {

    private final StudentModelAssembler assembler = new StudentModelAssembler();

    @Test
    void shouldAddLinksToStudentDto() {

        // --- ARRANGE ---
        StudentDto studentDto = StudentDto.builder()
                .id(UUID.randomUUID())
                .firstName("Carlos")
                .lastName("Catán")
                .dni("12345678")
                .build();

        // --- ACT ---
        EntityModel<StudentDto> entityModel = assembler.toModel(studentDto);

        // --- ASSERT ---
        assertNotNull(entityModel);
        assertEquals(studentDto, entityModel.getContent(), "entityModel must contain studentDto");
        assertEquals(2, entityModel.getLinks().toList().size(), "amount of links should be 2");
        assertTrue(entityModel.hasLink(IanaLinkRelations.SELF), "entityModel must contain self");

        Link self = entityModel.getRequiredLink(IanaLinkRelations.SELF);
        assertTrue(self.getHref().endsWith("/"+studentDto.getId()), "entityModel must end with /"+studentDto.getId());
    }

    @Test
    void shouldThrowExceptionWhenStudentDtoIsNull() {

        StudentDto studentDto = null;

        assertThrows(NullPointerException.class, () -> assembler.toModel(studentDto), "should throw NullPointerException");
    }

    @Test
    void shouldThrowExceptionWhenStudentDtoIdIsEmpty() {

        StudentDto studentDto = StudentDto.builder()
                .id(null)
                .firstName("Carlos")
                .lastName("Catán")
                .dni("12345678")
                .build();

        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(studentDto), "should throw IllegalArgumentException");
    }
}
