package com.aluracursos.libreria.model.dto;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String nombreAutor,
        @JsonAlias("birth_year") Integer fechaDeNacimiento,
        @JsonAlias("death_year") Integer fechaDeFallecimiento
) {
}
