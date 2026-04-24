package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "family_members")
public class FamilyMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_member_id", nullable = false)
    private Customer familyMember;
    
    @Column(nullable = false)
    private String relationship;
    
    // Constructors
    public FamilyMember() {}
    
    public FamilyMember(Customer customer, Customer familyMember, String relationship) {
        this.customer = customer;
        this.familyMember = familyMember;
        this.relationship = relationship;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Customer getFamilyMember() {
        return familyMember;
    }
    
    public void setFamilyMember(Customer familyMember) {
        this.familyMember = familyMember;
    }
    
    public String getRelationship() {
        return relationship;
    }
    
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
