package com.rcas.controller;

import com.rcas.entity.User;
import com.rcas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = userData.get("email");
            String username = userData.get("username");
            String password = userData.get("password");
            String fullName = userData.get("fullName");
            String role = userData.get("role");
            String zone = userData.get("zone");

            // Validaciones básicas
            if (email == null || username == null || password == null || fullName == null) {
                response.put("error", "Email, username, password y fullName son requeridos");
                return ResponseEntity.badRequest().body(response);
            }

            // Verificar si el usuario ya existe
            if (userRepository.existsByEmail(email)) {
                response.put("error", "El email ya está registrado");
                return ResponseEntity.badRequest().body(response);
            }

            if (userRepository.existsByUsername(username)) {
                response.put("error", "El nombre de usuario ya está registrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear nuevo usuario
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setFullName(fullName);

            // Asignar rol (por defecto USER)
            if (role != null && role.equalsIgnoreCase("ADMIN")) {
                user.setRole(User.Role.ADMIN);
            } else {
                user.setRole(User.Role.USER);
            }

            // Asignar zona
            if (zone != null && !zone.trim().isEmpty()) {
                user.setZone(zone);
            }

            User savedUser = userRepository.save(user);

            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("userId", savedUser.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();

        try {
            String identity = credentials.get("identity"); // email o username
            String password = credentials.get("password");

            if (identity == null || password == null) {
                response.put("error", "Credenciales incompletas");
                return ResponseEntity.badRequest().body(response);
            }

            // Buscar usuario por email o username
            Optional<User> userOpt = userRepository.findByEmailOrUsername(identity, identity);

            if (userOpt.isEmpty()) {
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOpt.get();

            // Verificar contraseña
            if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                response.put("error", "Contraseña incorrecta");
                return ResponseEntity.badRequest().body(response);
            }

            // Login exitoso - devolver TODOS los campos requeridos por Flutter
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("email", user.getEmail());
            userMap.put("username", user.getUsername());
            userMap.put("fullName", user.getFullName() != null ? user.getFullName() : "");
            userMap.put("role", user.getRole() != null ? user.getRole().name() : "USER");
            userMap.put("zone", user.getZone() != null ? user.getZone() : "");

            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("user", userMap);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
}
