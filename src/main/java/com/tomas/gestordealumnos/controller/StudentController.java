package com.tomas.gestordealumnos.controller;

import com.tomas.gestordealumnos.assembler.StudentModelAssembler;
import com.tomas.gestordealumnos.dto.StudentDto;
import com.tomas.gestordealumnos.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentModelAssembler  studentModelAssembler;

    public StudentController(StudentService studentService, StudentModelAssembler studentModelAssembler) {

        this.studentService = studentService;
        this.studentModelAssembler = studentModelAssembler;
    }

    @PostMapping
    public ResponseEntity<StudentDto> saveStudent(@Valid @RequestBody StudentDto studentDto) {

        return new ResponseEntity<>(studentService.saveStudent(studentDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<StudentDto>>> findAllStudents() {

        List<StudentDto> studentsDto = studentService.findAllStudents();
        CollectionModel<EntityModel<StudentDto>> collectionModel = studentModelAssembler.toCollectionModel(studentsDto);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<StudentDto>> findStudentById(@PathVariable UUID id) {

        StudentDto studentDto = studentService.findStudentById(id);
        EntityModel<StudentDto> studentModel = studentModelAssembler.toModel(studentDto);

        return new ResponseEntity<>(studentModel, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<StudentDto>> updateStudent(@PathVariable UUID id,@Valid @RequestBody StudentDto studentDto) {

        StudentDto updatedStudentDto = studentService.updateStudent(id, studentDto);
        EntityModel<StudentDto> studentModel = studentModelAssembler.toModel(updatedStudentDto);

        return new ResponseEntity<>(studentModel, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable UUID id) {

        studentService.deleteStudentById(id);

        return new ResponseEntity<>("Student deleted successfully.", HttpStatus.OK);
    }
}
