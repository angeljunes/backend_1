<<<<<<< HEAD
# RCAS Backend - Sistema de Registro y Control de Alertas de Seguridad

## Descripción
Backend desarrollado en Java Spring Boot para el sistema RCAS que permite gestionar alertas de seguridad con geolocalización.

## Tecnologías
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- Spring Security
- MySQL 8.0
- Maven

## Configuración

### Base de Datos
1. Crear la base de datos MySQL ejecutando `database_clean.sql`
2. Configurar las credenciales en `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rcas_simple
spring.datasource.username=root
spring.datasource.password=tu_password
```

### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/api`

## Endpoints Principales

### Autenticación
- `POST /api/auth/register` - Registrar usuario
- `POST /api/auth/login` - Iniciar sesión

### Ubicaciones
- `GET /api/location/countries` - Obtener países
- `GET /api/location/cities/{countryId}` - Obtener ciudades por país

### Alertas
- `GET /api/alerts` - Obtener todas las alertas
- `POST /api/alerts` - Crear nueva alerta
- `GET /api/alerts/filter?priority=alta&status=pendiente` - Filtrar alertas
- `PUT /api/alerts/{id}/status` - Actualizar estado de alerta

### Pruebas
- `GET /api/test/health` - Verificar estado del servidor
- `GET /api/test/ping` - Ping básico

## Estructura del Proyecto
```
src/main/java/com/rcas/
├── config/          # Configuraciones (Security, CORS)
├── controller/      # Controladores REST
├── dto/            # Data Transfer Objects
├── entity/         # Entidades JPA
├── repository/     # Repositorios JPA
├── service/        # Lógica de negocio
└── RcasBackendApplication.java
```

## Datos de Prueba
- Usuario: `test@rcas.com` / `testuser` / `password`
- Países: Perú, Colombia, Ecuador, etc.
- Ciudades: Lima, Arequipa, Bogotá, etc.

## Conexión con Frontend
El backend está configurado para aceptar peticiones CORS desde cualquier origen. 
Para conectar con tu frontend HTML/JavaScript, usa las URLs:
- `http://localhost:8080/api/location/countries`
- `http://localhost:8080/api/alerts`
- etc.
=======
# backend_1
>>>>>>> 68c847f2bdfdf04193c624da42efd85279310624
