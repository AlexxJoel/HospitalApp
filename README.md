# 🏥 Hospital API

Este proyecto proporciona una API RESTful para la gestión de citas médicas y otros recursos relacionados con un hospital.

## 📁 Estructura del proyecto

El proyecto está estructurado de la siguiente forma:

```
src/main/java/com/example/demo/ 
│ 
├── config # Configuraciones generales (Swagger, carga de datos iniciales, etc.) 
├── controller # Controladores REST que exponen los endpoints de la API 
│ 
└── dtos # Objetos de transferencia de datos (DTOs) 
├── model # Entidades del dominio \
├── repositories # Repositorios que interactúan con la base de datos 
├── services # Lógica de negocio 
└── HospitalApiApplication.java # Clase principal de Spring Boot
```

## 📦 Base de datos
La base de datos utilizada es PostgreSQL. Asegúrate de tenerla instalada y configurada correctamente.
Puedes crear la base de datos ejecutando el siguiente comando SQL:

```sql 
CREATE DATABASE test;
```
## 📦 Carga de datos iniciales
Al correr springboot se cargan datos iniciales en la base de datos. Con commandLiner 
ejecutar una vez y parar para posterior comentarlos 



## 🚀 Cómo ejecutar el proyecto

Asegúrate de tener instalado:

- Java 21
- Maven

La compilación del proyecto en desarrollo fue a traves del IDE IntelliJ:
## 📘 Documentación de la API
Una vez iniciado el proyecto, puedes acceder a la documentación de la API generada con Swagger en la siguiente URL:

```
http://localhost:8080/swagger-ui/index.html
```
Allí podrás explorar y probar todos los endpoints disponibles de manera interactiva.

## 🛠 Tecnologías utilizadas
Java 21

Spring Boot

Spring Data JPA

Swagger / OpenAPI

PostgreSQL

## 🧑‍💻 Autor
Proyecto desarrollado por AlexxJoel.