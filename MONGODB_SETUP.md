# 🗄️ Guía de Configuración MongoDB para RCAS

Esta guía te ayudará a configurar las colecciones de MongoDB para el sistema RCAS.

## 📋 Requisitos Previos

- MongoDB Atlas o MongoDB local instalado
- Acceso a la base de datos
- MongoDB Shell (mongosh) o MongoDB Compass

## 🚀 Opción 1: Usar el Script de Inicialización (Recomendado)

### Paso 1: Conectar a MongoDB

**MongoDB Atlas:**
```bash
mongosh "mongodb+srv://USUARIO:CONTRASEÑA@CLUSTER.mongodb.net/app"
```

**MongoDB Local:**
```bash
mongosh mongodb://localhost:27017/app
```

### Paso 2: Ejecutar el Script

```bash
mongosh "TU_CONEXION_STRING" < init_mongodb.js
```

O copia y pega el contenido de `init_mongodb.js` directamente en MongoDB Compass o en la shell.

## 🛠️ Opción 2: Crear Manualmente

### 1. Crear Colección `users`

```javascript
use app;

db.createCollection("users");
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "username": 1 }, { unique: true });

// Insertar usuario de ejemplo
db.users.insertOne({
  "email": "test@rcas.com",
  "username": "testuser",
  "passwordHash": "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
  "createdAt": new Date()
});
```

### 2. Crear Colección `countries`

```javascript
db.createCollection("countries");
db.countries.createIndex({ "name": 1 }, { unique: true });

db.countries.insertMany([
  { "name": "Perú", "createdAt": new Date() },
  { "name": "Colombia", "createdAt": new Date() },
  { "name": "Ecuador", "createdAt": new Date() },
  { "name": "Chile", "createdAt": new Date() }
]);
```

### 3. Crear Colección `cities`

```javascript
db.createCollection("cities");

// Obtener ID de Perú
var peruId = db.countries.findOne({ "name": "Perú" })._id;

db.cities.createIndex({ "country.$id": 1 });
db.cities.createIndex({ "name": 1 });

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
  }
]);
```

### 4. Crear Colección `alerts`

```javascript
db.createCollection("alerts");

var userId = db.users.findOne({ "username": "testuser" })._id;
var peruId = db.countries.findOne({ "name": "Perú" })._id;
var limaId = db.cities.findOne({ "name": "Lima" })._id;

db.alerts.createIndex({ "status": 1 });
db.alerts.createIndex({ "priority": 1 });
db.alerts.createIndex({ "user.$id": 1 });
db.alerts.createIndex({ "country.$id": 1 });
db.alerts.createIndex({ "city.$id": 1 });
db.alerts.createIndex({ "createdAt": -1 });

db.alerts.insertOne({
  "title": "Robo en zona comercial",
  "description": "Se reportó un robo en la zona comercial",
  "priority": "ALTA",
  "status": "PENDIENTE",
  "address": "Av. Principal 123, Lima",
  "latitude": -12.046374,
  "longitude": -77.042793,
  "user": { "$ref": "users", "$id": userId },
  "country": { "$ref": "countries", "$id": peruId },
  "city": { "$ref": "cities", "$id": limaId },
  "createdAt": new Date()
});
```

## 📊 Estructura de las Colecciones

### `users`
- `_id`: String/ObjectId (generado automáticamente)
- `email`: String (único, indexado)
- `username`: String (único, indexado)
- `passwordHash`: String
- `createdAt`: Date

### `countries`
- `_id`: String/ObjectId (generado automáticamente)
- `name`: String (único, indexado)
- `createdAt`: Date

### `cities`
- `_id`: String/ObjectId (generado automáticamente)
- `name`: String
- `country`: DBRef a `countries`
- `createdAt`: Date

### `alerts`
- `_id`: String/ObjectId (generado automáticamente)
- `title`: String
- `description`: String
- `priority`: String ("ALTA", "MEDIA", "BAJA")
- `status`: String ("PENDIENTE", "VERIFICADA", "RESUELTA")
- `address`: String (opcional)
- `latitude`: Number
- `longitude`: Number
- `user`: DBRef a `users`
- `country`: DBRef a `countries`
- `city`: DBRef a `cities`
- `createdAt`: Date

## ✅ Verificar la Instalación

```javascript
use app;

// Verificar colecciones
db.getCollectionNames();

// Contar documentos
db.users.countDocuments();
db.countries.countDocuments();
db.cities.countDocuments();
db.alerts.countDocuments();

// Ver índices
db.users.getIndexes();
db.countries.getIndexes();
db.cities.getIndexes();
db.alerts.getIndexes();
```

## 🔍 Consultas de Ejemplo

### Obtener todas las ciudades de un país
```javascript
var peruId = db.countries.findOne({ "name": "Perú" })._id;
db.cities.find({ "country.$id": peruId }).sort({ "name": 1 });
```

### Obtener alertas por estado
```javascript
db.alerts.find({ "status": "PENDIENTE" }).sort({ "createdAt": -1 });
```

### Obtener alertas por prioridad
```javascript
db.alerts.find({ "priority": "ALTA" }).sort({ "createdAt": -1 });
```

## 🗑️ Limpiar Base de Datos (Solo Desarrollo)

```javascript
use app;

db.users.drop();
db.countries.drop();
db.cities.drop();
db.alerts.drop();
```

## 📝 Notas Importantes

1. **DBRef**: MongoDB almacena referencias como objetos con `$ref` y `$id`
2. **Índices Únicos**: Previenen duplicados en `email`, `username` y `name` de países
3. **Fechas**: Usa `new Date()` en JavaScript o formato ISO en JSON
4. **IDs**: Pueden ser ObjectId o String dependiendo de tu configuración

## 🔐 Credenciales de Prueba

Después de ejecutar el script, puedes usar:
- **Email**: `test@rcas.com`
- **Username**: `testuser`
- **Password**: `password` (hash almacenado en BD)

## 📚 Archivos Relacionados

- `mongodb_models.md` - Documentación detallada de los modelos
- `init_mongodb.js` - Script de inicialización completo
- `application.properties` - Configuración de conexión MongoDB

## 🆘 Solución de Problemas

### Error: "Collection already exists"
```javascript
// Eliminar y recrear (solo desarrollo)
db.users.drop();
// Luego ejecutar el script nuevamente
```

### Error: "Index already exists"
```javascript
// Los índices se crean automáticamente si no existen
// Puedes ignorar este error o eliminarlos primero:
db.users.dropIndexes();
```

### Error: "Duplicate key error"
```javascript
// Verificar documentos duplicados
db.users.find({ "email": "test@rcas.com" });
// Eliminar duplicados si es necesario
```

