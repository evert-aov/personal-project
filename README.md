# Schedule Assignment System

## Descripción
Este proyecto es una plataforma para la gestión de asignaciones de horarios académicos, finanzas personales y gestión de notas. Está construido con Spring Boot y utiliza PostgreSQL para el almacenamiento de datos.

## Características principales
- **Gestión Académica**: Control de periodos académicos, aulas, horarios, materias y grupos.
- **Finanzas**: Registro de transacciones y días de trabajo.
- **Notas y Pizarras**: Gestión de notas personales y pizarras virtuales.
- **Seguridad**: Autenticación y autorización mediante JWT (JSON Web Tokens).

## Tecnologías utilizadas
- **Java 25**
- **Spring Boot 4.0.1**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **PostgreSQL** (Flyway para migraciones)
- **Lombok**
- **SpringDoc OpenAPI** (Swagger UI)

## Requisitos
- JDK 25 o superior
- Docker y Docker Compose (opcional, para la base de datos)
- PostgreSQL

## Configuración del entorno
1. Clonar el repositorio.
2. Configurar la base de datos en `src/main/resources/application.properties`.
3. Ejecutar el proyecto con `./gradlew bootRun`.

## Documentación de la API
Una vez iniciada la aplicación, la documentación de la API está disponible en:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Estructura del Proyecto
- `com.uagrm.personal.academic_catalog`: Gestión académica.
- `com.uagrm.personal.finance`: Gestión financiera.
- `com.uagrm.personal.note`: Notas y pizarras.
- `com.uagrm.personal.security`: Seguridad, usuarios y roles.
- `com.uagrm.personal.config`: Configuraciones generales.

## Licencia
Propiedad de la UAGRM - Proyecto Personal.
