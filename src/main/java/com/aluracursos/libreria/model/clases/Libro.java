package com.aluracursos.libreria.model.clases;

import com.aluracursos.libreria.model.dto.DatosLibros;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    @Column(unique = true)
    private String autor;
    private String idiomas;
    private Integer numeroDeDescargas;

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autor = datosLibros.autor().getFirst().nombreAutor();
        this.idiomas = datosLibros.idiomas().getFirst();
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }
}
