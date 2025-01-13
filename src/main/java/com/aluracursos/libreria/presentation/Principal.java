package com.aluracursos.libreria.presentation;

import com.aluracursos.libreria.model.clases.*;
import com.aluracursos.libreria.model.dto.Datos;
import com.aluracursos.libreria.repository.*;
import com.aluracursos.libreria.service.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner consola = new Scanner(System.in);
    private List<Libro> libros;
    private List<Autor> autores;
    private final LibroRepository libroRepositorio;
    private final AutorRepository autorRepositorio;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepositorio = libroRepository;
        this.autorRepositorio = autorRepository;
        this.autores = new ArrayList<>();
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 9) {
            var menu = """
                    \n*** Aplicacion de Libreria ***
                    1. Buscar libro por titulo
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un determinado año
                    5. Listar libros por idioma
                    6. Estadisticas de libros
                    7. Top 10 libros mas descargados
                    8. Buscar autor por nombre
                    9. Salir
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
                    EstadisticasDeLibros();
                    break;
                case 7:
                    Top10Libros();
                    break;
                case 8:
                    ConsultarAutores();
                    System.out.println("Ingrese el nombre del autor a buscar");
                    var nombreAutor = consola.nextLine();
                    Autor autor = buscarAutorPorNombre(nombreAutor);
                    if (autor != null) {
                        System.out.println("Autor encontrado: " + getNombreCompleto(autor.getNombreAutor()));
                    } else {
                        System.out.println("Lo siento, el autor no está en nuestra lista.");
                    }
                    break;
                case 9:
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
        return conversor.obtenerDatos(json, Datos.class);
    }

    // Busqueda de un libro por titulo
    private void buscarLibroPorTitulo() {
        Datos datosLibro = getDatosLibro();

        try {
            Libro libro = new Libro(datosLibro.resultados().getFirst());
            Autor autor = new Autor(datosLibro.resultados().getFirst().autor().getFirst());

            System.out.printf("""
                    Libro[
                        Titulo: %s
                        Autor: %s
                        Idioma: %s
                        Cantidad de descargas: %s
                    ]
                    """,
                    libro.getTitulo(),
                    getNombreCompleto(libro.getAutor()),
                    libro.getIdiomas(),
                    libro.getNumeroDeDescargas().toString());

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
            System.out.printf("""
                        Titulo: %s
                        Autor: %s
                        Idioma: %s
                        Cantidad de descargas: %s
                    ]
                    """,
                    l.getTitulo(),
                    getNombreCompleto(l.getAutor()),
                    l.getIdiomas(),
                    l.getNumeroDeDescargas().toString());
        });
    }

    // Autores registrados
    private void ConsultarAutores() {
        autores = autorRepositorio.findAll();
        autores.stream().forEach(a -> {
            System.out.printf("""
                        Autor: %s
                        Fecha de Nacimiento: %s
                        Fecha de Fallecimiento: %s
                    ]
                    """,
                    getNombreCompleto(a.getNombreAutor()),
                    a.getFechaDeNacimiento(),
                    a.getFechaDeFallecimiento().toString());
        });
    }

    // Autores vivos en un determinado año
    private void ConsultarAutorPorAnio() {
        System.out.println("Ingrese el año aproximado del autor a buscar");
        var anioBusqueda = Integer.parseInt(consola.nextLine());

        List<Autor> autores = autorRepositorio.autorPorFecha(anioBusqueda);
        autores.stream().forEach(a -> {
            String nombreCompleto = getNombreCompleto(a.getNombreAutor());
            System.out.printf("""
                Autor: %s
                Fecha de Nacimiento: %s
                Fecha de Fallecimiento: %s
            ]
            """,
                    nombreCompleto,
                    a.getFechaDeNacimiento().toString(),
                    a.getFechaDeFallecimiento().toString());
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
                System.out.printf("""
                        Titulo: %s
                        Autor: %s
                        Idioma: %s
                        Cantidad de descargas: %s
                    ]
                    """,
                        l.getTitulo(),
                        getNombreCompleto(l.getAutor()),
                        l.getIdiomas(),
                        l.getNumeroDeDescargas().toString());
            });

        } catch (Exception e) {
            System.out.println("Opcion no valida");
        }
    }

    // Estadisticas de libros
    private void EstadisticasDeLibros() {
        Datos datosLibros = getDatosLibro();

        try {
            List<Libro> libros = datosLibros.resultados().stream()
                    .map(Libro::new)
                    .toList();

            double[] descargas = libros.stream()
                    .mapToDouble(Libro::getNumeroDeDescargas)
                    .toArray();

            DoubleSummaryStatistics estadisticas = libros.stream()
                    .mapToDouble(Libro::getNumeroDeDescargas)
                    .summaryStatistics();

            double mediana = obtenerMediana(descargas);

            System.out.printf("""
                Estadísticas:
                    Media de descargas: %.2f
                    Mediana de descargas: %.2f
                    Máximo de descargas: %.2f
                    Mínimo de descargas: %.2f
                    Número de libros: %d
                """,
                    estadisticas.getAverage(),
                    mediana,
                    estadisticas.getMax(),
                    estadisticas.getMin(),
                    libros.size());

        } catch (Exception e) {
            System.out.println("No se encontraron libros");
        }
    }

    private double obtenerMediana(double[] valores) {
        Arrays.sort(valores);
        int longitud = valores.length;
        if (longitud % 2 == 1) {
            return valores[longitud / 2];
        } else {
            return (valores[longitud / 2 - 1] + valores[longitud / 2]) / 2;
        }
    }

    // Top 10 libros mas descargados
    private void Top10Libros() {
        libros = libroRepositorio.findAll();
        libros.sort(Comparator.comparing(Libro::getNumeroDeDescargas).reversed());
        System.out.println("Top 10 libros más descargados:");
        libros.stream().limit(10).forEach(libro -> {
            System.out.printf("%d. %s (%s) - %d descargas\n",
                    libros.indexOf(libro) + 1,
                    libro.getTitulo(),
                    getNombreCompleto(libro.getAutor()),
                    libro.getNumeroDeDescargas());
        });
    }

    public String getNombreCompleto(String nombreAutor) {
        String[] partes = nombreAutor.split(",");
        if (partes.length > 1) {
            return partes[1].trim() + " " + partes[0].trim();
        } else {
            return nombreAutor.trim();
        }
    }

    public Autor buscarAutorPorNombre(String nombre) {
        return autores.stream()
                .filter(a -> a.getNombreAutor().toLowerCase().split(",")[1].trim().toLowerCase().equals(nombre.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}
