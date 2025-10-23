# Microservicio de Registro de Clientes

## 1. Descripción General

Este proyecto es un microservicio RESTful para la gestión de clientes, construido con Java 21 y Quarkus. Sigue los principios de **Domain-Driven Design (DDD)** para garantizar un código limpio, escalable y mantenible. La API está securizada mediante JWT y documentada con OpenAPI.

### 1.1. Stack Tecnológico Principal

- **Lenguaje**: Java 21
- **Framework**: Quarkus 3.11.2
- **Persistencia**: Hibernate ORM con Panache (Estrategia de Repositorio)
- **Base de Datos (Desarrollo/Pruebas)**: H2 en memoria
- **Seguridad**: MicroProfile JWT (quarkus-smallrye-jwt)
- **Documentación**: OpenAPI (quarkus-smallrye-openapi)
- **Mapeo**: MapStruct
- **Build Tool**: Maven

---

## 2. Arquitectura del Proyecto (Domain-Driven Design)

El proyecto está estructurado en tres capas principales, siguiendo las mejores prácticas de DDD para separar responsabilidades y aislar la lógica de negocio de los detalles de infraestructura.

```
+-----------------------------------------------------------------+
| Capa de Infraestructura (El "CÓMO" técnico)                     |
|-----------------------------------------------------------------|
| - ClientResource (Endpoints REST)                               |
| - ClientRepository (Implementación Panache)                     |
| - GlobalExceptionHandler                                        |
| - Configuración de BD, JWT, etc.                                |
+-----------------------------------------------------------------+
      ^ (depende de)
+-----------------------------------------------------------------+
| Capa de Aplicación (El "QUÉ" hace la aplicación)               |
|-----------------------------------------------------------------|
| - ClientApplicationService (Casos de Uso)                       |
| - CreateClientRequest / ClientResponse (DTOs)                   |
| - ClientMapper (Interfaz MapStruct)                             |
+-----------------------------------------------------------------+
      ^ (depende de)
+-----------------------------------------------------------------+
| Capa de Dominio (El "PORQUÉ" del negocio)                       |
|-----------------------------------------------------------------|
| - Client, Country, Status (Entidades)                           |
| - ClientRepository (Interfaz)                                   |
| - CountryValidationStrategy (Servicio de Dominio)               |
+-----------------------------------------------------------------+
```

### 2.1. Capa de Dominio (`domain`)

Es el corazón de la aplicación. Contiene toda la lógica y las reglas de negocio, sin depender de ninguna tecnología externa.

- **Modelos (`domain.model`)**: Entidades JPA como `Client`, `Country`, `Gender` y `Status`. Representan los conceptos de negocio.
- **Repositorios (`domain.repository`)**: Interfaces (ej. `ClientRepository`) que definen los contratos para la persistencia, sin conocer la implementación (Panache, en nuestro caso).
- **Servicios de Dominio (`domain.service`)**: Contiene la lógica de negocio compleja que no encaja en una sola entidad, como el Patrón Strategy para la validación.

### 2.2. Capa de Aplicación (`application`)

Orquesta los casos de uso del sistema. Actúa como un intermediario entre la infraestructura y el dominio.

- **Servicios de Aplicación (`application.service`)**: Clases como `ClientApplicationService` que exponen las funcionalidades principales (ej. `createClient`, `updateClient`). No contienen lógica de negocio, sino que coordinan las llamadas a los repositorios y servicios de dominio.
- **DTOs (`application.dto`)**: Objetos de Transferencia de Datos (implementados como Java Records) para las solicitudes (`CreateClientRequest`) y respuestas (`ClientResponse`) de la API. Aquí se aplican las validaciones de entrada.
- **Mappers (`application.mapper`)**: Interfaces de MapStruct (`ClientMapper`) para convertir DTOs a Entidades y viceversa, evitando la exposición de las entidades del dominio en la API.

### 2.3. Capa de Infraestructura (`infrastructure`)

