package com.aluracursos.libreria.repository;

import com.aluracursos.libreria.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
    @Query("SELECT l FROM Libro l WHERE l.idiomas ILIKE %:idiomas%")
    List<Libro> findByIdiomas(String idiomas);
}
