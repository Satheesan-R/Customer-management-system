package com.example.demo.repository;

import com.example.demo.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    List<FamilyMember> findByCustomerId(Long customerId);
    List<FamilyMember> findByFamilyMemberId(Long familyMemberId);
    
    @Query("SELECT fm FROM FamilyMember fm JOIN FETCH fm.customer JOIN FETCH fm.familyMember WHERE fm.customer.id = :customerId")
    List<FamilyMember> findByCustomerIdWithDetails(@Param("customerId") Long customerId);
    
    @Query("SELECT fm FROM FamilyMember fm WHERE fm.customer.id = :customerId AND fm.familyMember.id = :familyMemberId")
    FamilyMember findByCustomerAndFamilyMember(@Param("customerId") Long customerId, @Param("familyMemberId") Long familyMemberId);
}
