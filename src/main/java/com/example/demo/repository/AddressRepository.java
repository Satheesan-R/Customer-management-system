package com.example.demo.repository;

import com.example.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomerId(Long customerId);
    
    @Query("SELECT a FROM Address a WHERE a.customer.id = :customerId AND a.city.name LIKE %:cityName%")
    List<Address> findByCustomerIdAndCityNameContaining(@Param("customerId") Long customerId, @Param("cityName") String cityName);
    
    @Query("SELECT a FROM Address a JOIN FETCH a.city c JOIN FETCH c.country WHERE a.customer.id = :customerId")
    List<Address> findByCustomerIdWithDetails(@Param("customerId") Long customerId);
}
