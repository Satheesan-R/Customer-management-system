-- Customer Management System Database Schema
-- MariaDB DDL Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS customer_management_db;
USE customer_management_db;

-- Countries table (Master Data)
CREATE TABLE IF NOT EXISTS countries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(3) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Cities table (Master Data)
CREATE TABLE IF NOT EXISTS cities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (country_id) REFERENCES countries(id),
    UNIQUE KEY unique_city_country (name, country_id)
);

-- Customers table (Main Entity)
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    nic VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nic (nic),
    INDEX idx_name (name),
    INDEX idx_dob (dob)
);

-- Customer Mobile Numbers table (One-to-Many relationship)
CREATE TABLE IF NOT EXISTS customer_mobile_numbers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    mobile_number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    UNIQUE KEY unique_customer_mobile (customer_id, mobile_number),
    INDEX idx_mobile_number (mobile_number)
);

-- Addresses table (One-to-Many relationship)
CREATE TABLE IF NOT EXISTS addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address_line_1 VARCHAR(255) NOT NULL,
    address_line_2 VARCHAR(255),
    city_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (city_id) REFERENCES cities(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    INDEX idx_customer_address (customer_id),
    INDEX idx_city_address (city_id)
);

-- Family Members table (Many-to-Many relationship between customers)
CREATE TABLE IF NOT EXISTS family_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    family_member_id BIGINT NOT NULL,
    relationship VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (family_member_id) REFERENCES customers(id) ON DELETE CASCADE,
    UNIQUE KEY unique_family_relationship (customer_id, family_member_id),
    INDEX idx_customer_family (customer_id),
    INDEX idx_family_member (family_member_id),
    INDEX idx_relationship (relationship)
);

-- Add check constraints for data integrity
ALTER TABLE customers ADD CONSTRAINT chk_customer_name_not_empty CHECK (name <> '');
ALTER TABLE customers ADD CONSTRAINT chk_customer_nic_not_empty CHECK (nic <> '');
ALTER TABLE customers ADD CONSTRAINT chk_customer_dob_not_future CHECK (dob <= CURDATE());
ALTER TABLE customer_mobile_numbers ADD CONSTRAINT chk_mobile_not_empty CHECK (mobile_number <> '');
ALTER TABLE addresses ADD CONSTRAINT chk_address_line1_not_empty CHECK (address_line_1 <> '');
ALTER TABLE family_members ADD CONSTRAINT chk_relationship_not_empty CHECK (relationship <> '');

-- Prevent self-referencing family relationships
ALTER TABLE family_members ADD CONSTRAINT chk_no_self_reference CHECK (customer_id <> family_member_id);

-- Create indexes for better query performance
CREATE INDEX idx_customers_name_partial ON customers(name(20));
CREATE INDEX idx_customers_nic_partial ON customers(nic(20));
CREATE INDEX idx_addresses_line1_partial ON addresses(address_line_1(20));
CREATE INDEX idx_addresses_line2_partial ON addresses(address_line_2(20));
CREATE INDEX idx_family_members_relationship_partial ON family_members(relationship(20));

-- Create view for customer details with all related information
CREATE OR REPLACE VIEW customer_details AS
SELECT 
    c.id,
    c.name,
    c.dob,
    c.nic,
    c.created_at,
    c.updated_at,
    GROUP_CONCAT(DISTINCT cmn.mobile_number ORDER BY cmn.mobile_number SEPARATOR ', ') as mobile_numbers,
    COUNT(DISTINCT a.id) as address_count,
    COUNT(DISTINCT fm.id) as family_member_count
FROM customers c
LEFT JOIN customer_mobile_numbers cmn ON c.id = cmn.customer_id
LEFT JOIN addresses a ON c.id = a.customer_id
LEFT JOIN family_members fm ON c.id = fm.customer_id
GROUP BY c.id, c.name, c.dob, c.nic, c.created_at, c.updated_at;

-- Create view for customer addresses with city and country details
CREATE OR REPLACE VIEW customer_address_details AS
SELECT 
    a.id as address_id,
    a.address_line_1,
    a.address_line_2,
    a.created_at as address_created_at,
    c.id as customer_id,
    c.name as customer_name,
    c.nic as customer_nic,
    ci.id as city_id,
    ci.name as city_name,
    co.id as country_id,
    co.name as country_name,
    co.code as country_code
