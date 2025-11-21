package com.rcas.dto;

public class CountryDTO {
    private String id;
    private String name;
    
    // Constructores
    public CountryDTO() {}
    
    public CountryDTO(String id, String name) {
        this.id = id;
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
}
