package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private FamilyMemberRepository familyMemberRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private Address testAddress;
    private FamilyMember testFamilyMember;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");
        testCustomer.setDob(LocalDate.of(1990, 1, 1));
        testCustomer.setNic("123456789V");
        testCustomer.setMobileNumbers(new HashSet<>(Arrays.asList("0712345678", "0723456789")));

        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setAddressLine1("123 Main St");
        testAddress.setAddressLine2("Apt 4B");
        testAddress.setCustomer(testCustomer);

        testFamilyMember = new FamilyMember();
        testFamilyMember.setId(1L);
        testFamilyMember.setCustomer(testCustomer);
        testFamilyMember.setFamilyMember(testCustomer);
        testFamilyMember.setRelationship("Father");
    }

    @Test
    void testSaveCustomer() {
        // Given
        Customer newCustomer = new Customer();
        newCustomer.setName("Jane Doe");
        newCustomer.setDob(LocalDate.of(1995, 5, 15));
        newCustomer.setNic("987654321V");

        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        // When
        Customer result = customerService.save(newCustomer);

        // Then
        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("987654321V", result.getNic());
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    void testFindById() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // When
        Optional<Customer> result = customerService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testCustomer.getName(), result.get().getName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Customer> result = customerService.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(999L);
    }

    @Test
    void testFindAll() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        Page<Customer> customerPage = new PageImpl<>(customers);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);

        // When
        Page<Customer> result = customerService.findAll(Pageable.unpaged());

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testCustomer.getName(), result.getContent().get(0).getName());
        verify(customerRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testDeleteById() {
        // Given
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        // When
        customerService.deleteById(1L);

        // Then
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSearchByName() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        Page<Customer> customerPage = new PageImpl<>(customers);
        when(customerRepository.findByNameContaining(eq("John"), any(Pageable.class))).thenReturn(customerPage);

        // When
        Page<Customer> result = customerService.searchByName("John", Pageable.unpaged());

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testCustomer.getName(), result.getContent().get(0).getName());
        verify(customerRepository, times(1)).findByNameContaining(eq("John"), any(Pageable.class));
    }

    @Test
    void testSearchByNic() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        Page<Customer> customerPage = new PageImpl<>(customers);
        when(customerRepository.findByNicContaining(eq("123"), any(Pageable.class))).thenReturn(customerPage);

        // When
        Page<Customer> result = customerService.searchByNic("123", Pageable.unpaged());

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testCustomer.getNic(), result.getContent().get(0).getNic());
        verify(customerRepository, times(1)).findByNicContaining(eq("123"), any(Pageable.class));
    }

    @Test
    void testFindByNic() {
        // Given
        when(customerRepository.findByNic("123456789V")).thenReturn(Optional.of(testCustomer));

        // When
        Optional<Customer> result = customerService.findByNic("123456789V");

        // Then
        assertTrue(result.isPresent());
        assertEquals(testCustomer.getNic(), result.get().getNic());
        verify(customerRepository, times(1)).findByNic("123456789V");
    }

    @Test
    void testExistsByNic() {
        // Given
        when(customerRepository.existsByNic("123456789V")).thenReturn(true);

        // When
        boolean result = customerService.existsByNic("123456789V");

        // Then
        assertTrue(result);
        verify(customerRepository, times(1)).existsByNic("123456789V");
    }

    @Test
    void testAddAddressToCustomer() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        // When
        Customer result = customerService.addAddressToCustomer(1L, testAddress);

        // Then
        assertNotNull(result);
        assertEquals(testCustomer.getName(), result.getName());
        verify(customerRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(testAddress);
    }

    @Test
    void testAddAddressToCustomerNotFound() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.addAddressToCustomer(999L, testAddress);
        });
        verify(customerRepository, times(1)).findById(999L);
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testAddFamilyMemberToCustomer() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(familyMemberRepository.save(any(FamilyMember.class))).thenReturn(testFamilyMember);

        // When
        Customer result = customerService.addFamilyMemberToCustomer(1L, testFamilyMember);

        // Then
        assertNotNull(result);
        assertEquals(testCustomer.getName(), result.getName());
        verify(customerRepository, times(1)).findById(1L);
        verify(familyMemberRepository, times(1)).save(testFamilyMember);
    }

    @Test
    void testAddMobileNumberToCustomer() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        Customer result = customerService.addMobileNumberToCustomer(1L, "0734567890");

        // Then
        assertNotNull(result);
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testConvertToDTO() {
        // Given
        testCustomer.setAddresses(Arrays.asList(testAddress));
        testCustomer.setFamilyMembers(Arrays.asList(testFamilyMember));

        // When
        CustomerDTO result = customerService.convertToDTO(testCustomer);

        // Then
        assertNotNull(result);
        assertEquals(testCustomer.getId(), result.getId());
        assertEquals(testCustomer.getName(), result.getName());
        assertEquals(testCustomer.getNic(), result.getNic());
        assertEquals(testCustomer.getDob(), result.getDob());
        assertEquals(testCustomer.getMobileNumbers(), result.getMobileNumbers());
        assertNotNull(result.getAddresses());
        assertNotNull(result.getFamilyMembers());
    }

    @Test
    void testConvertToEntity() {
        // Given
        CustomerDTO dto = new CustomerDTO();
        dto.setId(1L);
        dto.setName("John Doe");
        dto.setDob(LocalDate.of(1990, 1, 1));
        dto.setNic("123456789V");
        dto.setMobileNumbers(new HashSet<>(Arrays.asList("0712345678")));

        // When
        Customer result = customerService.convertToEntity(dto);

        // Then
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getNic(), result.getNic());
        assertEquals(dto.getDob(), result.getDob());
        assertEquals(dto.getMobileNumbers(), result.getMobileNumbers());
    }
}
