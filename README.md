# Booking System API

API REST para gestión de reservas de citas desarrollada con **arquitectura hexagonal** (puertos y adaptadores) usando **Java 21** y **Spring Boot 3**.

## 📋 Descripción

Sistema de reservas diseñado para consultorios médicos, coworking spaces, barberías o cualquier negocio que requiera gestión de citas. Implementa validaciones de negocio robustas, autenticación JWT y documentación completa con Swagger.

## 🛠️ Stack Tecnológico

- **Java 21**
- **Spring Boot 3.2.0**
- **Maven** (gestión de dependencias)
- **H2 Database** (base de datos en memoria)
- **JWT** (autenticación y autorización)
- **Swagger/OpenAPI 3** (documentación de API)
- **JUnit 5** (testing)
- **Arquitectura Hexagonal** (puertos y adaptadores)

## 🏗️ Arquitectura

Estructura del proyecto basada en **arquitectura hexagonal**:

```
src/main/java/com/bookingsystem/
├── domain/                 # Capa de dominio (lógica de negocio)
│   ├── model/             # Entidades del dominio
│   └── port/              # Interfaces (puertos)
├── application/           # Capa de aplicación
│   ├── usecase/          # Casos de uso
│   └── service/          # Servicios de aplicación
├── infrastructure/       # Capa de infraestructura
│   ├── adapter/
│   │   ├── persistence/  # Adaptadores de persistencia (JPA)
│   │   └── web/          # Adaptadores web (Controllers, DTOs)
│   └── security/         # Configuración de seguridad
└── configuration/        # Configuración Spring (beans, Swagger)
```

## 🚀 Cómo Ejecutar

### Prerrequisitos
- Java 21
- Maven 3.6+

### Ejecución con Maven
```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/booking-system-api.git
cd booking-system-api

# Ejecutar la aplicación
mvn spring-boot:run
```

### Ejecución con IntelliJ IDEA
1. Importar el proyecto como proyecto Maven
2. Ejecutar la clase `BookingSystemApiApplication`

La aplicación estará disponible en: `http://localhost:8080`

## 📚 Documentación API

### Swagger UI
- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/v3/api-docs`

### Base de Datos H2
- **Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:bookingdb`
- **Username**: `sa`
- **Password**: `password`

## 🔐 Autenticación

### Credenciales de Prueba

| Usuario | Contraseña | Rol   |
|---------|------------|-------|
| admin   | admin123   | ADMIN |
| user    | user123    | USER  |
| juan    | juan123    | USER  |

### Obtener Token JWT

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "ADMIN"
}
```

## 📋 Endpoints Principales

### Autenticación
- `POST /api/auth/login` - Iniciar sesión

### Reservas
- `POST /api/reservations` - Crear nueva reserva
- `GET /api/reservations` - Listar todas las reservas
- `GET /api/reservations/my` - Listar mis reservas
- `DELETE /api/reservations/{id}` - Cancelar reserva

### Ejemplos de Uso

#### 1. Crear Reserva
```bash
curl -X POST http://localhost:8080/api/reservations \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Carlos Mendez",
    "customerEmail": "carlos@email.com",
    "startTime": "2025-07-20T10:00:00",
    "endTime": "2025-07-20T11:00:00",
    "description": "Consulta médica"
  }'
```

#### 2. Listar Reservas
```bash
curl -X GET http://localhost:8080/api/reservations \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📏 Reglas de Negocio

### Validaciones Implementadas
- ✅ **Horario de negocio**: Reservas solo de 08:00 a 18:00
- ✅ **Días laborales**: Solo lunes a viernes
- ✅ **No solapamiento**: Una reserva por franja horaria
- ✅ **Límite diario**: Máximo 3 reservas por usuario por día
- ✅ **Fechas futuras**: No se permiten reservas en el pasado
- ✅ **Autorización**: Los usuarios solo pueden cancelar sus propias reservas

## 🧪 Testing

Ejecutar tests unitarios:
```bash
mvn test
```

### Cobertura de Tests
- ✅ Tests de dominio (entidades y reglas de negocio)
- ✅ Tests de casos de uso
- ✅ Tests de validaciones

## 🔧 Configuración

### Variables de Entorno
| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `JWT_SECRET` | Clave secreta para JWT | `mySecretKeyForBookingSystemApiWithMinimum256BitsLength` |
| `JWT_EXPIRATION` | Tiempo de expiración JWT (ms) | `86400000` (24h) |

### Profiles de Spring
- `default`: Configuración para desarrollo con H2

## 🤝 Contribuir

1. Fork el proyecto
2. Crear rama para feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👨‍💻 Autor

**Felipe Camacho**
- GitHub: [@PipeC29](https://github.com/PipeC29/)
- Email: ing.felipecamachob@gmail.com

---

⭐ ¡Si te gustó este proyecto, dale una estrella en GitHub!