FROM addresses a
JOIN customers c ON a.customer_id = c.id
JOIN cities ci ON a.city_id = ci.id
JOIN countries co ON ci.country_id = co.id;

-- Create view for family member relationships
CREATE OR REPLACE VIEW family_member_details AS
SELECT 
    fm.id as family_member_id,
    fm.relationship,
    fm.created_at as relationship_created_at,
    c.id as customer_id,
    c.name as customer_name,
    c.nic as customer_nic,
    fm_c.id as family_member_customer_id,
    fm_c.name as family_member_name,
    fm_c.nic as family_member_nic
FROM family_members fm
JOIN customers c ON fm.customer_id = c.id
JOIN customers fm_c ON fm.family_member_id = fm_c.id;

-- Create stored procedure for customer search with filters
DELIMITER //
CREATE PROCEDURE search_customers(
    IN search_name VARCHAR(255),
    IN search_nic VARCHAR(50),
    IN page_offset INT,
    IN page_size INT
)
BEGIN
    SELECT 
        c.id,
        c.name,
        c.dob,
        c.nic,
        c.created_at,
        c.updated_at,
        GROUP_CONCAT(DISTINCT cmn.mobile_number ORDER BY cmn.mobile_number SEPARATOR ', ') as mobile_numbers
    FROM customers c
    LEFT JOIN customer_mobile_numbers cmn ON c.id = cmn.customer_id
    WHERE 
        (search_name IS NULL OR c.name LIKE CONCAT('%', search_name, '%'))
        AND (search_nic IS NULL OR c.nic LIKE CONCAT('%', search_nic, '%'))
    GROUP BY c.id, c.name, c.dob, c.nic, c.created_at, c.updated_at
    ORDER BY c.name, c.id
    LIMIT page_size OFFSET page_offset;
END //
DELIMITER ;

-- Create stored procedure for customer statistics
DELIMITER //
CREATE PROCEDURE get_customer_statistics()
BEGIN
    SELECT 
        COUNT(*) as total_customers,
        COUNT(DISTINCT cmn.customer_id) as customers_with_mobile_numbers,
        COUNT(DISTINCT a.customer_id) as customers_with_addresses,
        COUNT(DISTINCT fm.customer_id) as customers_with_family_members,
        COUNT(DISTINCT cmn.mobile_number) as total_mobile_numbers,
        COUNT(DISTINCT a.id) as total_addresses,
        COUNT(DISTINCT fm.id) as total_family_relationships
    FROM customers c
    LEFT JOIN customer_mobile_numbers cmn ON c.id = cmn.customer_id
    LEFT JOIN addresses a ON c.id = a.customer_id
    LEFT JOIN family_members fm ON c.id = fm.customer_id;
END //
DELIMITER ;

-- Create trigger for audit log (optional - for tracking changes)
DELIMITER //
CREATE TRIGGER customers_before_update 
BEFORE UPDATE ON customers
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER addresses_before_update 
BEFORE UPDATE ON addresses
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER family_members_before_update 
BEFORE UPDATE ON family_members
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END //
DELIMITER ;

-- Create full-text search index for better text search performance (if supported)
-- ALTER TABLE customers ADD FULLTEXT(name, nic);
-- ALTER TABLE addresses ADD FULLTEXT(address_line_1, address_line_2);

-- Performance optimization: Partition customers table by year (optional for very large datasets)
-- This would require more complex setup and is optional for most use cases

-- Comments for documentation
COMMENT ON DATABASE customer_management_db IS 'Customer Management System Database';
COMMENT ON TABLE countries IS 'Master data for countries';
COMMENT ON TABLE cities IS 'Master data for cities linked to countries';
COMMENT ON TABLE customers IS 'Main customer entity with basic information';
COMMENT ON TABLE customer_mobile_numbers IS 'Multiple mobile numbers per customer';
COMMENT ON TABLE addresses IS 'Multiple addresses per customer';
COMMENT ON TABLE family_members IS 'Family relationships between customers';
COMMENT ON VIEW customer_details IS 'Aggregated customer information with counts';
COMMENT ON VIEW customer_address_details IS 'Customer addresses with city and country details';
COMMENT ON VIEW family_member_details IS 'Family member relationships with customer details';
