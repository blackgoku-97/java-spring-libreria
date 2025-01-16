package com.aluracursos.libreria.presentation;

import com.aluracursos.libreria.model.clases.*;
import com.aluracursos.libreria.model.dto.Datos;
import com.aluracursos.libreria.repository.*;
import com.aluracursos.libreria.service.*;

import java.util.*;

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
        Map<Integer, Runnable> opciones = Map.of(
                1, this::buscarLibroPorTitulo,
                2, this::ConsultarLibros,
                3, this::ConsultarAutores,
                4, this::ConsultarAutorPorAnio,
                5, this::BuscarLibroPorIdioma,
                6, this::EstadisticasDeLibros,
                7, this::Top10Libros,
                8, this::buscarAutor,
                9, this::ConsultarAutorNacidoPorAnio,
                10, this::Salir
        );

        for (int opcion = -1; opcion != 10;) {
            System.out.print("""
                \n*** Aplicacion de Librería ***
                1. Buscar libro por titulo
                2. Listar libros registrados
                3. Listar autores registrados
                4. Listar autores vivos en un determinado año
                5. Listar libros por idioma
                6. Estadisticas de libros
                7. Top 10 libros mas descargados
                8. Buscar autor por nombre
                9. Listar autores nacidos en un rango de años
                10. Salir
                Elige una opcion:\s""");

            try {
                opcion = Integer.parseInt(consola.nextLine());
                opciones.getOrDefault(opcion, () -> System.out.println("Opcion no válida")).run();
            } catch (Exception e) {
                System.out.println("Ingrese una opcion valida");
            }
        }
    }

    // Obtener datos de un libro
    private Datos getDatosLibro() {
        System.out.println("Ingrese el nombre del libro a buscar");
        var nombreLibro = consola.nextLine().toLowerCase().replace(" ", "%20");
        return conversor.obtenerDatos(consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro), Datos.class);
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
            System.out.println("No se encontró el libro");
        }
    }

    // Libros registrados
    private void ConsultarLibros() {
        libros = libroRepositorio.findAll();
        libros.forEach(l -> System.out.printf("""
                    Titulo: %s
                    Autor: %s
                    Idioma: %s
                    Cantidad de descargas: %s
                ]
                """,
                l.getTitulo(),
                getNombreCompleto(l.getAutor()),
                l.getIdiomas(),
                l.getNumeroDeDescargas().toString()));
    }

    // Autores registrados
    private void ConsultarAutores() {
        autores = autorRepositorio.findAll();
        autores.forEach(a -> System.out.printf("""
                    Autor: %s
                    Fecha de Nacimiento: %s
                    Fecha de Fallecimiento: %s
                ]
                """,
                getNombreCompleto(a.getNombreAutor()),
                a.getFechaDeNacimiento(),
                a.getFechaDeFallecimiento().toString()));
    }

    // Autores vivos en un determinado año
    private void ConsultarAutorPorAnio() {
        System.out.println("Ingrese el año predeterminado del autor a buscar");
        var anioBusqueda = Integer.parseInt(consola.nextLine());

        List<Autor> autores = autorRepositorio.autorPorFecha(anioBusqueda);
        autores.forEach(a -> System.out.printf("""
            Autor: %s
            Fecha de Nacimiento: %s
            Fecha de Fallecimiento: %s
        ]
        """,
                getNombreCompleto(a.getNombreAutor()),
                a.getFechaDeNacimiento(),
                a.getFechaDeFallecimiento().toString()));
    }

    // Libros por idioma
    private void BuscarLibroPorIdioma() {
        String[] idiomas = {"es", "en", "fr", "de", "it"};
        System.out.println("Seleccione el idioma del libro:");
        for (int i = 0; i < idiomas.length; i++) {
            System.out.println((i + 1) + ". " + idiomas[i]);
        }
        System.out.print("Elige una opción: ");

        int opcion;
        try {
            opcion = Integer.parseInt(consola.nextLine());
            if (opcion < 1 || opcion > idiomas.length) {
                System.out.println("Opción no válida");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Opción no válida");
            return;
        }

        libros = libroRepositorio.findByIdiomas(idiomas[opcion - 1]);
        libros.forEach(l -> {
            System.out.printf("""
                Título: %s
                Autor: %s
                Idioma: %s
                Cantidad de descargas: %s
            """,
                    l.getTitulo(),
                    getNombreCompleto(l.getAutor()),
                    l.getIdiomas(),
                    l.getNumeroDeDescargas().toString());
            System.out.println();
        });
    }

    // Estadisticas de libros
    private void EstadisticasDeLibros() {
        Datos datosLibros = getDatosLibro();

        try {
            List<Libro> libros = datosLibros.resultados().stream()
                    .map(Libro::new)
                    .toList();

            DoubleSummaryStatistics estadisticas = libros.stream()
                    .mapToDouble(Libro::getNumeroDeDescargas)
                    .summaryStatistics();

            double mediana = obtenerMediana(libros.stream()
                    .mapToDouble(Libro::getNumeroDeDescargas)
                    .toArray());

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
        int indiceMediana = valores.length / 2;
        return quickSelect(valores, indiceMediana);
    }

    private double quickSelect(double[] valores, int k) {
        if (valores.length == 1) {
            return valores[0];
        }
        double[] menores = new double[k];
        double[] mayores = new double[valores.length - k - 1];
        int i = 0, j = 0;
        for (double valor : valores) {
            if (valor < valores[valores.length / 2]) {
                menores[i++] = valor;
            } else if (valor > valores[valores.length / 2]) {
                mayores[j++] = valor;
            }
        }
        if (k < i) {
            return quickSelect(menores, k);
        } else if (k > i) {
            return quickSelect(mayores, k - i - 1);
        } else {
            return valores[valores.length / 2];
        }
    }

    // Top 10 libros más descargados
    private void Top10Libros() {
        libros = libroRepositorio.findAll();
        libros.sort(Comparator.comparing(Libro::getNumeroDeDescargas).reversed());
        System.out.println("Top 10 libros más descargados:");
        libros.stream().limit(10).forEach(libro -> System.out.printf("%d. %s (%s) - %d descargas\n",
                libros.indexOf(libro) + 1,
                libro.getTitulo(),
                getNombreCompleto(libro.getAutor()),
                libro.getNumeroDeDescargas()));
    }

    public String getNombreCompleto(String nombreAutor) {
        String[] partes = nombreAutor.split(",");
        if (partes.length > 1) {
            return partes[1].trim() + " " + partes[0].trim();
        } else {
            return nombreAutor.trim();
        }
    }

    // Autores por nombre
    public Autor buscarAutorPorNombre(String nombre) {
        return autores.stream()
                .filter(a -> a.getNombreAutor().split(",")[1].trim().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public void buscarAutor() {
        ConsultarAutores();
        System.out.println("Ingrese el nombre del autor a buscar");
        var nombreAutor = consola.nextLine();
        Autor autor = buscarAutorPorNombre(nombreAutor);
        if (autor != null) {
            System.out.println("Autor encontrado: " + getNombreCompleto(autor.getNombreAutor()));
        } else {
            System.out.println("Lo siento, el autor no está en nuestra lista.");
        }
    }

    // Autores nacidos en un determinado año
    private void ConsultarAutorNacidoPorAnio() {
        System.out.println("Ingrese el año de inicio");
        int anioInicio = Integer.parseInt(consola.nextLine());
        System.out.println("Ingrese el año de fin");
        int anioFin = Integer.parseInt(consola.nextLine());
        List<Autor> autores = autorRepositorio.findByFechaNacimientoBetween(anioInicio, anioFin);
        System.out.println("Autores nacidos entre " + anioInicio + " y " + anioFin + ":");
        autores.forEach(autor -> System.out.println(getNombreCompleto(autor.getNombreAutor())));
    }

    // Saliendo del programa
    private void Salir() {
        System.out.println("Saliendo...");
    }
}
