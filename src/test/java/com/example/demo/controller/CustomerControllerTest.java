package com.example.demo.controller;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.*;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ExcelProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ExcelProcessingService excelProcessingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer testCustomer;
    private CustomerDTO testCustomerDTO;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");
        testCustomer.setDob(LocalDate.of(1990, 1, 1));
        testCustomer.setNic("123456789V");
        testCustomer.setMobileNumbers(new HashSet<>(Arrays.asList("0712345678")));

        testCustomerDTO = new CustomerDTO();
        testCustomerDTO.setId(1L);
        testCustomerDTO.setName("John Doe");
        testCustomerDTO.setDob(LocalDate.of(1990, 1, 1));
        testCustomerDTO.setNic("123456789V");
        testCustomerDTO.setMobileNumbers(new HashSet<>(Arrays.asList("0712345678")));
    }

    @Test
    void testCreateCustomer() throws Exception {
        // Given
        when(customerService.existsByNic("123456789V")).thenReturn(false);
        when(customerService.save(any(Customer.class))).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.nic").value("123456789V"));

        verify(customerService, times(1)).existsByNic("123456789V");
        verify(customerService, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateCustomerWithExistingNic() throws Exception {
        // Given
        when(customerService.existsByNic("123456789V")).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).existsByNic("123456789V");
        verify(customerService, never()).save(any());
    }

    @Test
    void testGetCustomerById() throws Exception {
        // Given
        when(customerService.findByIdWithAllDetails(1L)).thenReturn(Optional.of(testCustomer));
        when(customerService.convertToDTO(testCustomer)).thenReturn(testCustomerDTO);

        // When & Then
        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.nic").value("123456789V"));

        verify(customerService, times(1)).findByIdWithAllDetails(1L);
        verify(customerService, times(1)).convertToDTO(testCustomer);
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        // Given
        when(customerService.findByIdWithAllDetails(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/customers/999"))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).findByIdWithAllDetails(999L);
        verify(customerService, never()).convertToDTO(any());
    }

    @Test
    void testGetAllCustomers() throws Exception {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        Page<Customer> customerPage = new PageImpl<>(customers);
        when(customerService.findAll(any())).thenReturn(customerPage);

        // When & Then
        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));

        verify(customerService, times(1)).findAll(any());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        // Given
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("John Updated");
        updatedCustomer.setDob(LocalDate.of(1990, 1, 1));
        updatedCustomer.setNic("123456789V");

        when(customerService.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerService.save(any(Customer.class))).thenReturn(updatedCustomer);

        // When & Then
        mockMvc.perform(put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"));

        verify(customerService, times(1)).findById(1L);
        verify(customerService, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomerNotFound() throws Exception {
        // Given
        when(customerService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/customers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).findById(999L);
        verify(customerService, never()).save(any());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        // Given
        when(customerService.findById(1L)).thenReturn(Optional.of(testCustomer));
        doNothing().when(customerService).deleteById(1L);

        // When & Then
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).findById(1L);
        verify(customerService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomerNotFound() throws Exception {
        // Given
        when(customerService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/customers/999"))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).findById(999L);
        verify(customerService, never()).deleteById(any());
    }

    @Test
    void testSearchCustomers() throws Exception {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        Page<Customer> customerPage = new PageImpl<>(customers);
        when(customerService.searchByNameAndNic(eq("John"), eq("123"), any())).thenReturn(customerPage);

        // When & Then
        mockMvc.perform(get("/customers/search")
                .param("name", "John")
                .param("nic", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));

        verify(customerService, times(1)).searchByNameAndNic(eq("John"), eq("123"), any());
    }

    @Test
    void testGetCustomerByNic() throws Exception {
        // Given
        when(customerService.findByNic("123456789V")).thenReturn(Optional.of(testCustomer));
        when(customerService.convertToDTO(testCustomer)).thenReturn(testCustomerDTO);

        // When & Then
        mockMvc.perform(get("/customers/nic/123456789V"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.nic").value("123456789V"));

        verify(customerService, times(1)).findByNic("123456789V");
        verify(customerService, times(1)).convertToDTO(testCustomer);
    }

    @Test
    void testGetCustomerByNicNotFound() throws Exception {
        // Given
        when(customerService.findByNic("999999999V")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/customers/nic/999999999V"))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).findByNic("999999999V");
        verify(customerService, never()).convertToDTO(any());
    }

    @Test
    void testBulkUpload() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "test content".getBytes()
        );

        ExcelProcessingService.BulkUploadResult result = new ExcelProcessingService.BulkUploadResult();
        result.setTotalRows(10);
        result.setSuccessCount(8);
        result.setErrorCount(2);
        result.setErrors(Arrays.asList("Row 3: Invalid date format", "Row 7: Missing NIC"));

        when(excelProcessingService.processBulkCustomerUpload(any(), eq(customerService))).thenReturn(result);

        // When & Then
        mockMvc.perform(multipart("/customers/bulk-upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRows").value(10))
                .andExpect(jsonPath("$.successCount").value(8))
                .andExpect(jsonPath("$.errorCount").value(2));

        verify(excelProcessingService, times(1)).processBulkCustomerUpload(any(), eq(customerService));
    }

    @Test
    void testBulkUploadInvalidFileType() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/customers/bulk-upload")
                .file(file))
                .andExpect(status().isBadRequest());

        verify(excelProcessingService, never()).processBulkCustomerUpload(any(), any());
    }

    @Test
    void testAddAddressToCustomer() throws Exception {
        // Given
        Address address = new Address();
        address.setAddressLine1("123 Main St");
        address.setAddressLine2("Apt 4B");

        when(customerService.addAddressToCustomer(eq(1L), any(Address.class))).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(post("/customers/1/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(customerService, times(1)).addAddressToCustomer(eq(1L), any(Address.class));
    }

    @Test
    void testAddFamilyMemberToCustomer() throws Exception {
        // Given
        FamilyMember familyMember = new FamilyMember();
        familyMember.setRelationship("Father");

        when(customerService.addFamilyMemberToCustomer(eq(1L), any(FamilyMember.class))).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(post("/customers/1/family-members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(familyMember)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(customerService, times(1)).addFamilyMemberToCustomer(eq(1L), any(FamilyMember.class));
    }

    @Test
    void testAddMobileNumberToCustomer() throws Exception {
        // Given
        when(customerService.addMobileNumberToCustomer(1L, "0734567890")).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(post("/customers/1/mobile-numbers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"0734567890\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(customerService, times(1)).addMobileNumberToCustomer(1L, "0734567890");
    }

    @Test
    void testRemoveAddressFromCustomer() throws Exception {
        // Given
        doNothing().when(customerService).removeAddressFromCustomer(1L);

        // When & Then
        mockMvc.perform(delete("/customers/addresses/1"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).removeAddressFromCustomer(1L);
    }

    @Test
    void testRemoveFamilyMemberFromCustomer() throws Exception {
        // Given
        doNothing().when(customerService).removeFamilyMemberFromCustomer(1L);

        // When & Then
        mockMvc.perform(delete("/customers/family-members/1"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).removeFamilyMemberFromCustomer(1L);
    }

    @Test
    void testRemoveMobileNumberFromCustomer() throws Exception {
        // Given
        when(customerService.removeMobileNumberFromCustomer(1L, "0712345678")).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(delete("/customers/1/mobile-numbers/0712345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(customerService, times(1)).removeMobileNumberFromCustomer(1L, "0712345678");
    }
}
