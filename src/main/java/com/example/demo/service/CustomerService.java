package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.FamilyMemberDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    // Basic CRUD operations
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByIdWithAllDetails(Long id) {
        return customerRepository.findByIdWithAllDetails(id);
    }

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    // Search operations
    public Page<Customer> searchByName(String name, Pageable pageable) {
        return customerRepository.findByNameContaining(name, pageable);
    }

    public Page<Customer> searchByNic(String nic, Pageable pageable) {
        return customerRepository.findByNicContaining(nic, pageable);
    }

    public Page<Customer> searchByNameAndNic(String name, String nic, Pageable pageable) {
        return customerRepository.findByNameAndNicContaining(name, nic, pageable);
    }

    // Customer specific operations
    public Optional<Customer> findByNic(String nic) {
        return customerRepository.findByNic(nic);
    }

    public boolean existsByNic(String nic) {
        if (nic == null) return false;
        String normalizedNic = nic.trim().toLowerCase();
        return customerRepository.existsByNic(normalizedNic);
    }

    // Address operations
    public Customer addAddressToCustomer(Long customerId, Address address) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            address.setCustomer(customer);
            addressRepository.save(address);
            return customer;
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    public void removeAddressFromCustomer(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    // Family member operations
    public Customer addFamilyMemberToCustomer(Long customerId, FamilyMember familyMember) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            familyMember.setCustomer(customer);
            familyMemberRepository.save(familyMember);
            return customer;
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    public void removeFamilyMemberFromCustomer(Long familyMemberId) {
        familyMemberRepository.deleteById(familyMemberId);
    }

    // Mobile number operations
    public Customer addMobileNumberToCustomer(Long customerId, String mobileNumber) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            MobileNumber mobile = new MobileNumber();
            mobile.setNumber(mobileNumber);
            mobile.setCustomer(customer);
            if (customer.getMobileNumbers() == null) {
                customer.setMobileNumbers(new java.util.HashSet<>());
            }
            customer.getMobileNumbers().add(mobile);
            return customerRepository.save(customer);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    public Customer removeMobileNumberFromCustomer(Long customerId, String mobileNumber) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (customer.getMobileNumbers() != null) {
                customer.getMobileNumbers().removeIf(m -> m.getNumber().equals(mobileNumber));
            }
            return customerRepository.save(customer);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    // DTO conversion methods
    public CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setDob(customer.getDob());
        dto.setNic(customer.getNic());
        
        // Convert mobile numbers to strings
        if (customer.getMobileNumbers() != null) {
            dto.setMobileNumbers(customer.getMobileNumbers().stream()
                .map(m -> m.getNumber())
                .collect(Collectors.toSet()));
        }
        
        if (customer.getAddresses() != null) {
            dto.setAddresses(customer.getAddresses().stream()
                .map(this::convertAddressToDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private AddressDTO convertAddressToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setAddressLine1(address.getLine1());
        dto.setAddressLine2(address.getLine2());
        if (address.getCity() != null) {
            dto.setCity(address.getCity().getName());
            if (address.getCity().getCountry() != null) {
                dto.setCountry(address.getCity().getCountry().getName());
            }
        }
        return dto;
    }

    private FamilyMemberDTO convertFamilyMemberToDTO(FamilyMember familyMember) {
        FamilyMemberDTO dto = new FamilyMemberDTO();
        dto.setId(familyMember.getId());
        if (familyMember.getFamilyMember() != null) {
            dto.setFamilyMemberName(familyMember.getFamilyMember().getName());
            dto.setFamilyMemberNic(familyMember.getFamilyMember().getNic());
        }
        dto.setRelationship(familyMember.getRelationship());
        return dto;
    }

    public Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setDob(dto.getDob());
        customer.setNic(dto.getNic());
        // Note: Mobile numbers are handled separately in the controller
        return customer;
    }
}