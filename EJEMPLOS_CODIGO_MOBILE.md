# 💻 Ejemplos de Código - Migración a MongoDB

## 📱 Android (Kotlin)

### Modelos Actualizados

```kotlin
// User.kt
data class User(
    val id: String,  // ✅ Cambiado de Long a String
    val email: String,
    val username: String,
    val createdAt: String? = null
) {
    companion object {
        fun fromJson(json: JSONObject): User {
            return User(
                id = json.getString("id"),  // ✅ String
                email = json.getString("email"),
                username = json.getString("username"),
                createdAt = json.optString("createdAt", null)
            )
        }
    }
}

// Country.kt
data class Country(
    val id: String,  // ✅ Cambiado de Long a String
    val name: String
) {
    companion object {
        fun fromJson(json: JSONObject): Country {
            return Country(
                id = json.getString("id"),  // ✅ String
                name = json.getString("name")
            )
        }
    }
}

// City.kt
data class City(
    val id: String,  // ✅ Cambiado de Long a String
    val name: String,
    val countryId: String,  // ✅ Cambiado de Long a String
    val countryName: String
) {
    companion object {
        fun fromJson(json: JSONObject): City {
            return City(
                id = json.getString("id"),  // ✅ String
                name = json.getString("name"),
                countryId = json.getString("countryId"),  // ✅ String
                countryName = json.getString("countryName")
            )
        }
    }
}

// Alert.kt
data class Alert(
    val id: String,  // ✅ Cambiado de Long a String
    val title: String,
    val description: String?,
    val priority: String,
    val status: String,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val createdAt: String?,
    val userId: String? = null,  // ✅ Cambiado de Long? a String?
    val countryId: String? = null,  // ✅ Cambiado de Long? a String?
    val cityId: String? = null,  // ✅ Cambiado de Long? a String?
    val username: String? = null,
    val countryName: String? = null,
    val cityName: String? = null
) {
    companion object {
        fun fromJson(json: JSONObject): Alert {
            return Alert(
                id = json.getString("id"),  // ✅ String
                title = json.getString("title"),
                description = json.optString("description", null),
                priority = json.getString("priority"),
                status = json.getString("status"),
                address = json.optString("address", null),
                latitude = json.optDouble("latitude", Double.NaN).takeIf { !it.isNaN() },
                longitude = json.optDouble("longitude", Double.NaN).takeIf { !it.isNaN() },
                createdAt = json.optString("createdAt", null),
                userId = json.optJSONObject("user")?.optString("id"),
                countryId = json.optJSONObject("country")?.optString("id"),
                cityId = json.optJSONObject("city")?.optString("id"),
                username = json.optJSONObject("user")?.optString("username"),
                countryName = json.optJSONObject("country")?.optString("name"),
                cityName = json.optJSONObject("city")?.optString("name")
            )
        }
    }
}
```

### Almacenamiento Local

```kotlin
// SharedPreferences
class UserPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    
    fun saveUserId(userId: String) {  // ✅ String
        prefs.edit().putString("user_id", userId).apply()
    }
    
    fun getUserId(): String? {  // ✅ String?
        return prefs.getString("user_id", null)
    }
}
```

### Llamadas a la API

```kotlin
// ApiService.kt
class ApiService {
    private val baseUrl = "http://localhost:8081/api"
    
    // Obtener ciudades por país
    suspend fun getCitiesByCountry(countryId: String): List<City> {  // ✅ String
        val url = "$baseUrl/location/cities/$countryId"  // ✅ String en URL
        // ... hacer request
    }
    
    // Crear alerta
    suspend fun createAlert(
        title: String,
        description: String,
        userId: String,  // ✅ String
        countryId: String,  // ✅ String
        cityId: String,  // ✅ String
        latitude: Double,
        longitude: Double
    ): Alert {
        val body = JSONObject().apply {
            put("title", title)
            put("description", description)
            put("userId", userId)  // ✅ String
            put("countryId", countryId)  // ✅ String
            put("cityId", cityId)  // ✅ String
            put("latitude", latitude)
            put("longitude", longitude)
        }
        // ... hacer POST request
    }
    
    // Actualizar estado de alerta
    suspend fun updateAlertStatus(alertId: String, status: String): Alert {  // ✅ String
        val url = "$baseUrl/alerts/$alertId/status"  // ✅ String en URL
        // ... hacer PUT request
    }
}
```

