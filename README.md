# **Proyecto de Librería** 📘

## 1. **Introducción** 📖

El proyecto consiste en la creación de una aplicación de consola que permita la gestión de libros y autores.

## 2. **Requisitos** 📉

### 2.1. **Requisitos funcionales** 📈

* El sistema debe permitir la busqueda de libros por titúlo.
* El sistema debe permitir la consulta de libros registrados.
* El sistema debe permitir la consulta de autores registrados.
* El sistema debe permitir la busqueda de autores vivos por un año determinado.
* El sistema debe permitir la consulta de libros por idioma.
* El sistema debe permitir las estadísticas de libros (se demora pero funciona).
* El sistema debe permitir la busqueda de los 10 libros mas descargados (de la base de datos).
* El sistema debe permitir la busqueda de autores vivos por un rango de años.
* El sistema debe permitir la busqueda de un autor por nombre.

### 2.2. **Requisitos Previos** ⚙️

Antes de proceder, asegurate de tener estos programas en el equipo:

* ☕ **JDK 21**
* 🖥️ **IntelliJ IDEA (Puede usar cualquier IDE de Java o editor de programación)
* 🌐 **Conexión a Internet** (Para obtener los datos de los libros y autores en tiempo real)

### 2.3. **Herramientas** 🛠️

* Java: Lenguaje de programación utilizado para el desarrollo.
* Spring Boot: Framework de Java para el desarrollo.
* Spring Data JPA: Dependencia de Spring para la integración con bases de datos.
* Spring Boot DevTools: Herramienta de desarrollo (Opcional)
* Jackson: Librería para la serialización y deserialización de objetos JSON.
* PostgreSQL Driver: Driver de PostgreSQL.
* PostgreSQL: Sistema de gestión de bases de datos.
* Maven: Gestor de dependencias.
* Lombok: Librería para el uso de anotaciones de preprocesamiento.
* GitHub: Sistema de control de versiones.
* Intellij IDEA: IDE de programación.
* Gutendex API: API de la biblioteca Gutendex.

### 2.4. **Función de la Aplicación** 🚀

Para ejecutar el proyecto es necesario seguir estos pasos:

1. Crear variables de entorno

* DB_HOST: Host de la base de datos.
* DB_PORT: Puerto de la base de datos.
* DB_NAME: Nombre de la base de datos.
* DB_USER: Nombre de usuario de la base de datos.
* DB_PASSWORD: Contraseña de la base de datos.

2. Verifica la configuración y ejecuta el proyecto:

* Asegúrate de que todas las configuraciones estén correctas.

* Ejecuta el proyecto desde tu IDE.


## 3. 🔄 **Funcionamiento**

La aplicación solicita el nombre de un libro. Utiliza la API **Gutendex-API** para obtener los datos del libro, con la ayuda de **Jackson**, procesa la información en formato JSON. La libreria se realiza de manera eficiente, brindando resultados precisos y rápidos.

## 4. **Licencia** 📝

Este proyecto está licenciado bajo los términos de la [MIT License](LICENSE).
