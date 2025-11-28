# ✅ Backend Admin Permissions - COMPLETADO

## 🎯 Cambios Realizados en AlertController.java

### **1. Método Auxiliar: `isUserAdmin()`**
```java
private boolean isUserAdmin(String userId) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        return user.getRole() == User.Role.ADMIN;
    }
    return false;
}
```

### **2. Endpoint PUT /alerts/{id}?userId=xxx (Solo ADMIN)**
- Valida que el usuario sea ADMIN
- Permite actualizar: status, priority, title, description
- Retorna error 403 si no es ADMIN

### **3. Endpoint DELETE /alerts/{id}?userId=xxx (Solo ADMIN)**
- Valida que el usuario sea ADMIN
- Elimina la alerta
- Retorna error 403 si no es ADMIN

## ✅ Backend Completo

El backend ya tiene validación de roles para:
- ✅ Actualizar alertas (solo ADMIN)
- ✅ Eliminar alertas (solo ADMIN)

## ⚠️ Falta en Flutter

Actualizar `AlertsService.dart` para enviar `userId` en las peticiones de update y delete.
