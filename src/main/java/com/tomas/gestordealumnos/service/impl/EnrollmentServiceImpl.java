package com.tomas.gestordealumnos.service.impl;

import com.tomas.gestordealumnos.dto.EnrollmentDto;
import com.tomas.gestordealumnos.dto.EnrollmentUpdateDto;
import com.tomas.gestordealumnos.enumeration.EnrollmentStatus;
import com.tomas.gestordealumnos.exception.CourseNotFoundException;
import com.tomas.gestordealumnos.exception.EnrollmentNotFoundException;
import com.tomas.gestordealumnos.exception.StudentNotFoundException;
import com.tomas.gestordealumnos.mapper.EnrollmentMapper;
import com.tomas.gestordealumnos.model.Course;
import com.tomas.gestordealumnos.model.Enrollment;
import com.tomas.gestordealumnos.model.Student;
import com.tomas.gestordealumnos.repository.CourseRepository;
import com.tomas.gestordealumnos.repository.EnrollmentRepository;
import com.tomas.gestordealumnos.repository.StudentRepository;
import com.tomas.gestordealumnos.service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, StudentRepository studentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public EnrollmentDto createEnrollment(EnrollmentDto enrollmentDto) {

        if (enrollmentRepository.existsByStudentUuidAndCourseCode(enrollmentDto.getStudentUuid(), enrollmentDto.getCourseCode())) {
            throw new IllegalArgumentException("Already enrolled student");
        }

        Student student = studentRepository.findByUuid(enrollmentDto.getStudentUuid())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Course course = courseRepository.findByCode(enrollmentDto.getCourseCode())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Enrollment newEnrollment = new Enrollment(student, course, EnrollmentStatus.PENDING);

        return EnrollmentMapper.toDto(enrollmentRepository.save(newEnrollment));
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByStudent(UUID studentUuid) {

        if (!studentRepository.existsByUuid(studentUuid)) {
            throw new StudentNotFoundException("Student " + studentUuid + "not found");
        }

        return enrollmentRepository.findByStudentUuid(studentUuid)
                .stream()
                .map(EnrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByCourse(String courseCode) {

        if (!courseRepository.existsByCode(courseCode)) {
            throw new CourseNotFoundException("Course with code " + courseCode + " not found");
        }

        return enrollmentRepository.findByCourseCode(courseCode)
                .stream()
                .map(EnrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDto updateEnrollment(Long id, EnrollmentUpdateDto enrollmentUpdateDto) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment with id" + id + " not found"));

        enrollment.setStatus(enrollmentUpdateDto.getEnrollmentStatus());
        enrollment.setFinalGrade(enrollmentUpdateDto.getFinalGrade());

        return EnrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }
}