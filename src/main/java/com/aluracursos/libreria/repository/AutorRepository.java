package com.aluracursos.libreria.repository;

import com.aluracursos.libreria.model.clases.Autor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Integer> {
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento >= :anoBusqueda ORDER BY a.fechaDeNacimiento ASC")
    List<Autor> autorPorFecha(int anoBusqueda);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento BETWEEN :anioInicio AND :anioFin")
    List<Autor> findByFechaNacimientoBetween(@Param("anioInicio") Integer anioInicio, @Param("anioFin") Integer anioFin);
}
