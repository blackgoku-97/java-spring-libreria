# **Proyecto de Librería** 📘

## 1. **Introducción** 📖

El proyecto consiste en la creación de una aplicación de Console que permita la gestión de libros.

## 2. **Requisitos** 📉

### 2.1. **Requisitos funcionales** 📈

* El sistema debe permitir la busqueda de libros por titúlo.
* El sistema debe permitir la consulta de libros registrados.
* El sistema debe permitir la consulta de autores registrados.
* El sistema debe permitir la busqueda del autor por un año determinado.
* El sistema debe permitir la consulta de libros por idioma.

### 2.2. **Requisitos Previos** ⚙️

Antes de proceder, asegurate de tener estos programas en el equipo:

* ☕ **JDK 21**
* 🖥️ **IntelliJ IDEA (Puede usar cualquier IDE de Java o editor de programación)
* 🌐 **Conexión a Internet** (Para obtener las tasas de cambio en tiempo real)

### 2.3. **Herramientas** 🛠️

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Boot DevTools (Opcional)
* Jackson
* PostgreSQL Driver
* PostgreSQL
* Maven
* GitHub
* Intellij IDEA
* Gutendex API

### 2.4. **Función de la Aplicación** 🚀

Para ejecutar el proyecto es necesario seguir estos pasos:

1. Crear variables de entorno

1. DB_HOST: Host de la base de datos.
2. DB_NAME: Nombre de la base de datos.
3. DB_USER: Nombre de usuario de la base de datos.
4. DB_PASSWORD: Contraseña de la base de datos.

## 3. 🔄 **Funcionamiento**

La aplicación solicita el nombre de un libro. Utiliza la API **Gutendex-API** para obtener los datos del libro, con la ayuda de **Jackson**, procesa la información en formato JSON. La libreria se realiza de manera eficiente, brindando resultados precisos y rápidos.

## 4. **Licencia** 📝

Este proyecto está licenciado bajo los términos de la [MIT License](LICENSE).
