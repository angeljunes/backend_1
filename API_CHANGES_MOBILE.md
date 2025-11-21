# 📱 Cambios en la API para Aplicación Móvil - Migración a MongoDB

## ⚠️ CAMBIOS IMPORTANTES

Este documento describe los cambios realizados en el backend después de migrar de MySQL/JPA a MongoDB. **El equipo de desarrollo móvil debe actualizar su aplicación** para ser compatible con estos cambios.

---

## 🔄 Cambio Principal: IDs de `Long` a `String`

### ❌ ANTES (MySQL/JPA)
```json
{
  "id": 123,
  "userId": 456,
  "countryId": 789
}
```

### ✅ AHORA (MongoDB)
```json
{
  "id": "507f1f77bcf86cd799439011",
  "userId": "507f1f77bcf86cd799439012",
  "countryId": "507f1f77bcf86cd799439013"
}
```

**⚠️ IMPORTANTE**: Todos los IDs ahora son **Strings** (ObjectId de MongoDB), no números.

---

## 📋 Cambios por Endpoint

### 1. **Autenticación** - `/api/auth`

#### POST `/api/auth/register`
**Request (sin cambios):**
```json
{
  "email": "usuario@ejemplo.com",
  "username": "nombre_usuario",
  "password": "contraseña123"
}
```

**Response (cambio en userId):**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "userId": "507f1f77bcf86cd799439011"  // ⚠️ Ahora es String, no Long
}
```

#### POST `/api/auth/login`
**Request (sin cambios):**
```json
{
  "identity": "usuario@ejemplo.com",
  "password": "contraseña123"
}
```

**Response (cambio en user.id):**
```json
{
  "success": true,
  "message": "Login exitoso",
  "user": {
    "id": "507f1f77bcf86cd799439011",  // ⚠️ Ahora es String
    "email": "usuario@ejemplo.com",
    "username": "nombre_usuario"
  }
}
```

#### GET `/api/auth/user/{id}`
**Cambio en parámetro:**
- **Antes**: `/api/auth/user/123` (número)
- **Ahora**: `/api/auth/user/507f1f77bcf86cd799439011` (string)

**Response:**
```json
{
  "id": "507f1f77bcf86cd799439011",  // ⚠️ String
  "email": "usuario@ejemplo.com",
  "username": "nombre_usuario",
  "createdAt": "2024-01-15T10:30:00"
}
```

---

### 2. **Ubicaciones** - `/api/location`

#### GET `/api/location/countries`
**Response (cambio en id):**
```json
[
  {
    "id": "507f1f77bcf86cd799439021",  // ⚠️ String
    "name": "Perú"
  },
  {
    "id": "507f1f77bcf86cd799439022",  // ⚠️ String
    "name": "Colombia"
  }
]
```

#### GET `/api/location/cities/{countryId}`
**Cambio en parámetro:**
- **Antes**: `/api/location/cities/123` (número)
- **Ahora**: `/api/location/cities/507f1f77bcf86cd799439021` (string)

**Response (cambios en id y countryId):**
```json
[
  {
    "id": "507f1f77bcf86cd799439031",  // ⚠️ String
    "name": "Lima",
    "countryId": "507f1f77bcf86cd799439021",  // ⚠️ String
    "countryName": "Perú"
  },
  {
    "id": "507f1f77bcf86cd799439032",  // ⚠️ String
    "name": "Arequipa",
    "countryId": "507f1f77bcf86cd799439021",  // ⚠️ String
    "countryName": "Perú"
  }
]
```

#### GET `/api/location/countries/{id}`
**Cambio en parámetro:**
- **Antes**: `/api/location/countries/123` (número)
- **Ahora**: `/api/location/countries/507f1f77bcf86cd799439021` (string)

#### GET `/api/location/cities/detail/{id}`
**Cambio en parámetro:**
- **Antes**: `/api/location/cities/detail/123` (número)
- **Ahora**: `/api/location/cities/detail/507f1f77bcf86cd799439031` (string)

---

### 3. **Alertas** - `/api/alerts`

#### GET `/api/alerts`
**Response (cambios en todos los IDs):**
```json
[
  {
    "id": "507f1f77bcf86cd799439041",  // ⚠️ String
    "title": "Robo en zona comercial",
    "description": "Se reportó un robo...",
    "priority": "ALTA",
    "status": "PENDIENTE",
    "address": "Av. Principal 123",
    "latitude": -12.046374,
    "longitude": -77.042793,
    "createdAt": "2024-01-15T10:30:00",
    "user": {
      "id": "507f1f77bcf86cd799439011",  // ⚠️ String
      "username": "testuser"
    },
    "country": {
      "id": "507f1f77bcf86cd799439021",  // ⚠️ String
      "name": "Perú"
    },
    "city": {
      "id": "507f1f77bcf86cd799439031",  // ⚠️ String
      "name": "Lima"
    }
  }
]
```

#### GET `/api/alerts/{id}`
**Cambio en parámetro:**
- **Antes**: `/api/alerts/123` (número)
- **Ahora**: `/api/alerts/507f1f77bcf86cd799439041` (string)

#### POST `/api/alerts`
**Request (cambios en IDs):**
```json
{
  "title": "Nueva alerta",
  "description": "Descripción de la alerta",
  "priority": "ALTA",
  "address": "Av. Principal 123",
  "latitude": -12.046374,
  "longitude": -77.042793,
  "userId": "507f1f77bcf86cd799439011",  // ⚠️ String, no número
  "countryId": "507f1f77bcf86cd799439021",  // ⚠️ String, no número
  "cityId": "507f1f77bcf86cd799439031"  // ⚠️ String, no número
}
```

**Response:**
```json
{
  "id": "507f1f77bcf86cd799439041",  // ⚠️ String
  "title": "Nueva alerta",
  "description": "Descripción de la alerta",
  "priority": "ALTA",
  "status": "PENDIENTE",
  "address": "Av. Principal 123",
  "latitude": -12.046374,
  "longitude": -77.042793,
  "createdAt": "2024-01-15T10:30:00",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "username": "testuser"
  },
  "country": {
    "id": "507f1f77bcf86cd799439021",
    "name": "Perú"
  },
  "city": {
    "id": "507f1f77bcf86cd799439031",
    "name": "Lima"
  }
}
```

#### PUT `/api/alerts/{id}/status`
**Cambio en parámetro:**
- **Antes**: `/api/alerts/123/status` (número)
- **Ahora**: `/api/alerts/507f1f77bcf86cd799439041/status` (string)

**Request (sin cambios):**
```json
{
  "status": "VERIFICADA"
}
```

#### GET `/api/alerts/filter`
**Query params (sin cambios):**
```
GET /api/alerts/filter?priority=ALTA&status=PENDIENTE
```

**Response (cambios en IDs):**
```json
[
  {
    "id": "507f1f77bcf86cd799439041",  // ⚠️ String
    "title": "Robo en zona comercial",
    // ... resto de campos con IDs como String
  }
]
```

#### GET `/api/alerts/location`
**Query params (sin cambios):**
```
GET /api/alerts/location?lat=-12.046374&lng=-77.042793&radius=0.01
```

**Response (cambios en IDs):**
```json
[
  {
    "id": "507f1f77bcf86cd799439041",  // ⚠️ String
    // ... resto de campos
  }
]
```

---

## 🔧 Cambios Necesarios en la Aplicación Móvil

### 1. **Modelos de Datos**

#### ❌ ANTES (Java/Kotlin)
```kotlin
data class User(
    val id: Long,  // ❌ Cambiar a String
    val email: String,
    val username: String
)

