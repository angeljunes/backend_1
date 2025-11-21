package com.rcas.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDateTime;

@Document(collection = "cities")
public class City {
    
    @Id
    private String id;
    
    private String name;
    
    private LocalDateTime createdAt;
    
    @DBRef
    private Country country;
    
    // Referencia removida para evitar referencias circulares
    // Las alertas se consultan directamente desde AlertRepository
    
    // Constructores
    public City() {
        this.createdAt = LocalDateTime.now();
    }
    
    public City(String name, Country country) {
        this();
        this.name = name;
        this.country = country;
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
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
}
