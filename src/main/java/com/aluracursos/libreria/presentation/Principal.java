package com.aluracursos.libreria.presentation;

import com.aluracursos.libreria.model.Autor;
import com.aluracursos.libreria.model.Datos;
import com.aluracursos.libreria.model.Libro;
import com.aluracursos.libreria.repository.AutorRepository;
import com.aluracursos.libreria.repository.LibroRepository;
import com.aluracursos.libreria.service.ConsumoAPI;
import com.aluracursos.libreria.service.ConvierteDatos;

import java.util.List;
import java.util.Scanner;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner consola = new Scanner(System.in);
    private List<Libro> libros;
    private List<Autor> autores;
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepositorio = libroRepository;
        this.autorRepositorio = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 6) {
            var menu = """
                    \n*** Aplicacion de Libreria ***
                    1. Buscar libro por titulo
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un determinado año
                    5. Listar libros por idioma
                    6. Salir
                    Elige una opcion:\s""";

            try {
                System.out.print(menu);
                opcion = Integer.parseInt(consola.nextLine());

            } catch (Exception e) {
                System.out.println("Ingrese una opcion valida");
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    ConsultarLibros();
                    break;
                case 3:
                    ConsultarAutores();
                    break;
                case 4:
                    ConsultarAutorPorAnio();
                    break;
                case 5:
                    BuscarLibroPorIdioma();
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }
    }

    // Obtener datos de un libro
    private Datos getDatosLibro() {
        System.out.println("Ingrese el nombre del libro a buscar");
        var nombreLibro = consola.nextLine().toLowerCase().replace(" ", "%20");
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro);
        Datos datosLibro = conversor.obtenerDatos(json, Datos.class);
        return datosLibro;
    }

    // Busqueda de un libro por titulo
    private void buscarLibroPorTitulo() {
        Datos datosLibro = getDatosLibro();

        try {
            Libro libro = new Libro(datosLibro.resultados().get(0));
            Autor autor = new Autor(datosLibro.resultados().get(0).autor().get(0));

            System.out.println("""
                    Libro[
                        Titulo: %s
                        Autor: %s
                        Idioma: %s
                        Cantidad de descargas: %s
                    ]
                    """.formatted(libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIdiomas(),
                    libro.getNumeroDeDescargas().toString()));

            libroRepositorio.save(libro);
            autorRepositorio.save(autor);

        } catch (Exception e) {
            System.out.println("No se encontro el libro");
        }
    }

    // Libros registrados
    private void ConsultarLibros() {
        libros = libroRepositorio.findAll();
        libros.stream().forEach(l -> {
            System.out.println("""
                        Titulo: %s
                        Autor: %s
                        Idioma: %s
                        Cantidad de descargas: %s
                    ]
                    """.formatted(l.getTitulo(),
                    l.getAutor(),
                    l.getIdiomas(),
                    l.getNumeroDeDescargas().toString()));
        });
    }

    // Autores registrados
    private void ConsultarAutores() {
        autores = autorRepositorio.findAll();
        autores.stream().forEach(a -> {
            System.out.println("""
                        Autor: %s
                        Fecha de Nacimiento: %s
                        Fecha de Fallecimiento: %s
                    ]
                    """.formatted(a.getAutor(),
                    a.getFechaDeNacimiento(),
                    a.getFechaDeFallecimiento().toString()));
        });
    }

    // Autores vivos en un determinado año
    private void ConsultarAutorPorAnio() {
        System.out.println("Ingrese el año aproximado del autor a buscar");
        var anioBusqueda = Integer.parseInt(consola.nextLine());

        List<Autor> autores = autorRepositorio.autorPorFecha(anioBusqueda);
        autores.stream().forEach(a -> {
            System.out.println("""
                        Autor: %s
                        Fecha de Nacimiento: %s
                        Fecha de Fallecimiento: %s
                    ]
                    """.formatted(a.getAutor(),
                    a.getFechaDeNacimiento().toString(),
                    a.getFechaDeFallecimiento().toString()));
        });
    }

    // Libros por idioma
    private void BuscarLibroPorIdioma() {
        System.out.println("""
                Seleccione el idioma del libro
                1. es - español
                2. en - ingles
                3. fr - frances
                4. de - aleman
                5. it - Italiano
                Elige una opcion:\s""");

        try {
            var opcion2 = Integer.parseInt(consola.nextLine());

            switch (opcion2) {
                case 1:
                    libros = libroRepositorio.findByIdiomas("es");
                    break;
                case 2:
                    libros = libroRepositorio.findByIdiomas("en");
                    break;
                case 3:
                    libros = libroRepositorio.findByIdiomas("fr");
                    break;
                case 4:
                    libros = libroRepositorio.findByIdiomas("de");
                    break;
                case 5:
                    libros = libroRepositorio.findByIdiomas("it");
                    break;
                default:
                    System.out.println("Introduzca una opcion valida");
            }

            libros.stream().forEach(l -> {
                System.out.println("""
                        Titulo: %s
                        Autor: %s
                        Idioma: %s
                        Cantidad de descargas: %s
                    ]
                    """.formatted(l.getTitulo(),
                            l.getAutor(),
                            l.getIdiomas(),
                            l.getNumeroDeDescargas().toString()));
            });

        } catch (Exception e) {
            System.out.println("Opcion no valida");
        }
    }
}
