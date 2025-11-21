# Modelos de Documentos MongoDB para RCAS

## 📋 Índice de Colecciones
1. **users** - Usuarios del sistema
2. **countries** - Países
3. **cities** - Ciudades
4. **alerts** - Alertas de seguridad

---

## 1. Colección: `users`

### Estructura del Documento
```json
{
  "_id": "ObjectId o String",
  "email": "usuario@ejemplo.com",
  "username": "nombre_usuario",
  "passwordHash": "$2a$10$hashedPassword...",
  "createdAt": ISODate("2024-01-15T10:30:00Z")
}
```

### Índices Requeridos
```javascript
// Crear índices únicos
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "username": 1 }, { unique: true });
```

### Ejemplo de Documento
```json
{
  "_id": "507f1f77bcf86cd799439011",
  "email": "test@rcas.com",
  "username": "testuser",
  "passwordHash": "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
  "createdAt": ISODate("2024-01-15T10:30:00Z")
}
```

---

## 2. Colección: `countries`

### Estructura del Documento
```json
{
  "_id": "ObjectId o String",
  "name": "Nombre del País",
  "createdAt": ISODate("2024-01-15T10:30:00Z")
}
```

### Índices Requeridos
```javascript
// Crear índice único para el nombre
db.countries.createIndex({ "name": 1 }, { unique: true });
```

### Ejemplo de Documento
```json
{
  "_id": "507f1f77bcf86cd799439021",
  "name": "Perú",
  "createdAt": ISODate("2024-01-15T10:30:00Z")
}
```

**Nota**: Actualmente solo se usa Perú. Puedes agregar más países más adelante si es necesario.

---

## 3. Colección: `cities`

### Estructura del Documento
```json
{
  "_id": "ObjectId o String",
  "name": "Nombre de la Ciudad",
  "country": {
    "$ref": "countries",
    "$id": "ObjectId o String del país"
  },
  "createdAt": ISODate("2024-01-15T10:30:00Z")
}
```

### Índices Recomendados
```javascript
// Índice para búsquedas por país
db.cities.createIndex({ "country.$id": 1 });
// Índice para búsquedas por nombre
db.cities.createIndex({ "name": 1 });
// Índice compuesto para búsquedas por país y nombre
db.cities.createIndex({ "country.$id": 1, "name": 1 });
```

### Ejemplos de Documentos (Ciudades de Perú)
```json
[
  {
    "_id": "507f1f77bcf86cd799439031",
    "name": "Lima",
    "country": {
      "$ref": "countries",
      "$id": "507f1f77bcf86cd799439021"
    },
    "createdAt": ISODate("2024-01-15T10:30:00Z")
  },
  {
    "_id": "507f1f77bcf86cd799439032",
    "name": "Arequipa",
    "country": {
      "$ref": "countries",
      "$id": "507f1f77bcf86cd799439021"
    },
    "createdAt": ISODate("2024-01-15T10:30:00Z")
  },
  {
    "_id": "507f1f77bcf86cd799439033",
    "name": "Cusco",
    "country": {
      "$ref": "countries",
      "$id": "507f1f77bcf86cd799439021"
    },
    "createdAt": ISODate("2024-01-15T10:30:00Z")
  },
  {
    "_id": "507f1f77bcf86cd799439034",
    "name": "Trujillo",
    "country": {
      "$ref": "countries",
      "$id": "507f1f77bcf86cd799439021"
    },
    "createdAt": ISODate("2024-01-15T10:30:00Z")
  }
]
```

**Ciudades incluidas**: Lima, Arequipa, Cusco, Trujillo, Chiclayo, Piura, Iquitos, Huancayo

---

## 4. Colección: `alerts`

### Estructura del Documento
```json
{
  "_id": "ObjectId o String",
  "title": "Título de la Alerta",
  "description": "Descripción detallada de la alerta",
  "priority": "ALTA | MEDIA | BAJA",
  "status": "PENDIENTE | VERIFICADA | RESUELTA",
  "address": "Dirección física (opcional)",
  "latitude": 12.345678,
  "longitude": -77.123456,
  "user": {
    "$ref": "users",
    "$id": "ObjectId o String del usuario"
  },
  "country": {
    "$ref": "countries",
    "$id": "ObjectId o String del país"
  },
  "city": {
    "$ref": "cities",
    "$id": "ObjectId o String de la ciudad"
  },
  "createdAt": ISODate("2024-01-15T10:30:00Z")
}
```