data class Alert(
    val id: Long,  // ❌ Cambiar a String
    val userId: Long,  // ❌ Cambiar a String
    val countryId: Long,  // ❌ Cambiar a String
    val cityId: Long  // ❌ Cambiar a String
)
```

#### ✅ AHORA (Java/Kotlin)
```kotlin
data class User(
    val id: String,  // ✅ String
    val email: String,
    val username: String
)

data class Alert(
    val id: String,  // ✅ String
    val userId: String,  // ✅ String
    val countryId: String,  // ✅ String
    val cityId: String  // ✅ String
)
```

#### ❌ ANTES (Swift)
```swift
struct User: Codable {
    let id: Int64  // ❌ Cambiar a String
    let email: String
    let username: String
}

struct Alert: Codable {
    let id: Int64  // ❌ Cambiar a String
    let userId: Int64  // ❌ Cambiar a String
    let countryId: Int64  // ❌ Cambiar a String
    let cityId: Int64  // ❌ Cambiar a String
}
```

#### ✅ AHORA (Swift)
```swift
struct User: Codable {
    let id: String  // ✅ String
    let email: String
    let username: String
}

struct Alert: Codable {
    let id: String  // ✅ String
    let userId: String  // ✅ String
    let countryId: String  // ✅ String
    let cityId: String  // ✅ String
}
```

#### ❌ ANTES (Dart/Flutter)
```dart
class User {
  final int id;  // ❌ Cambiar a String
  final String email;
  final String username;
  
  User({required this.id, required this.email, required this.username});
  
  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'] as int,  // ❌ Cambiar
      email: json['email'] as String,
      username: json['username'] as String,
    );
  }
}
```

#### ✅ AHORA (Dart/Flutter)
```dart
class User {
  final String id;  // ✅ String
  final String email;
  final String username;
  
