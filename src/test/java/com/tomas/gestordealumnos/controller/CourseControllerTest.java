package com.tomas.gestordealumnos.controller;

import com.tomas.gestordealumnos.assembler.CourseModelAssembler;
import com.tomas.gestordealumnos.dto.CourseDto;
import com.tomas.gestordealumnos.exception.CourseNotFoundException;
import com.tomas.gestordealumnos.service.impl.CourseServiceImpl;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(CourseModelAssembler.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseServiceImpl courseService;

    @Test
    void addCourseShouldReturnCourseDtoAndStatus201() throws Exception {

        CourseDto courseDto = buildDefaultCourseDto();

        when(courseService.saveCourse(any(CourseDto.class))).thenReturn(courseDto);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(courseDto.getCode()))
                .andExpect(jsonPath("$.name").value(courseDto.getName()));
    }

    @Test
    void addCourseShouldReturnStatus400WhenDataIsInvalid() throws Exception {

        CourseDto courseDto = CourseDto.builder()
                .code("")
                .name("")
                .build();

        mockMvc.perform(post("/api/courses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed for one or more fields"))
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void findCourseByCodeShouldReturnCourseDtoAndStatus200() throws Exception {

        String courseCode = "01-MAT1";
        CourseDto expectedCourseDto = buildDefaultCourseDto();

        when(courseService.findCourseByCode(courseCode)).thenReturn(expectedCourseDto);

        mockMvc.perform(get("/api/courses/{code}", courseCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(courseCode))
                .andExpect(jsonPath("$.name").value(expectedCourseDto.getName()))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void findCourseByCodeShouldReturnStatus404WhenCodeDoesNotExists() throws Exception {
        String courseCode = "01-MAT1";

        when(courseService.findCourseByCode(courseCode)).thenThrow(new CourseNotFoundException("Course not found"));

        mockMvc.perform(get("/api/courses/{code}", courseCode)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void findAllCoursesShouldReturnAllCoursesDtoAndStatus200() throws Exception {

        CourseDto course1 = buildDefaultCourseDto();
        CourseDto course2 = buildAnotherDefaultCourseDto();

        when(courseService.findAllCourses()).thenReturn(List.of(course1, course2));

        mockMvc.perform(get("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.courseDtoList.size()").value(2))
                .andExpect(jsonPath("$._embedded.courseDtoList[0].code").value(course1.getCode()))
                .andExpect(jsonPath("$._embedded.courseDtoList[1].code").value(course2.getCode()));
    }

    @Test
    void updateCourseByCodeShouldReturnCourseDtoAndStatus200() throws Exception {

        CourseDto updateRequestDto = buildDefaultUpdatedCourseDto();
        String updateCourseCode = updateRequestDto.getCode();

        when(courseService.updateCourse(anyString(), any(CourseDto.class))).thenReturn(updateRequestDto);

        mockMvc.perform(put("/api/courses/{code}", updateCourseCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(updateCourseCode))
                .andExpect(jsonPath("$.name").value(updateRequestDto.getName()))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void updateCourseByCodeShouldReturnStatus400WhenDataIsInvalid() throws Exception {

        CourseDto updateRequestDto = CourseDto.builder()
                .code("")
                .name("").build();

        String courseCode = "01-MAT1";

        mockMvc.perform(put("/api/courses/{code}", courseCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed for one or more fields"))
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updateCourseByCodeShouldReturnStatus404WhenCodeDoesNotExists() throws Exception {

        String notExistingCourseCode = "01-LYT1";
        CourseDto courseDto = buildDefaultUpdatedCourseDto();

        when(courseService.updateCourse(eq(notExistingCourseCode), any(CourseDto.class))).thenThrow(new CourseNotFoundException("Course not found"));

        mockMvc.perform(put("/api/courses/{code}", notExistingCourseCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deleteCourseByCodeShouldReturnStatus200() throws Exception {

        String courseCode = "01-MAT1";

        mockMvc.perform(delete("/api/courses/{code}", courseCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCourseByCodeShouldReturnStatus404WhenCodeDoesNotExists() throws Exception {

        String notExistingCourseCode = "01-LYT1";

        doThrow(new CourseNotFoundException("Course not found")).when(courseService).deleteCourseByCode(notExistingCourseCode);

        mockMvc.perform(delete("/api/courses/{code}", notExistingCourseCode)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    private CourseDto buildDefaultCourseDto() {

        return CourseDto.builder()
                .code("01-MAT1")
                .name("Matemática 1")
                .build();
    }

    private CourseDto buildAnotherDefaultCourseDto() {

        return CourseDto.builder()
                .code("01-LYT1")
                .name("Lengua y Literatura 1")
                .build();
    }

    private CourseDto buildDefaultUpdatedCourseDto() {

        return CourseDto.builder()
                .code("01-MAT1")
                .name("MATEMÁTICA 01")
                .build();
    }
}