### Índices Recomendados
```javascript
// Índice para búsquedas por estado
db.alerts.createIndex({ "status": 1 });
// Índice para búsquedas por prioridad
db.alerts.createIndex({ "priority": 1 });
// Índice para búsquedas por usuario
db.alerts.createIndex({ "user.$id": 1 });
// Índice para búsquedas por país
db.alerts.createIndex({ "country.$id": 1 });
// Índice para búsquedas por ciudad
db.alerts.createIndex({ "city.$id": 1 });
// Índice geográfico 2dsphere para búsquedas por ubicación
db.alerts.createIndex({ "location": "2dsphere" });
// Índice compuesto para ordenamiento por fecha
db.alerts.createIndex({ "createdAt": -1 });
// Índice compuesto para filtros comunes
db.alerts.createIndex({ "status": 1, "priority": 1, "createdAt": -1 });
```

### Ejemplos de Documentos
```json
[
  {
    "_id": "507f1f77bcf86cd799439041",
    "title": "Robo en zona comercial",
    "description": "Se reportó un robo en la zona comercial del centro",
    "priority": "ALTA",
    "status": "PENDIENTE",
    "address": "Av. Principal 123, Lima",
    "latitude": -12.046374,
    "longitude": -77.042793,
    "user": {
      "$ref": "users",
      "$id": "507f1f77bcf86cd799439011"
    },
    "country": {
      "$ref": "countries",
      "$id": "507f1f77bcf86cd799439021"
    },
    "city": {
      "$ref": "cities",
      "$id": "507f1f77bcf86cd799439031"
    },
    "createdAt": ISODate("2024-01-15T10:30:00Z")
  },
  {
    "_id": "507f1f77bcf86cd799439042",
    "title": "Accidente de tránsito",
    "description": "Accidente vehicular en intersección principal",
    "priority": "MEDIA",
    "status": "VERIFICADA",
    "address": "Calle Secundaria 456, Arequipa",
    "latitude": -16.409047,
    "longitude": -71.537451,
    "user": {
      "$ref": "users",
      "$id": "507f1f77bcf86cd799439011"
    },
    "country": {
      "$ref": "countries",
      "$id": "507f1f77bcf86cd799439021"
    },
    "city": {
      "$ref": "cities",
      "$id": "507f1f77bcf86cd799439032"
    },
    "createdAt": ISODate("2024-01-15T11:00:00Z")
  }
]
```

---

## 📝 Script Completo de Inicialización

