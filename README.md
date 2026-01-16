# Gestión de Concesionario - Proyecto Académico

Proyecto académico desarrollado durante el ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).  
El objetivo principal es practicar la persistencia de datos en Java usando JPA/Hibernate y desarrollar lógica de negocio real en una aplicación de gestión de concesionarios de vehículos.

## Tecnologías

- Java 17+
- JPA/Hibernate
- JPQL
- Base de datos compatible con JPA (MySQL)
- Maven 

## Funcionalidades

- Inicializar la base de datos y cargar datos de ejemplo  
- Dar de alta concesionarios y coches  
- Añadir equipamientos a coches  
- Registrar reparaciones y calcular costes  
- Realizar ventas y asignarlas a propietarios  
- Listar coches de un concesionario  
- Mostrar reparaciones de un mecánico  
- Calcular ingresos totales de un concesionario

> Este proyecto es académico y no está pensado para uso comercial.

## Estructura
project
├─ src/
│   └─ main/
│       ├─ java/
│       │   ├─ app/
│       │   │   └─ Main.java
│       │   ├─ model/
│       │   │   ├─ Coche.java
│       │   │   ├─ Concesionario.java
│       │   │   ├─ Propietario.java
│       │   │   ├─ Reparacion.java
│       │   │   └─ Venta.java
│       │   ├─ service/
│       │   │   └─ GestionConcesionario.java
│       │   └─ utils/
│       │       └─ GeneradorInformacion.java
│       └─ resources/
│           └─ META-INF/
│               └─ persistence.xml
└─ docs/
    └─ modelo_dominio.png