  User({required this.id, required this.email, required this.username});
  
  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'] as String,  // ✅ String
      email: json['email'] as String,
      username: json['username'] as String,
    );
  }
}
```

---

### 2. **Llamadas a la API**

#### ❌ ANTES
```kotlin
// Construir URL con número
val url = "/api/alerts/${alertId}"  // alertId era Long
val url = "/api/location/cities/${countryId}"  // countryId era Long
```

#### ✅ AHORA
```kotlin
// Construir URL con String
val url = "/api/alerts/${alertId}"  // alertId es String
val url = "/api/location/cities/${countryId}"  // countryId es String
```

**Nota**: La construcción de URLs no cambia, pero los valores ahora son Strings.

---

### 3. **Almacenamiento Local**

#### ❌ ANTES
```kotlin
// Guardar ID como número
sharedPreferences.putLong("user_id", userId)
val userId = sharedPreferences.getLong("user_id", 0L)
```

#### ✅ AHORA
```kotlin
// Guardar ID como String
sharedPreferences.putString("user_id", userId)
val userId = sharedPreferences.getString("user_id", "")
```

---

### 4. **Validación de IDs**

#### ❌ ANTES
```kotlin
fun isValidId(id: Long): Boolean {
    return id > 0
}
```

#### ✅ AHORA
```kotlin
fun isValidId(id: String): Boolean {
    return id.isNotBlank() && id.length == 24  // ObjectId de MongoDB tiene 24 caracteres
}
```

---

### 5. **Comparación de IDs**

#### ❌ ANTES
```kotlin
if (user.id == currentUserId) {  // Comparación de Long
    // ...
}
```

#### ✅ AHORA
```kotlin
if (user.id == currentUserId) {  // Comparación de String
    // ...
}
```

---

## 📝 Checklist de Migración

- [ ] Actualizar todos los modelos de datos (User, Alert, Country, City)
- [ ] Cambiar tipos de `Long/Int64/Int` a `String` para todos los IDs
- [ ] Actualizar parsers JSON para manejar IDs como String
- [ ] Actualizar almacenamiento local (SharedPreferences, UserDefaults, etc.)
- [ ] Actualizar validaciones de IDs
- [ ] Actualizar comparaciones de IDs
- [ ] Probar todos los endpoints que usan IDs
- [ ] Actualizar documentación interna del equipo móvil

---

## 🧪 Ejemplos de Pruebas

### Test de Registro
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "test@ejemplo.com",
  "username": "testuser",
  "password": "password123"
}

Response:
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "userId": "507f1f77bcf86cd799439011"  // ✅ String
}
```

### Test de Obtener Ciudades
```http
GET /api/location/cities/507f1f77bcf86cd799439021

Response:
[
  {
    "id": "507f1f77bcf86cd799439031",  // ✅ String
    "name": "Lima",
    "countryId": "507f1f77bcf86cd799439021",  // ✅ String
    "countryName": "Perú"
  }
]
```

### Test de Crear Alerta
```http
POST /api/alerts
Content-Type: application/json

{
  "title": "Test Alert",
  "description": "Test description",
  "priority": "ALTA",
  "userId": "507f1f77bcf86cd799439011",  // ✅ String
  "countryId": "507f1f77bcf86cd799439021",  // ✅ String
  "cityId": "507f1f77bcf86cd799439031",  // ✅ String
  "latitude": -12.046374,
  "longitude": -77.042793
}

Response:
{
  "id": "507f1f77bcf86cd799439041",  // ✅ String
  "title": "Test Alert",
  // ... resto de campos
}
```

---

## ⚠️ Errores Comunes a Evitar

### ❌ Error 1: Enviar número en lugar de String
```json
// ❌ INCORRECTO
{
  "userId": 123
}

// ✅ CORRECTO
{
  "userId": "507f1f77bcf86cd799439011"
}
```

### ❌ Error 2: Parsear ID como número
```kotlin
// ❌ INCORRECTO
val id = json.getLong("id")

// ✅ CORRECTO
val id = json.getString("id")
```

### ❌ Error 3: Comparar String con número
```kotlin
// ❌ INCORRECTO
if (user.id == 123L) { }

// ✅ CORRECTO
if (user.id == "507f1f77bcf86cd799439011") { }
```

---

## 📞 Soporte

Si encuentras algún problema durante la migración, contacta al equipo de backend con:
- El endpoint que estás usando
- El error que recibes
- El código de tu modelo de datos

---

## 📅 Fecha de Migración

**Fecha**: Enero 2024  
**Versión API**: 2.0 (MongoDB)  
**Versión Anterior**: 1.0 (MySQL)

---

## 🔗 Recursos Adicionales

- [Documentación MongoDB ObjectId](https://www.mongodb.com/docs/manual/reference/method/ObjectId/)
- [Formato de IDs MongoDB](https://www.mongodb.com/docs/manual/reference/bson-types/#objectid)