### Validación de IDs

```kotlin
object ValidationUtils {
    fun isValidMongoId(id: String?): Boolean {
        return !id.isNullOrBlank() && id.length == 24 && id.matches(Regex("[0-9a-fA-F]{24}"))
    }
}
```

---

## 🍎 iOS (Swift)

### Modelos Actualizados

```swift
// User.swift
struct User: Codable {
    let id: String  // ✅ Cambiado de Int64 a String
    let email: String
    let username: String
    let createdAt: String?
    
    enum CodingKeys: String, CodingKey {
        case id
        case email
        case username
        case createdAt
    }
}

// Country.swift
struct Country: Codable {
    let id: String  // ✅ Cambiado de Int64 a String
    let name: String
}

// City.swift
struct City: Codable {
    let id: String  // ✅ Cambiado de Int64 a String
    let name: String
    let countryId: String  // ✅ Cambiado de Int64 a String
    let countryName: String
    
    enum CodingKeys: String, CodingKey {
        case id
        case name
        case countryId
        case countryName
    }
}

// Alert.swift
struct Alert: Codable {
    let id: String  // ✅ Cambiado de Int64 a String
    let title: String
    let description: String?
    let priority: String
    let status: String
    let address: String?
    let latitude: Double?
    let longitude: Double?
    let createdAt: String?
    let userId: String?  // ✅ Cambiado de Int64? a String?
    let countryId: String?  // ✅ Cambiado de Int64? a String?
    let cityId: String?  // ✅ Cambiado de Int64? a String?
    let username: String?
    let countryName: String?
    let cityName: String?
    
    enum CodingKeys: String, CodingKey {
        case id
        case title
        case description
        case priority
        case status
        case address
        case latitude
        case longitude
        case createdAt
        case userId
        case countryId
        case cityId
        case username
        case countryName
        case cityName
    }
}
```

### Almacenamiento Local

```swift
// UserDefaults
class UserPreferences {
    private let userIdKey = "user_id"
    
    func saveUserId(_ userId: String) {  // ✅ String
        UserDefaults.standard.set(userId, forKey: userIdKey)
    }
    
    func getUserId() -> String? {  // ✅ String?
        return UserDefaults.standard.string(forKey: userIdKey)
    }
}
```

### Llamadas a la API

```swift
// ApiService.swift
class ApiService {
    private let baseURL = "http://localhost:8081/api"
    
    // Obtener ciudades por país
    func getCitiesByCountry(countryId: String, completion: @escaping (Result<[City], Error>) -> Void) {  // ✅ String
        let url = URL(string: "\(baseURL)/location/cities/\(countryId)")!  // ✅ String en URL
        // ... hacer request
    }
    
    // Crear alerta
    func createAlert(
        title: String,
        description: String,
        userId: String,  // ✅ String
        countryId: String,  // ✅ String
        cityId: String,  // ✅ String
        latitude: Double,
        longitude: Double,
        completion: @escaping (Result<Alert, Error>) -> Void
    ) {
        let body: [String: Any] = [
            "title": title,
            "description": description,
            "userId": userId,  // ✅ String
            "countryId": countryId,  // ✅ String
            "cityId": cityId,  // ✅ String
            "latitude": latitude,
            "longitude": longitude
        ]
        // ... hacer POST request
    }
}
```

### Validación de IDs

```swift
extension String {
    func isValidMongoId() -> Bool {
        return self.count == 24 && self.range(of: "^[0-9a-fA-F]{24}$", options: .regularExpression) != nil
    }
}
```

---

## 🎯 Flutter (Dart)

### Modelos Actualizados

