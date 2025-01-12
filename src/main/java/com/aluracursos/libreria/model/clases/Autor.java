package com.aluracursos.libreria.model.clases;

import com.aluracursos.libreria.model.dto.DatosAutor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "autores")
@Data
@NoArgsConstructor
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String nombreAutor;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;

    public Autor(DatosAutor datosAutor) {
        this.nombreAutor = datosAutor.nombreAutor();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
    }
}
