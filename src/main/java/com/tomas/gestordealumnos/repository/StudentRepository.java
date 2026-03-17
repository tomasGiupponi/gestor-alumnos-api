package com.tomas.gestordealumnos.repository;

import com.tomas.gestordealumnos.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUuid(UUID uuid);
    boolean existsByDni(String dni);
}
