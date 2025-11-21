package com.rcas.repository;

import com.rcas.entity.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends MongoRepository<Country, String> {
    
    Optional<Country> findByName(String name);
    
    List<Country> findAllByOrderByNameAsc();
}
