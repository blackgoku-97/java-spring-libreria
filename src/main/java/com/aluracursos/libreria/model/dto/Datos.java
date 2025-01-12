package com.aluracursos.libreria.model.dto;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(
        @JsonAlias("results") List<DatosLibros> resultados
) {
}
