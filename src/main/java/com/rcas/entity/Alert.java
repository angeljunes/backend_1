package com.rcas.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "alerts")
public class Alert {
    
    @Id
    private String id;
    
    private String title;
    
    private String description;
    
    private Priority priority = Priority.MEDIA;
    
    private Status status = Status.PENDIENTE;
    
    private String address;
    
    private BigDecimal latitude;
    
    private BigDecimal longitude;
    
    private LocalDateTime createdAt;
    
    @DBRef(lazy = true)
    private User user;
    
    @DBRef(lazy = true)
    private Country country;
    
    @DBRef(lazy = true)
    private City city;
    
    // Enums
    public enum Priority {
        ALTA, MEDIA, BAJA
    }
    
    public enum Status {
        PENDIENTE, VERIFICADA, RESUELTA
    }
    
    // Constructores
    public Alert() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Alert(String title, String description, Priority priority, BigDecimal latitude, BigDecimal longitude) {
        this();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
    
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
    }
}
