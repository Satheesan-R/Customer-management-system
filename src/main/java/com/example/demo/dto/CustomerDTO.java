package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class CustomerDTO {
    private Long id;
    private String name;
    private LocalDate dob;
    private String nic;
    private Set<String> mobileNumbers;
    private List<AddressDTO> addresses;
    private List<FamilyMemberDTO> familyMembers;

    // Constructors
    public CustomerDTO() {}

    public CustomerDTO(String name, LocalDate dob, String nic) {
        this.name = name;
        this.dob = dob;
        this.nic = nic;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public Set<String> getMobileNumbers() {
        return mobileNumbers;
    }

    public void setMobileNumbers(Set<String> mobileNumbers) {
        this.mobileNumbers = mobileNumbers;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public List<FamilyMemberDTO> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FamilyMemberDTO> familyMembers) {
        this.familyMembers = familyMembers;
    }
}