```dart
// user.dart
class User {
  final String id;  // ✅ Cambiado de int a String
  final String email;
  final String username;
  final String? createdAt;
  
  User({
    required this.id,
    required this.email,
    required this.username,
    this.createdAt,
  });
  
  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'] as String,  // ✅ String
      email: json['email'] as String,
      username: json['username'] as String,
      createdAt: json['createdAt'] as String?,
    );
  }
  
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'email': email,
      'username': username,
      'createdAt': createdAt,
    };
  }
}

// country.dart
class Country {
  final String id;  // ✅ Cambiado de int a String
  final String name;
  
  Country({required this.id, required this.name});
  
  factory Country.fromJson(Map<String, dynamic> json) {
    return Country(
      id: json['id'] as String,  // ✅ String
      name: json['name'] as String,
    );
  }
}

// city.dart
class City {
  final String id;  // ✅ Cambiado de int a String
  final String name;
  final String countryId;  // ✅ Cambiado de int a String
  final String countryName;
  
  City({
    required this.id,
    required this.name,
    required this.countryId,
    required this.countryName,
  });
  
  factory City.fromJson(Map<String, dynamic> json) {
    return City(
      id: json['id'] as String,  // ✅ String
      name: json['name'] as String,
      countryId: json['countryId'] as String,  // ✅ String
      countryName: json['countryName'] as String,
    );
  }
}

// alert.dart
class Alert {
  final String id;  // ✅ Cambiado de int a String
  final String title;
  final String? description;
  final String priority;
  final String status;
  final String? address;
  final double? latitude;
  final double? longitude;
  final String? createdAt;
  final String? userId;  // ✅ Cambiado de int? a String?
  final String? countryId;  // ✅ Cambiado de int? a String?
  final String? cityId;  // ✅ Cambiado de int? a String?
  final String? username;
  final String? countryName;
  final String? cityName;
  
  Alert({
    required this.id,
    required this.title,
    this.description,
    required this.priority,
    required this.status,
    this.address,
    this.latitude,
    this.longitude,
    this.createdAt,
    this.userId,
    this.countryId,
    this.cityId,
    this.username,
    this.countryName,
    this.cityName,
  });
  
  factory Alert.fromJson(Map<String, dynamic> json) {
    return Alert(
      id: json['id'] as String,  // ✅ String
      title: json['title'] as String,
      description: json['description'] as String?,
      priority: json['priority'] as String,
      status: json['status'] as String,
      address: json['address'] as String?,
      latitude: json['latitude'] != null ? (json['latitude'] as num).toDouble() : null,
      longitude: json['longitude'] != null ? (json['longitude'] as num).toDouble() : null,
      createdAt: json['createdAt'] as String?,
      userId: json['user'] != null ? json['user']['id'] as String? : null,
      countryId: json['country'] != null ? json['country']['id'] as String? : null,
      cityId: json['city'] != null ? json['city']['id'] as String? : null,
      username: json['user'] != null ? json['user']['username'] as String? : null,
      countryName: json['country'] != null ? json['country']['name'] as String? : null,
      cityName: json['city'] != null ? json['city']['name'] as String? : null,
    );
  }
}
```

### Almacenamiento Local

```dart
// shared_preferences
import 'package:shared_preferences/shared_preferences.dart';

class UserPreferences {
  static const String userIdKey = 'user_id';
  
  static Future<void> saveUserId(String userId) async {  // ✅ String
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(userIdKey, userId);
  }
  
  static Future<String?> getUserId() async {  // ✅ String?
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(userIdKey);
  }
}
```

### Llamadas a la API

```dart
// api_service.dart
import 'package:http/http.dart' as http;
import 'dart:convert';

class ApiService {
  static const String baseUrl = 'http://localhost:8081/api';
  
  // Obtener ciudades por país
  static Future<List<City>> getCitiesByCountry(String countryId) async {  // ✅ String
    final url = Uri.parse('$baseUrl/location/cities/$countryId');  // ✅ String en URL
    final response = await http.get(url);
    
    if (response.statusCode == 200) {
      final List<dynamic> jsonList = json.decode(response.body);
      return jsonList.map((json) => City.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load cities');
    }
  }
  
  // Crear alerta
  static Future<Alert> createAlert({
    required String title,
    required String description,
    required String userId,  // ✅ String
    required String countryId,  // ✅ String
    required String cityId,  // ✅ String
    required double latitude,
    required double longitude,
  }) async {
    final url = Uri.parse('$baseUrl/alerts');
    final body = json.encode({
      'title': title,
      'description': description,
      'userId': userId,  // ✅ String
      'countryId': countryId,  // ✅ String
      'cityId': cityId,  // ✅ String
      'latitude': latitude,
      'longitude': longitude,
    });
    
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: body,
    );
    
    if (response.statusCode == 200) {
      return Alert.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to create alert');
    }
  }
}
```

