package com.example.demo.dto;

public class FamilyMemberDTO {
    private Long id;
    private String familyMemberName;
    private String familyMemberNic;
    private String relationship;

    // Constructors
    public FamilyMemberDTO() {}

    public FamilyMemberDTO(String familyMemberName, String familyMemberNic, String relationship) {
        this.familyMemberName = familyMemberName;
        this.familyMemberNic = familyMemberNic;
        this.relationship = relationship;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyMemberName() {
        return familyMemberName;
    }

    public void setFamilyMemberName(String familyMemberName) {
        this.familyMemberName = familyMemberName;
    }

    public String getFamilyMemberNic() {
        return familyMemberNic;
    }

    public void setFamilyMemberNic(String familyMemberNic) {
        this.familyMemberNic = familyMemberNic;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
