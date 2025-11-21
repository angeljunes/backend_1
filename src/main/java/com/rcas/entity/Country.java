package com.rcas.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "countries")
public class Country {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;
    
    private LocalDateTime createdAt;
    
    // Referencias removidas para evitar referencias circulares
    // Las ciudades y alertas se consultan directamente desde sus repositorios
    
    // Constructores
    public Country() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Country(String name) {
        this();
        this.name = name;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
