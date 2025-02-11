# Proyecto TUP

Este proyecto es una aplicación desarrollada en Java utilizando el framework Spring Boot. Está estructurado en varias capas (Model, Persistence, Business y Controller) siguiendo el patrón de arquitectura en capas, y utiliza técnicas de Inyección de Dependencias, Mapeo de Objetos (DTOs) y pruebas unitarias con Mockito y JUnit.

## Tabla de Contenidos

- [Descripción](#descripción)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Configuración y Ejecución](#configuración-y-ejecución)
- [Endpoints HTTP para REST Client](#endpoints-http-para-rest-client)
- [Pruebas](#pruebas)
- [Diagramas UML](#diagramas-uml)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Contacto](#contacto)

## Descripción

El proyecto simula un sistema académico con entidades como **Alumno**, **Asignatura**, **Materia** y **Profesor**. Cada capa del proyecto está separada para facilitar la mantenibilidad y escalabilidad:

- **Model:** Contiene las clases de dominio, incluyendo las entidades principales y sus relaciones, además de enumeraciones y DTOs para el intercambio de información.
- **Persistence:** Implementa el acceso a datos mediante DAOs en memoria (por ejemplo, `AlumnoDaoMemoryImpl`, `AsignaturaDaoMemoryImpl`, `MateriaDaoMemoryImpl`, `ProfesorDaoMemoryImpl`).
- **Business:** Contiene la lógica de negocio en los servicios (por ejemplo, `AlumnoServiceImpl`, `AsignaturaServiceImpl`, `MateriaServiceImpl`, `ProfesorServiceImpl`).
- **Controller:** Expone endpoints REST para la interacción con el sistema (por ejemplo, `AlumnoController`, `MateriaController` y `ProfesorController`).

## Requisitos

- **Java 17** (o la versión especificada en el `pom.xml`)
- **Maven** (para la gestión de dependencias y la compilación)
- **Spring Boot**

## Configuración y Ejecución

Clonar el repositorio:

   git clone <URL_DEL_REPOSITORIO>
   cd <NOMBRE_DEL_REPOSITORIO>

    
Construir el proyecto:

    mvn clean install

Ejecutar la aplicación:

    mvn spring-boot:run


## Endpoints HTTP para REST Client

La aplicación se ejecutará (por defecto) en http://localhost:8080.
Se ha incluido un archivo llamado **endpoints.http** en la raíz del proyecto. Con el plugin [REST Client para VS Code](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) instalado, puedes abrir este archivo y enviar las peticiones directamente desde el editor. 

Cada bloque de petición tiene un encabezado descriptivo, por ejemplo:

```http
### Obtener todos los alumnos
GET http://localhost:8080/alumno
Accept: application/json

```

Simplemente haz clic en el enlace "Send Request" que aparece encima del bloque para ejecutar la petición y ver la respuesta.

## Pruebas

El proyecto cuenta con pruebas unitarias que cubren cada capa (model, persistence, business y controller). Para ejecutar las pruebas, utiliza:

    mvn test

Además, se ha configurado JaCoCo para generar reportes de cobertura de código.

## Diagramas UML

Se incluyen diagramas UML que documentan la arquitectura del proyecto.

    Diagrama de la capa Model.
    Diagrama de la capa Persistence.
    Diagrama de la capa Business.
    Diagrama de la capa Controller.
    Diagrama global del proyecto.

## Tecnologías Utilizadas

    Java 17
    Spring Boot
    Maven
    JUnit 5 y Mockito para pruebas unitarias

## Contacto

    Nombre: Luciano Cossia
    Email: cossialuciano@gmail.com
