package com.example.demo.controller;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.*;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ExcelProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private ExcelProcessingService excelProcessingService;

    // Basic CRUD operations
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        // Normalize NIC for uniqueness check
        String normalizedNic = customer.getNic() != null ? customer.getNic().trim().toLowerCase() : null;
        if (normalizedNic == null || normalizedNic.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (service.existsByNic(normalizedNic)) {
            return ResponseEntity.badRequest().build();
        }
        customer.setNic(normalizedNic);

        // Convert mobileNumbers (if present) from string to MobileNumber entities
        java.util.Set<com.example.demo.entity.MobileNumber> mobileEntities = new java.util.HashSet<>();
        if (customer.getMobileNumbers() != null) {
            for (com.example.demo.entity.MobileNumber m : customer.getMobileNumbers()) {
                if (m.getNumber() != null && !m.getNumber().trim().isEmpty()) {
                    com.example.demo.entity.MobileNumber mobile = new com.example.demo.entity.MobileNumber();
                    mobile.setNumber(m.getNumber().trim());
                    mobile.setCustomer(customer);
                    mobileEntities.add(mobile);
                }
            }
        }
        customer.setMobileNumbers(mobileEntities);

        Customer savedCustomer = service.save(customer);
        return ResponseEntity.ok(savedCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        Optional<Customer> customer = service.findByIdWithAllDetails(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(service.convertToDTO(customer.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<Customer>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = service.findAll(pageable);
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        Optional<Customer> existingCustomer = service.findById(id);
        if (existingCustomer.isPresent()) {
            customer.setId(id);
            
            // Convert mobileNumbers (if present) from string to MobileNumber entities
            java.util.Set<com.example.demo.entity.MobileNumber> mobileEntities = new java.util.HashSet<>();
            if (customer.getMobileNumbers() != null) {
                for (com.example.demo.entity.MobileNumber m : customer.getMobileNumbers()) {
                    if (m.getNumber() != null && !m.getNumber().trim().isEmpty()) {
                        com.example.demo.entity.MobileNumber mobile = new com.example.demo.entity.MobileNumber();
                        mobile.setNumber(m.getNumber().trim());
                        mobile.setCustomer(customer);
                        mobileEntities.add(mobile);
                    }
                }
            }
            customer.setMobileNumbers(mobileEntities);
            
            Customer updatedCustomer = service.save(customer);
            return ResponseEntity.ok(updatedCustomer);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Search operations
    @GetMapping("/search")
    public ResponseEntity<Page<Customer>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nic,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers;
        
        if (name != null && nic != null) {
            customers = service.searchByNameAndNic(name, nic, pageable);
        } else if (name != null) {
            customers = service.searchByName(name, pageable);
        } else if (nic != null) {
            customers = service.searchByNic(nic, pageable);
        } else {
            customers = service.findAll(pageable);
        }
        
        return ResponseEntity.ok(customers);
    }

    // Customer specific operations
    @GetMapping("/nic/{nic}")
    public ResponseEntity<CustomerDTO> getByNic(@PathVariable String nic) {
        Optional<Customer> customer = service.findByNic(nic);
        if (customer.isPresent()) {
            return ResponseEntity.ok(service.convertToDTO(customer.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Bulk upload operations
    @PostMapping("/bulk-upload")
    public ResponseEntity<ExcelProcessingService.BulkUploadResult> bulkUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file type
            if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
                return ResponseEntity.badRequest().build();
            }
            
            ExcelProcessingService.BulkUploadResult result = excelProcessingService.processBulkCustomerUpload(file, service);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ExcelProcessingService.BulkUploadResult errorResult = new ExcelProcessingService.BulkUploadResult();
            errorResult.setErrorCount(1);
            errorResult.setErrors(List.of("Error processing file: " + e.getMessage()));
            return ResponseEntity.internalServerError().body(errorResult);
        }
    }

    // Address operations
    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<Customer> addAddress(@PathVariable Long customerId, @RequestBody Address address) {
        try {
            Customer customer = service.addAddressToCustomer(customerId, address);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> removeAddress(@PathVariable Long addressId) {
        service.removeAddressFromCustomer(addressId);
        return ResponseEntity.ok().build();
    }

    // Family member operations
    @PostMapping("/{customerId}/family-members")
    public ResponseEntity<Customer> addFamilyMember(@PathVariable Long customerId, @RequestBody FamilyMember familyMember) {
        try {
            Customer customer = service.addFamilyMemberToCustomer(customerId, familyMember);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/family-members/{familyMemberId}")
    public ResponseEntity<Void> removeFamilyMember(@PathVariable Long familyMemberId) {
        service.removeFamilyMemberFromCustomer(familyMemberId);
        return ResponseEntity.ok().build();
    }

    // Mobile number operations
    @PostMapping("/{customerId}/mobile-numbers")
    public ResponseEntity<Customer> addMobileNumber(@PathVariable Long customerId, @RequestBody String mobileNumber) {
        try {
            Customer customer = service.addMobileNumberToCustomer(customerId, mobileNumber);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{customerId}/mobile-numbers/{mobileNumber}")
    public ResponseEntity<Customer> removeMobileNumber(@PathVariable Long customerId, @PathVariable String mobileNumber) {
        try {
            Customer customer = service.removeMobileNumberFromCustomer(customerId, mobileNumber);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}