package com.example.demo.repository;

import com.example.demo.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByCountryId(Long countryId);
    City findByNameAndCountryId(String name, Long countryId);
    City findByName(String name);
    
    @Query("SELECT c FROM City c WHERE c.name LIKE %:name%")
    List<City> findByNameContaining(@Param("name") String name);
    
    boolean existsByNameAndCountryId(String name, Long countryId);
}