Contiene todo lo relacionado con tecnologías externas: la API web, la implementación de la base de datos, etc.

- **Recursos (`infrastructure.resource`)**: Endpoints REST JAX-RS (`ClientResource`) que manejan las peticiones HTTP, la autenticación y la serialización JSON.
- **Manejo de Excepciones (`infrastructure.exception`)**: Mappers de excepciones (`GlobalExceptionHandler`) que traducen los errores de negocio en respuestas HTTP apropiadas (4xx, 5xx).

---

## 3. Lógica de Negocio: Patrón Strategy para Validación

Para cumplir con el requisito de que la validación del campo `numCTA` varíe según el país, se implementó el Patrón Strategy.

1.  **Interfaz `CountryValidationStrategy`**: Define el contrato con un único método `validate(String numCTA, String countryCode)`.
2.  **Implementación para Chile (`ChileValidationStrategy`)**: Valida que el `numCTA` para clientes de Chile comience con `"003"`. Si no, lanza una `InvalidAccountException` (resultando en un `400 Bad Request`).
3.  **Implementación por Defecto (`DefaultValidationStrategy`)**: Se aplica a todos los demás países y no realiza ninguna validación específica.
4.  **Selector (`ClientValidationService`)**: Este servicio de dominio inyecta todas las implementaciones de la estrategia y, basándose en el código del país del cliente, selecciona y ejecuta la estrategia correcta.

Este patrón permite añadir nuevas reglas de validación para otros países de forma limpia y desacoplada, simplemente creando una nueva clase que implemente la interfaz, sin modificar el código existente.

---

## 4. Seguridad y Documentación

### 4.1. Generación de Claves y Tokens JWT

La API está securizada con JWT usando el algoritmo `RS256` (firma asimétrica).

**Paso 1: Generar Claves (si no existen)**

Las claves se generan con `openssl` y se almacenan en `src/main/resources/tokens/`.

```bash
# Navegar a la carpeta de tokens
cd src/main/resources/tokens/

# Generar nueva clave privada
openssl genpkey -algorithm RSA -out private-key.pem -pkeyopt rsa_keygen_bits:2048

# Generar la clave pública correspondiente
openssl rsa -pubout -in private-key.pem -out public-key.pem
```

**Paso 2: Generar Tokens para Pruebas**

El proyecto incluye una clase de prueba para generar tokens válidos con los roles `user` y `admin`. Para generar nuevos tokens, ejecuta el siguiente comando Maven desde la raíz del proyecto:

```bash
# Ejecuta la prueba que genera los tokens
mvn test -Dtest=TokenGenerationTest -DskipTests=false
```

La salida en la consola mostrará los tokens listos para ser copiados y usados en Postman o `curl`. Estos tokens tienen una validez de 1 hora.

### 4.2. Documentación OpenAPI y Postman

La API está completamente documentada usando OpenAPI. Quarkus genera la especificación automáticamente.

**Cómo importar la colección en Postman:**

1.  Con la aplicación corriendo, abre Postman.
2.  Haz clic en el botón **Import**.
3.  Selecciona la pestaña **Link**.
4.  Pega la siguiente URL en el campo de texto:
    ```
    http://localhost:8080/q/openapi
    ```
5.  Haz clic en **Continue** y luego en **Import**. Postman creará automáticamente una nueva colección con todos los endpoints, sus parámetros y descripciones.

Para probar los endpoints, recuerda añadir el token JWT en la pestaña **Authorization** de cada petición (Type: `Bearer Token`).

---

## 5. Cómo Ejecutar el Proyecto

### Prerrequisitos

- JDK 21
- Apache Maven 3.8+

### Ejecución en Modo Desarrollo

Para iniciar la aplicación con Live Reloading, ejecuta:

```bash
mvn quarkus:dev
```

La aplicación estará disponible en `http://localhost:8080`.

### Ejecución de Pruebas

Para ejecutar todas las pruebas de integración y unitarias, usa:

```bash
mvn test
```
