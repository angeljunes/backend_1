package com.rcas.repository;

import com.rcas.entity.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {
    
    List<Alert> findByStatusOrderByCreatedAtDesc(Alert.Status status);
    
    List<Alert> findByPriorityOrderByCreatedAtDesc(Alert.Priority priority);
    
    // Consulta por ID de usuario usando DBRef
    // Spring Data MongoDB puede buscar por el objeto User completo o por su ID
    // Usamos un método derivado que busca por el campo user
    List<Alert> findByUser_IdOrderByCreatedAtDesc(String userId);
    
    default List<Alert> findByFilters(Alert.Priority priority, Alert.Status status) {
        if (priority != null && status != null) {
            return findByPriorityAndStatusOrderByCreatedAtDesc(priority, status);
        } else if (priority != null) {
            return findByPriorityOrderByCreatedAtDesc(priority);
        } else if (status != null) {
            return findByStatusOrderByCreatedAtDesc(status);
        } else {
            return findAllByOrderByCreatedAtDesc();
        }
    }
    
    List<Alert> findByPriorityAndStatusOrderByCreatedAtDesc(Alert.Priority priority, Alert.Status status);
    
    @Query(value = "{ 'latitude': { $gte: ?0, $lte: ?1 }, " +
           "'longitude': { $gte: ?2, $lte: ?3 } }", sort = "{ 'createdAt': -1 }")
    List<Alert> findByLocationBounds(BigDecimal minLat, BigDecimal maxLat,
                                   BigDecimal minLng, BigDecimal maxLng);
    
    List<Alert> findAllByOrderByCreatedAtDesc();
    
    default List<Alert> findAllWithRelations() {
        return findAllByOrderByCreatedAtDesc();
    }
}
