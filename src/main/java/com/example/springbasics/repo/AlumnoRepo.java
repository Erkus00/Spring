package com.example.springbasics.repo;

import model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlumnoRepo extends JpaRepository<Alumno, Long> {
    @Query(value = "SELECT al FROM Alumno al WHERE al.ad<5")
    List<Alumno> findByAdSuspensos();

    @Query(value = "SELECT al FROM Alumno al WHERE al.id<5")
    List<Alumno> findByIdSuspensos();
}