### Validación de IDs

```dart
class ValidationUtils {
  static bool isValidMongoId(String? id) {
    if (id == null || id.isEmpty) return false;
    return id.length == 24 && RegExp(r'^[0-9a-fA-F]{24}$').hasMatch(id);
  }
}
```

---

## 🔍 React Native (TypeScript/JavaScript)

### Modelos Actualizados

```typescript
// types.ts
export interface User {
  id: string;  // ✅ Cambiado de number a string
  email: string;
  username: string;
  createdAt?: string;
}

export interface Country {
  id: string;  // ✅ Cambiado de number a string
  name: string;
}

export interface City {
  id: string;  // ✅ Cambiado de number a string
  name: string;
  countryId: string;  // ✅ Cambiado de number a string
  countryName: string;
}

export interface Alert {
  id: string;  // ✅ Cambiado de number a string
  title: string;
  description?: string;
  priority: string;
  status: string;
  address?: string;
  latitude?: number;
  longitude?: number;
  createdAt?: string;
  userId?: string;  // ✅ Cambiado de number? a string?
  countryId?: string;  // ✅ Cambiado de number? a string?
  cityId?: string;  // ✅ Cambiado de number? a string?
  username?: string;
  countryName?: string;
  cityName?: string;
}
```

### Almacenamiento Local

```typescript
// AsyncStorage
import AsyncStorage from '@react-native-async-storage/async-storage';

export const UserStorage = {
  saveUserId: async (userId: string): Promise<void> => {  // ✅ string
    await AsyncStorage.setItem('user_id', userId);
  },
  
  getUserId: async (): Promise<string | null> => {  // ✅ string | null
    return await AsyncStorage.getItem('user_id');
  },
};
```

### Llamadas a la API

```typescript
// api.ts
const BASE_URL = 'http://localhost:8081/api';

export const ApiService = {
  // Obtener ciudades por país
  getCitiesByCountry: async (countryId: string): Promise<City[]> => {  // ✅ string
    const response = await fetch(`${BASE_URL}/location/cities/${countryId}`);  // ✅ string en URL
    const data = await response.json();
    return data;
  },
  
  // Crear alerta
  createAlert: async (alert: {
    title: string;
    description: string;
    userId: string;  // ✅ string
    countryId: string;  // ✅ string
    cityId: string;  // ✅ string
    latitude: number;
    longitude: number;
  }): Promise<Alert> => {
    const response = await fetch(`${BASE_URL}/alerts`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(alert),
    });
    const data = await response.json();
    return data;
  },
};
```

### Validación de IDs

```typescript
export const ValidationUtils = {
  isValidMongoId: (id: string | null | undefined): boolean => {
    if (!id) return false;
    return id.length === 24 && /^[0-9a-fA-F]{24}$/.test(id);
  },
};
```

---

## ✅ Resumen de Cambios por Plataforma

| Plataforma | Tipo Anterior | Tipo Nuevo | Ejemplo |
|-----------|--------------|-----------|---------|
| **Kotlin** | `Long` | `String` | `val id: String` |
| **Swift** | `Int64` | `String` | `let id: String` |
| **Dart** | `int` | `String` | `final String id` |
| **TypeScript** | `number` | `string` | `id: string` |

---

## 🎯 Próximos Pasos

1. Actualizar todos los modelos de datos
2. Actualizar parsers JSON
3. Actualizar almacenamiento local
4. Actualizar llamadas a la API
5. Probar todos los endpoints
6. Actualizar tests unitarios