### Script MongoDB Shell (mongosh)
```javascript
// Conectar a la base de datos
use app;

// ============================================
// 1. CREAR COLECCIÓN: users
// ============================================
db.createCollection("users");

// Crear índices para users
db.users.createIndex({ "email": 1 }, { unique: true, name: "idx_email_unique" });
db.users.createIndex({ "username": 1 }, { unique: true, name: "idx_username_unique" });

// Insertar usuario de ejemplo
db.users.insertOne({
  "email": "test@rcas.com",
  "username": "testuser",
  "passwordHash": "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
  "createdAt": new Date()
});

// ============================================
// 2. CREAR COLECCIÓN: countries
// ============================================
db.createCollection("countries");

// Crear índice para countries
db.countries.createIndex({ "name": 1 }, { unique: true, name: "idx_name_unique" });

// Insertar solo Perú
db.countries.insertOne({
  "name": "Perú",
  "createdAt": new Date()
});

// ============================================
// 3. CREAR COLECCIÓN: cities
// ============================================
db.createCollection("cities");

// Obtener ID de Perú
var peruId = db.countries.findOne({ "name": "Perú" })._id;

// Crear índices para cities
db.cities.createIndex({ "country.$id": 1 }, { name: "idx_country_id" });
db.cities.createIndex({ "name": 1 }, { name: "idx_name" });
db.cities.createIndex({ "country.$id": 1, "name": 1 }, { name: "idx_country_name" });

// Insertar ciudades de Perú
db.cities.insertMany([
  {
    "name": "Lima",
    "country": { "$ref": "countries", "$id": peruId },
    "createdAt": new Date()
  },
  {
    "name": "Arequipa",
    "country": { "$ref": "countries", "$id": peruId },
    "createdAt": new Date()
  },
  {
    "name": "Cusco",
    "country": { "$ref": "countries", "$id": peruId },
    "createdAt": new Date()
  },
  {
    "name": "Trujillo",
    "country": { "$ref": "countries", "$id": peruId },
    "createdAt": new Date()
  },
  {
    "name": "Chiclayo",
    "country": { "$ref": "countries", "$id": peruId },
    "createdAt": new Date()
  },
  {
    "name": "Piura",
    "country": { "$ref": "countries", "$id": peruId },
    "createdAt": new Date()
  },
  {
    "name": "Iquitos",
    "country": { "$ref": "countries", "$id": peruId },
    "createdAt": new Date()
  },
  {
    "name": "Huancayo",
    "country": { "$ref": "countries", "$id": peruId },
    "createdAt": new Date()
  }
]);

// ============================================
// 4. CREAR COLECCIÓN: alerts
// ============================================
db.createCollection("alerts");

// Obtener IDs de referencia
var userId = db.users.findOne({ "username": "testuser" })._id;
var limaId = db.cities.findOne({ "name": "Lima" })._id;
var arequipaId = db.cities.findOne({ "name": "Arequipa" })._id;

// Crear índices para alerts
db.alerts.createIndex({ "status": 1 }, { name: "idx_status" });
db.alerts.createIndex({ "priority": 1 }, { name: "idx_priority" });
db.alerts.createIndex({ "user.$id": 1 }, { name: "idx_user_id" });
db.alerts.createIndex({ "country.$id": 1 }, { name: "idx_country_id" });
db.alerts.createIndex({ "city.$id": 1 }, { name: "idx_city_id" });
db.alerts.createIndex({ "createdAt": -1 }, { name: "idx_created_at_desc" });
db.alerts.createIndex({ "status": 1, "priority": 1, "createdAt": -1 }, { name: "idx_status_priority_date" });

// Insertar alertas de ejemplo
db.alerts.insertMany([
  {
    "title": "Robo en zona comercial",
    "description": "Se reportó un robo en la zona comercial del centro de Lima",
    "priority": "ALTA",
    "status": "PENDIENTE",
    "address": "Av. Principal 123, Lima",
    "latitude": -12.046374,
    "longitude": -77.042793,
    "user": { "$ref": "users", "$id": userId },
    "country": { "$ref": "countries", "$id": peruId },
    "city": { "$ref": "cities", "$id": limaId },
    "createdAt": new Date()
  },
  {
    "title": "Accidente de tránsito",
    "description": "Accidente vehicular en intersección principal de Arequipa",
    "priority": "MEDIA",
    "status": "VERIFICADA",
    "address": "Calle Secundaria 456, Arequipa",
    "latitude": -16.409047,
    "longitude": -71.537451,
    "user": { "$ref": "users", "$id": userId },
    "country": { "$ref": "countries", "$id": peruId },
    "city": { "$ref": "cities", "$id": arequipaId },
    "createdAt": new Date()
  }
]);

// Verificar las colecciones creadas
print("Colecciones creadas:");
db.getCollectionNames().forEach(function(collection) {
  print("  - " + collection + ": " + db[collection].countDocuments() + " documentos");
});
```

---

## 🔧 Notas Importantes

### Sobre DBRef en MongoDB
- MongoDB almacena las referencias como objetos con `$ref` (nombre de la colección) y `$id` (ID del documento)
- Spring Data MongoDB maneja automáticamente estas referencias cuando usas `@DBRef`
- Los IDs pueden ser `ObjectId` o `String` dependiendo de tu configuración

### Sobre los Índices
- Los índices únicos previenen duplicados
- Los índices en campos de referencia (`$id`) mejoran el rendimiento de las consultas
- El índice en `createdAt` con orden descendente (`-1`) optimiza las consultas ordenadas por fecha

### Sobre las Fechas
- MongoDB almacena fechas como `ISODate`
- En JavaScript/MongoDB Shell usa `new Date()`
- En JSON puedes usar el formato ISO: `"2024-01-15T10:30:00Z"`

---

## 📊 Resumen de Campos por Colección

| Colección | Campos Principales | Índices Únicos | Referencias DBRef |
|-----------|-------------------|----------------|-------------------|
| `users` | email, username, passwordHash | email, username | - |
| `countries` | name | name | - |
| `cities` | name | - | country |
| `alerts` | title, description, priority, status, latitude, longitude | - | user, country, city |

