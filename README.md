# Plataforma de Pago Cashless

Proyecto académico desarrollado como proyecto final del ciclo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

La aplicación plantea un sistema de pagos *cashless* para eventos y festivales, con una arquitectura cliente-servidor formada por un backend REST y una aplicación Android. El objetivo es centralizar la gestión de usuarios, pulseras, saldos, recargas, pagos y transacciones.

## Tecnologías

### Backend
- Java 22
- Spring Boot 3
- Spring Data JPA
- Spring Security
- PostgreSQL
- Flyway
- JWT
- Swagger / OpenAPI
- Maven
- Docker

### Aplicación Android
- Kotlin
- Android
- Gradle

## Funcionalidades principales

- Gestión de usuarios.
- Asociación y gestión de pulseras.
- Consulta y actualización de saldo.
- Recargas.
- Registro de pagos.
- Gestión e historial de transacciones.
- Autenticación y autorización.
- Persistencia de datos en PostgreSQL.
- Documentación de la API mediante OpenAPI/Swagger.

## Arquitectura

```text
Aplicación Android (Kotlin)
          |
          | HTTP / REST
          v
Backend Spring Boot (Java)
          |
          | JPA
          v
PostgreSQL
```

El backend concentra la lógica de negocio y expone los endpoints REST consumidos por la aplicación Android. La persistencia se realiza en PostgreSQL mediante Spring Data JPA y las migraciones de base de datos se gestionan con Flyway.

## Estructura del repositorio

```text
Plataforma-Pago-Cashless/
├── backend/     # API REST desarrollada con Java y Spring Boot
└── frontend/    # Aplicación Android desarrollada en Kotlin
```

## Puesta en marcha del backend

El proyecto incluye configuración para Docker. Para levantar PostgreSQL desde la carpeta `backend`:

```bash
docker compose -f services.yml up -d psql-server
```

A continuación puede iniciarse el backend utilizando Maven Wrapper.

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

## Qué demuestra este proyecto

Este proyecto integra conceptos trabajados durante DAM en un caso de uso completo: diseño de una API REST, persistencia relacional, seguridad, autenticación mediante JWT, gestión de migraciones, contenerización y desarrollo de una aplicación Android cliente.

## Estado

Proyecto académico finalizado y conservado como parte de mi portfolio técnico.
