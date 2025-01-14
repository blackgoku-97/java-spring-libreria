package com.aluracursos.libreria.repository;

import com.aluracursos.libreria.model.clases.Autor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Integer> {
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :ano AND a.fechaDeFallecimiento >= :ano OR a.fechaDeFallecimiento IS NULL")
    List<Autor> autorPorFecha(int ano);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento BETWEEN :anioInicio AND :anioFin")
    List<Autor> findByFechaNacimientoBetween(@Param("anioInicio") Integer anioInicio, @Param("anioFin") Integer anioFin);
}
