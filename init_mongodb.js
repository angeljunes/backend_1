// ============================================
// Script de Inicialización MongoDB para RCAS
// ============================================
// Uso: mongosh "mongodb+srv://..." < init_mongodb.js
// O ejecutar en MongoDB Compass o MongoDB Shell

// Seleccionar o crear la base de datos
use app;

print("============================================");
print("Inicializando base de datos RCAS");
print("============================================");

// ============================================
// 1. CREAR COLECCIÓN: users
// ============================================
print("\n1. Creando colección 'users'...");

// Eliminar colección si existe (opcional, solo para desarrollo)
// db.users.drop();

db.createCollection("users");

// Crear índices para users
db.users.createIndex({ "email": 1 }, { unique: true, name: "idx_email_unique" });
db.users.createIndex({ "username": 1 }, { unique: true, name: "idx_username_unique" });

print("   ✓ Índices creados: email (único), username (único)");

// Insertar usuario de ejemplo
var userResult = db.users.insertOne({
  "email": "test@rcas.com",
  "username": "testuser",
  "passwordHash": "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy", // password: password
  "createdAt": new Date()
});

print("   ✓ Usuario de ejemplo creado: test@rcas.com / testuser");

// ============================================
// 2. CREAR COLECCIÓN: countries
// ============================================
print("\n2. Creando colección 'countries'...");

// Eliminar colección si existe (opcional)
// db.countries.drop();

db.createCollection("countries");

// Crear índice para countries
db.countries.createIndex({ "name": 1 }, { unique: true, name: "idx_name_unique" });

print("   ✓ Índice creado: name (único)");

// Insertar solo Perú
var countriesResult = db.countries.insertOne({
  "name": "Perú",
  "createdAt": new Date()
});

print("   ✓ País insertado: Perú");

// Obtener ID de Perú para usar en ciudades
var peruId = db.countries.findOne({ "name": "Perú" })._id;

// ============================================
// 3. CREAR COLECCIÓN: cities
// ============================================
print("\n3. Creando colección 'cities'...");

// Eliminar colección si existe (opcional)
// db.cities.drop();

db.createCollection("cities");

// Crear índices para cities
db.cities.createIndex({ "country.$id": 1 }, { name: "idx_country_id" });
db.cities.createIndex({ "name": 1 }, { name: "idx_name" });
db.cities.createIndex({ "country.$id": 1, "name": 1 }, { name: "idx_country_name" });

print("   ✓ Índices creados: country.$id, name, country+name");

// Insertar ciudades de Perú
var citiesResult = db.cities.insertMany([
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

print("   ✓ " + citiesResult.insertedIds.length + " ciudades de Perú insertadas");

// ============================================
// 4. CREAR COLECCIÓN: alerts
// ============================================
print("\n4. Creando colección 'alerts'...");

// Eliminar colección si existe (opcional)
// db.alerts.drop();

db.createCollection("alerts");

// Obtener IDs de referencia
var userId = db.users.findOne({ "username": "testuser" })._id;
var limaId = db.cities.findOne({ "name": "Lima" })._id;
var arequipaId = db.cities.findOne({ "name": "Arequipa" })._id;
var cuscoId = db.cities.findOne({ "name": "Cusco" })._id;

// Crear índices para alerts
db.alerts.createIndex({ "status": 1 }, { name: "idx_status" });
db.alerts.createIndex({ "priority": 1 }, { name: "idx_priority" });
db.alerts.createIndex({ "user.$id": 1 }, { name: "idx_user_id" });
db.alerts.createIndex({ "country.$id": 1 }, { name: "idx_country_id" });
db.alerts.createIndex({ "city.$id": 1 }, { name: "idx_city_id" });
db.alerts.createIndex({ "createdAt": -1 }, { name: "idx_created_at_desc" });
db.alerts.createIndex({ "status": 1, "priority": 1, "createdAt": -1 }, { name: "idx_status_priority_date" });

print("   ✓ Índices creados: status, priority, user, country, city, createdAt, compuesto");

// Insertar alertas de ejemplo (solo Perú)
var alertsResult = db.alerts.insertMany([
  {
    "title": "Robo en zona comercial",
    "description": "Se reportó un robo en la zona comercial del centro de Lima. Se requiere presencia policial.",
    "priority": "ALTA",
    "status": "PENDIENTE",
    "address": "Av. Principal 123, Lima, Perú",
    "latitude": -12.046374,
    "longitude": -77.042793,
    "user": { "$ref": "users", "$id": userId },
    "country": { "$ref": "countries", "$id": peruId },
    "city": { "$ref": "cities", "$id": limaId },
    "createdAt": new Date()
  },
  {
    "title": "Accidente de tránsito",
    "description": "Accidente vehicular en intersección principal. Se reportan heridos leves.",
    "priority": "MEDIA",
    "status": "VERIFICADA",
    "address": "Calle Secundaria 456, Arequipa, Perú",
    "latitude": -16.409047,
    "longitude": -71.537451,
    "user": { "$ref": "users", "$id": userId },
    "country": { "$ref": "countries", "$id": peruId },
    "city": { "$ref": "cities", "$id": arequipaId },
    "createdAt": new Date()
  },
  {
    "title": "Actividad sospechosa en centro histórico",
    "description": "Se observa actividad sospechosa en el centro histórico de Cusco. Personas merodeando sin motivo aparente.",
    "priority": "BAJA",
    "status": "PENDIENTE",
    "address": "Plaza de Armas, Cusco, Perú",
    "latitude": -13.517088,
    "longitude": -71.978536,
    "user": { "$ref": "users", "$id": userId },
    "country": { "$ref": "countries", "$id": peruId },
    "city": { "$ref": "cities", "$id": cuscoId },
    "createdAt": new Date()
  }
]);

print("   ✓ " + alertsResult.insertedIds.length + " alertas de ejemplo insertadas");

// ============================================
// RESUMEN FINAL
// ============================================
print("\n============================================");
print("Resumen de colecciones creadas:");
print("============================================");

var collections = ["users", "countries", "cities", "alerts"];

collections.forEach(function(collection) {
  var count = db[collection].countDocuments();
  var indexes = db[collection].getIndexes().length;
  print("  " + collection.padEnd(12) + ": " + 
        count.toString().padStart(4) + " documentos, " + 
        indexes + " índices");
});

print("\n============================================");
print("✓ Inicialización completada exitosamente");
print("============================================");

// Mostrar algunos datos de ejemplo
print("\nDatos de ejemplo:");
print("  Usuario: test@rcas.com / testuser");
print("  Países: " + db.countries.countDocuments());
print("  Ciudades: " + db.cities.countDocuments());
print("  Alertas: " + db.alerts.countDocuments());

