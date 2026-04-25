package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "line1", nullable = false)
    private String line1;
    
    @Column(name = "line2")
    private String line2;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    // Getters and setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getLine1() { 
        return line1; 
    }
    
    public void setLine1(String line1) { 
        this.line1 = line1; 
    }
    
    public String getLine2() { 
        return line2; 
    }
    
    public void setLine2(String line2) { 
        this.line2 = line2; 
    }
    
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
