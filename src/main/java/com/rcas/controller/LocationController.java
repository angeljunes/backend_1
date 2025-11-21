package com.rcas.controller;

import com.rcas.dto.CityDTO;
import com.rcas.dto.CountryDTO;
import com.rcas.entity.City;
import com.rcas.entity.Country;
import com.rcas.repository.CityRepository;
import com.rcas.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "*")
public class LocationController {
    
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        List<Country> countries = countryRepository.findAllByOrderByNameAsc();
        List<CountryDTO> countryDTOs = countries.stream()
                .map(country -> new CountryDTO(country.getId(), country.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(countryDTOs);
    }
    
    @GetMapping("/cities/{countryId}")
    public ResponseEntity<List<CityDTO>> getCitiesByCountry(@PathVariable String countryId) {
        List<City> cities = cityRepository.findByCountryIdOrderByNameAsc(countryId);
        List<CityDTO> cityDTOs = cities.stream()
                .map(city -> {
                    String countryIdValue = city.getCountry() != null ? city.getCountry().getId() : null;
                    String countryNameValue = city.getCountry() != null ? city.getCountry().getName() : null;
                    return new CityDTO(
                        city.getId(), 
                        city.getName(), 
                        countryIdValue, 
                        countryNameValue
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(cityDTOs);
    }
    
    @GetMapping("/countries/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable String id) {
        return countryRepository.findById(id)
                .map(country -> ResponseEntity.ok(country))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cities/detail/{id}")
    public ResponseEntity<City> getCityById(@PathVariable String id) {
        return cityRepository.findById(id)
                .map(city -> ResponseEntity.ok(city))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/debug/cities-count/{countryId}")
    public ResponseEntity<String> getCitiesCount(@PathVariable String countryId) {
        List<City> cities = cityRepository.findByCountryIdOrderByNameAsc(countryId);
        return ResponseEntity.ok("País ID " + countryId + " tiene " + cities.size() + " ciudades");
    }
    
    @GetMapping("/debug/all-cities")
    public ResponseEntity<String> getAllCitiesCount() {
        List<City> allCities = cityRepository.findAll();
        long totalCities = allCities.size();
        StringBuilder result = new StringBuilder("Total de ciudades en BD: " + totalCities + "\n");
        
        // Agrupar por país
        allCities.stream()
            .filter(city -> city.getCountry() != null)
            .collect(java.util.stream.Collectors.groupingBy(
                city -> city.getCountry().getName(),
                java.util.stream.Collectors.counting()
            ))
            .forEach((countryName, count) -> 
                result.append(countryName).append(": ").append(count).append(" ciudades\n")
            );
            
        return ResponseEntity.ok(result.toString());
    }
}
