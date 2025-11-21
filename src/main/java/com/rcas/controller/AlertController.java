package com.rcas.controller;

import com.rcas.entity.Alert;
import com.rcas.entity.City;
import com.rcas.entity.Country;
import com.rcas.entity.User;
import com.rcas.repository.AlertRepository;
import com.rcas.repository.CityRepository;
import com.rcas.repository.CountryRepository;
import com.rcas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alerts")
@CrossOrigin(origins = "*")
public class AlertController {
    
    @Autowired
    private AlertRepository alertRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        List<Alert> alerts = alertRepository.findAllWithRelations();
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<Alert>> getAlertsByFilter(
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status) {
        
        Alert.Priority priorityEnum = null;
        Alert.Status statusEnum = null;
        
        if (priority != null) {
            priorityEnum = Alert.Priority.valueOf(priority.toUpperCase());
        }
        
        if (status != null) {
            statusEnum = Alert.Status.valueOf(status.toUpperCase());
        }
        
        List<Alert> alerts = alertRepository.findByFilters(priorityEnum, statusEnum);
        return ResponseEntity.ok(alerts);
    }
    
    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Map<String, Object> alertData) {
        try {
            Alert alert = new Alert();
            alert.setTitle((String) alertData.get("title"));
            alert.setDescription((String) alertData.get("description"));
            
            // Prioridad
            String priorityStr = (String) alertData.get("priority");
            if (priorityStr != null) {
                alert.setPriority(Alert.Priority.valueOf(priorityStr.toUpperCase()));
            }
            
            // Coordenadas
            if (alertData.get("latitude") != null) {
                alert.setLatitude(new BigDecimal(alertData.get("latitude").toString()));
            }
            if (alertData.get("longitude") != null) {
                alert.setLongitude(new BigDecimal(alertData.get("longitude").toString()));
            }
            
            // Dirección
            alert.setAddress((String) alertData.get("address"));
            
            // Usuario (opcional)
            if (alertData.get("userId") != null) {
                String userId = alertData.get("userId").toString();
                User user = userRepository.findById(userId).orElse(null);
                alert.setUser(user);
            }
            
            // País (opcional)
            if (alertData.get("countryId") != null) {
                String countryId = alertData.get("countryId").toString();
                Country country = countryRepository.findById(countryId).orElse(null);
                alert.setCountry(country);
            }
            
            // Ciudad (opcional)
            if (alertData.get("cityId") != null) {
                String cityId = alertData.get("cityId").toString();
                City city = cityRepository.findById(cityId).orElse(null);
                alert.setCity(city);
            }
            
            Alert savedAlert = alertRepository.save(alert);
            return ResponseEntity.ok(savedAlert);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable String id) {
        return alertRepository.findById(id)
                .map(alert -> ResponseEntity.ok(alert))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Alert> updateAlertStatus(@PathVariable String id, @RequestBody Map<String, String> statusData) {
        return alertRepository.findById(id)
                .map(alert -> {
                    String newStatus = statusData.get("status");
                    alert.setStatus(Alert.Status.valueOf(newStatus.toUpperCase()));
                    return ResponseEntity.ok(alertRepository.save(alert));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/location")
    public ResponseEntity<List<Alert>> getAlertsByLocation(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lng,
            @RequestParam(defaultValue = "0.01") BigDecimal radius) {
        
        BigDecimal minLat = lat.subtract(radius);
        BigDecimal maxLat = lat.add(radius);
        BigDecimal minLng = lng.subtract(radius);
        BigDecimal maxLng = lng.add(radius);
        
        List<Alert> alerts = alertRepository.findByLocationBounds(minLat, maxLat, minLng, maxLng);
        return ResponseEntity.ok(alerts);
    }
}
