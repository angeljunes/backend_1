package com.rcas.repository;

import com.rcas.entity.City;
import com.rcas.entity.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends MongoRepository<City, String> {
    
    // Buscar ciudades por objeto Country completo
    List<City> findByCountryOrderByNameAsc(Country country);
    
    // Buscar ciudades por ID de país usando método derivado de Spring Data MongoDB
    // Spring Data MongoDB busca en el campo 'country' (que es un @DBRef) y accede a su 'id'
    List<City> findByCountry_IdOrderByNameAsc(String countryId);
    
    // Alias para compatibilidad con el controlador
    // Este método hace lo mismo que findByCountry_IdOrderByNameAsc
    default List<City> findByCountryIdOrderByNameAsc(String countryId) {
        return findByCountry_IdOrderByNameAsc(countryId);
    }
}
