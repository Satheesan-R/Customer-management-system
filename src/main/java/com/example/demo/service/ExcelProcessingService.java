package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.MobileNumber;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExcelProcessingService {

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("MM-dd-yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    public BulkUploadResult processBulkCustomerUpload(MultipartFile file, CustomerService customerService) {
        BulkUploadResult result = new BulkUploadResult();
        
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            Iterator<Row> rowIterator = sheet.iterator();
            int headerRowIndex = 0;
            
            // Skip header row if present
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                if (isHeaderRow(headerRow)) {
                    headerRowIndex = 1;
                } else {
                    // Reset iterator if first row is not header
                    sheet = workbook.getSheetAt(0);
                    rowIterator = sheet.iterator();
                }
            }
            
            AtomicInteger processedCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger errorCount = new AtomicInteger(0);
            List<String> errors = new ArrayList<>();
            
            // Process rows in batches to avoid memory issues
            List<Customer> batch = new ArrayList<>();
            final int BATCH_SIZE = 1000;
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                processedCount.incrementAndGet();
                
                try {
                    Customer customer = createCustomerFromRow(row);
                    if (customer != null) {
                        batch.add(customer);
                        
                        // Process batch when it reaches the batch size
                        if (batch.size() >= BATCH_SIZE) {
                            processBatch(batch, customerService, successCount, errorCount, errors);
                            batch.clear();
                        }
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                    errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
                
                // Prevent memory overflow for very large files
                if (processedCount.get() % 10000 == 0) {
                    System.gc(); // Suggest garbage collection
                }
            }
            
            // Process remaining batch
            if (!batch.isEmpty()) {
                processBatch(batch, customerService, successCount, errorCount, errors);
            }
            
            workbook.close();
            
            result.setTotalRows(processedCount.get());
            result.setSuccessCount(successCount.get());
            result.setErrorCount(errorCount.get());
            result.setErrors(errors);
            
        } catch (Exception e) {
            result.setErrorCount(1);
            result.setErrors(List.of("Error processing file: " + e.getMessage()));
        }
        
        return result;
    }
    
    private boolean isHeaderRow(Row row) {
        Cell firstCell = row.getCell(0);
        if (firstCell != null && firstCell.getCellType() == CellType.STRING) {
            String value = firstCell.getStringCellValue().toLowerCase();
            return value.contains("name") || value.contains("nic") || value.contains("dob");
        }
        return false;
    }
    
    private Customer createCustomerFromRow(Row row) throws Exception {
        String name = getCellValueAsString(row.getCell(0));
        String dobStr = getCellValueAsString(row.getCell(1));
        String nic = getCellValueAsString(row.getCell(2));

        // Validate mandatory fields
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Name is required");
        }
        if (dobStr == null || dobStr.trim().isEmpty()) {
            throw new Exception("Date of birth is required");
        }
        if (nic == null || nic.trim().isEmpty()) {
            throw new Exception("NIC is required");
        }

        // Parse date of birth
        LocalDate dob = parseDate(dobStr);
        if (dob == null) {
            throw new Exception("Invalid date format: " + dobStr);
        }

        Customer customer = new Customer();
        customer.setName(name.trim());
        customer.setDob(dob);
        customer.setNic(nic.trim());

        // Add mobile numbers from column 4 onwards (now required)
        boolean hasMobile = false;
        for (int i = 3; i < row.getLastCellNum(); i++) {
            String mobileNumber = getCellValueAsString(row.getCell(i));
            if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
                MobileNumber mobile = new MobileNumber();
                mobile.setNumber(mobileNumber.trim());
                customer.getMobileNumbers().add(mobile);
                hasMobile = true;
            }
        }
        if (!hasMobile) {
            throw new Exception("At least one mobile number is required");
        }

        return customer;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
    
    private LocalDate parseDate(String dateStr) {
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // Try next format
            }
        }
        return null;
    }
    
    private void processBatch(List<Customer> batch, CustomerService customerService, 
                             AtomicInteger successCount, AtomicInteger errorCount, List<String> errors) {
        for (Customer customer : batch) {
            try {
                if (!customerService.existsByNic(customer.getNic())) {
                    customerService.save(customer);
                    successCount.incrementAndGet();
                } else {
                    errorCount.incrementAndGet();
                    errors.add("Customer with NIC " + customer.getNic() + " already exists");
                }
            } catch (Exception e) {
                errorCount.incrementAndGet();
                errors.add("Error saving customer " + customer.getNic() + ": " + e.getMessage());
            }
        }
    }
    
    public static class BulkUploadResult {
        private int totalRows;
        private int successCount;
        private int errorCount;
        private List<String> errors;
        
        // Getters and Setters
        public int getTotalRows() {
            return totalRows;
        }
        
        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }
        
        public int getSuccessCount() {
            return successCount;
        }
        
        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }
        
        public int getErrorCount() {
            return errorCount;
        }
        
        public void setErrorCount(int errorCount) {
            this.errorCount = errorCount;
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public void setErrors(List<String> errors) {
            this.errors = errors;
        }
    }
}
