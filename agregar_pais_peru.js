// ============================================
// Script para agregar solo Perú y sus ciudades
// ============================================
// Uso: Si ya tienes la base de datos creada y solo quieres agregar Perú

use app;

print("============================================");
print("Agregando Perú y sus ciudades");
print("============================================");

// Verificar si Perú ya existe
var peruExists = db.countries.findOne({ "name": "Perú" });

if (peruExists) {
    print("\n⚠️  Perú ya existe en la base de datos");
    print("   ID: " + peruExists._id);
    var peruId = peruExists._id;
} else {
    // Crear índice si no existe
    db.countries.createIndex({ "name": 1 }, { unique: true, name: "idx_name_unique" });
    
    // Insertar Perú
    var peruResult = db.countries.insertOne({
        "name": "Perú",
        "createdAt": new Date()
    });
    
    print("\n✓ Perú insertado");
    var peruId = peruResult.insertedId;
}

// Verificar ciudades existentes
var ciudadesExistentes = db.cities.find({ "country.$id": peruId }).toArray();
var nombresCiudadesExistentes = ciudadesExistentes.map(c => c.name);

print("\nCiudades existentes de Perú: " + nombresCiudadesExistentes.length);
if (nombresCiudadesExistentes.length > 0) {
    print("  - " + nombresCiudadesExistentes.join("\n  - "));
}

// Ciudades a insertar
var ciudadesPeru = [
    { "name": "Lima", "latitude": -12.046374, "longitude": -77.042793 },
    { "name": "Arequipa", "latitude": -16.409047, "longitude": -71.537451 },
    { "name": "Cusco", "latitude": -13.517088, "longitude": -71.978536 },
    { "name": "Trujillo", "latitude": -8.111554, "longitude": -79.028774 },
    { "name": "Chiclayo", "latitude": -6.776597, "longitude": -79.844298 },
    { "name": "Piura", "latitude": -5.194490, "longitude": -80.632820 },
    { "name": "Iquitos", "latitude": -3.749120, "longitude": -73.253830 },
    { "name": "Huancayo", "latitude": -12.067150, "longitude": -75.204460 },
    { "name": "Tacna", "latitude": -18.006567, "longitude": -70.246274 },
    { "name": "Ica", "latitude": -14.068021, "longitude": -75.725536 }
];

// Crear índices si no existen
db.cities.createIndex({ "country.$id": 1 }, { name: "idx_country_id" });
db.cities.createIndex({ "name": 1 }, { name: "idx_name" });

// Insertar ciudades que no existen
var ciudadesInsertadas = 0;
var ciudadesDuplicadas = 0;

ciudadesPeru.forEach(function(ciudad) {
    if (!nombresCiudadesExistentes.includes(ciudad.name)) {
        db.cities.insertOne({
            "name": ciudad.name,
            "country": { "$ref": "countries", "$id": peruId },
            "createdAt": new Date()
        });
        ciudadesInsertadas++;
        print("  ✓ " + ciudad.name + " insertada");
    } else {
        ciudadesDuplicadas++;
        print("  - " + ciudad.name + " ya existe (omitida)");
    }
});

print("\n============================================");
print("Resumen:");
print("  Ciudades insertadas: " + ciudadesInsertadas);
print("  Ciudades duplicadas: " + ciudadesDuplicadas);
print("  Total ciudades de Perú: " + db.cities.countDocuments({ "country.$id": peruId }));
print("============================================");

// Mostrar todas las ciudades de Perú
print("\nCiudades de Perú en la base de datos:");
var todasLasCiudades = db.cities.find({ "country.$id": peruId }).sort({ "name": 1 }).toArray();
todasLasCiudades.forEach(function(ciudad) {
    print("  - " + ciudad.name + " (ID: " + ciudad._id + ")");
});

print("\n✓ Proceso completado");

