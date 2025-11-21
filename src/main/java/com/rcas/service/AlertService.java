package com.rcas.service;

import com.rcas.dto.AlertDTO;
import com.rcas.entity.Alert;
import com.rcas.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {
    
    @Autowired
    private AlertRepository alertRepository;
    
    public List<AlertDTO> getAllAlertsAsDTO() {
        List<Alert> alerts = alertRepository.findAll();
        return alerts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<AlertDTO> getAlertsByStatus(Alert.Status status) {
        List<Alert> alerts = alertRepository.findByStatusOrderByCreatedAtDesc(status);
        return alerts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<AlertDTO> getAlertsByPriority(Alert.Priority priority) {
        List<Alert> alerts = alertRepository.findByPriorityOrderByCreatedAtDesc(priority);
        return alerts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private AlertDTO convertToDTO(Alert alert) {
        AlertDTO dto = new AlertDTO();
        dto.setId(alert.getId());
        dto.setTitle(alert.getTitle());
        dto.setDescription(alert.getDescription());
        dto.setPriority(alert.getPriority().name().toLowerCase());
        dto.setStatus(alert.getStatus().name().toLowerCase());
        dto.setAddress(alert.getAddress());
        dto.setLatitude(alert.getLatitude());
        dto.setLongitude(alert.getLongitude());
        dto.setCreatedAt(alert.getCreatedAt());
        
        if (alert.getUser() != null) {
            dto.setUsername(alert.getUser().getUsername());
        }
        
        if (alert.getCountry() != null) {
            dto.setCountryName(alert.getCountry().getName());
        }
        
        if (alert.getCity() != null) {
            dto.setCityName(alert.getCity().getName());
        }
        
        return dto;
    }
}
