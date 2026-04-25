package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByNic(String nic);
    
    boolean existsByNic(String nic);
    
    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name%")
    Page<Customer> findByNameContaining(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.nic LIKE %:nic%")
    Page<Customer> findByNicContaining(@Param("nic") String nic, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name% AND c.nic LIKE %:nic%")
    Page<Customer> findByNameAndNicContaining(@Param("name") String name, @Param("nic") String nic, Pageable pageable);
    
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.addresses LEFT JOIN FETCH c.mobileNumbers WHERE c.id = :id")
    Optional<Customer> findByIdWithAllDetails(@Param("id") Long id);
    
    @Query("SELECT COUNT(c) FROM Customer c")
    long countTotalCustomers();
}