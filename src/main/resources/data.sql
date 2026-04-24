-- Customer Management System Master Data Initialization
-- MariaDB DML Script

USE customer_management_db;

-- Insert Countries (Master Data)
INSERT IGNORE INTO countries (id, name, code) VALUES
(1, 'Sri Lanka', 'LK'),
(2, 'United States', 'US'),
(3, 'United Kingdom', 'GB'),
(4, 'Canada', 'CA'),
(5, 'Australia', 'AU'),
(6, 'India', 'IN'),
(7, 'Pakistan', 'PK'),
(8, 'Bangladesh', 'BD'),
(9, 'Malaysia', 'MY'),
(10, 'Singapore', 'SG'),
(11, 'United Arab Emirates', 'AE'),
(12, 'Saudi Arabia', 'SA'),
(13, 'Qatar', 'QA'),
(18, 'Kuwait', 'KW'),
(19, 'Oman', 'OM'),
(20, 'Bahrain', 'BH'),
(21, 'Japan', 'JP'),
(22, 'South Korea', 'KR'),
(23, 'China', 'CN'),
(24, 'Hong Kong', 'HK'),
(25, 'Germany', 'DE'),
(26, 'France', 'FR'),
(27, 'Italy', 'IT'),
(28, 'Spain', 'ES'),
(29, 'Netherlands', 'NL'),
(30, 'Belgium', 'BE'),
(31, 'Switzerland', 'CH'),
(32, 'Austria', 'AT'),
(33, 'Sweden', 'SE'),
(34, 'Norway', 'NO'),
(35, 'Denmark', 'DK'),
(36, 'Finland', 'FI'),
(37, 'New Zealand', 'NZ'),
(38, 'Thailand', 'TH'),
(39, 'Vietnam', 'VN'),
(40, 'Philippines', 'PH'),
(41, 'Indonesia', 'ID'),
(42, 'Russia', 'RU'),
(43, 'Brazil', 'BR'),
(44, 'Argentina', 'AR'),
(45, 'Mexico', 'MX'),
(46, 'South Africa', 'ZA'),
(47, 'Egypt', 'EG'),
(48, 'Nigeria', 'NG'),
(49, 'Kenya', 'KE'),
(50, 'Ghana', 'GH');

-- Insert Cities for Sri Lanka
INSERT IGNORE INTO cities (id, name, country_id) VALUES
-- Sri Lanka Cities
(1, 'Colombo', 1),
(2, 'Kandy', 1),
(3, 'Galle', 1),
(4, 'Jaffna', 1),
(5, 'Negombo', 1),
(6, 'Anuradhapura', 1),
(7, 'Trincomalee', 1),
(8, 'Batticaloa', 1),
(9, 'Matara', 1),
(10, 'Kurunegala', 1),
(11, 'Ratnapura', 1),
(12, 'Badulla', 1),
(13, 'Monaragala', 1),
(14, 'Ampara', 1),
(15, 'Puttalam', 1),
(16, 'Chilaw', 1),
(17, 'Kalutara', 1),
(18, 'Gampaha', 1),
(19, 'Nuwara Eliya', 1),
(20, 'Polonnaruwa', 1),

-- United States Cities
(21, 'New York', 2),
(22, 'Los Angeles', 2),
(23, 'Chicago', 2),
(24, 'Houston', 2),
(25, 'Phoenix', 2),
(26, 'Philadelphia', 2),
(27, 'San Antonio', 2),
(28, 'San Diego', 2),
(29, 'Dallas', 2),
(30, 'San Jose', 2),

-- United Kingdom Cities
(31, 'London', 3),
(32, 'Manchester', 3),
(33, 'Birmingham', 3),
(34, 'Liverpool', 3),
(35, 'Bristol', 3),
(36, 'Edinburgh', 3),
(37, 'Glasgow', 3),
(38, 'Leeds', 3),
(39, 'Sheffield', 3),
(40, 'Cardiff', 3),

-- Canada Cities
(41, 'Toronto', 4),
(42, 'Montreal', 4),
(43, 'Vancouver', 4),
(44, 'Calgary', 4),
(45, 'Edmonton', 4),
(46, 'Ottawa', 4),
(47, 'Winnipeg', 4),
(48, 'Quebec City', 4),
(49, 'Hamilton', 4),
(50, 'Halifax', 4),

-- Australia Cities
(51, 'Sydney', 5),
(52, 'Melbourne', 5),
(53, 'Brisbane', 5),
(54, 'Perth', 5),
(55, 'Adelaide', 5),
(56, 'Gold Coast', 5),
(57, 'Newcastle', 5),
(58, 'Canberra', 5),
(59, 'Wollongong', 5),
(60, 'Geelong', 5),

-- India Cities
(61, 'Mumbai', 6),
(62, 'Delhi', 6),
(63, 'Bangalore', 6),
(64, 'Chennai', 6),
(65, 'Kolkata', 6),
(66, 'Hyderabad', 6),
(67, 'Pune', 6),
(68, 'Ahmedabad', 6),
(69, 'Jaipur', 6),
(70, 'Lucknow', 6),

-- Pakistan Cities
(71, 'Karachi', 7),
(72, 'Lahore', 7),
(73, 'Faisalabad', 7),
(74, 'Rawalpindi', 7),
(75, 'Multan', 7),
(76, 'Hyderabad', 7),
(77, 'Gujranwala', 7),
(78, 'Peshawar', 7),
(79, 'Quetta', 7),
(80, 'Islamabad', 7),

-- Bangladesh Cities
(81, 'Dhaka', 8),
(82, 'Chittagong', 8),
(83, 'Khulna', 8),
(84, 'Rajshahi', 8),
(85, 'Sylhet', 8),
(86, 'Barisal', 8),
(87, 'Rangpur', 8),
(88, 'Mymensingh', 8),
(89, 'Comilla', 8),
(90, 'Narayanganj', 8),

-- Malaysia Cities
(91, 'Kuala Lumpur', 9),
(92, 'George Town', 9),
(93, 'Ipoh', 9),
(94, 'Shah Alam', 9),
(95, 'Petaling Jaya', 9),
(96, 'Johor Bahru', 9),
(97, 'Seremban', 9),
(98, 'Kuching', 9),
(99, 'Kota Kinabalu', 9),
(100, 'Klang', 9),

-- Singapore (City-State)
(101, 'Singapore', 10),

-- United Arab Emirates Cities
(102, 'Dubai', 11),
(103, 'Abu Dhabi', 11),
(104, 'Sharjah', 11),
(105, 'Al Ain', 11),
(106, 'Ajman', 11),
(107, 'Ras Al Khaimah', 11),
(108, 'Fujairah', 11),
(109, 'Umm Al Quwain', 11),

-- Saudi Arabia Cities
(110, 'Riyadh', 12),
(111, 'Jeddah', 12),
(112, 'Mecca', 12),
(113, 'Medina', 12),
(114, 'Dammam', 12),
(115, 'Khobar', 12),
(116, 'Tabuk', 12),
(117, 'Buraidah', 12),
(118, 'Hail', 12),
(119, 'Najran', 12),

-- Qatar Cities
(120, 'Doha', 13),
(121, 'Al Rayyan', 13),
(122, 'Umm Salal', 13),
(123, 'Al Khor', 13),
(124, 'Al Wakrah', 13),
(125, 'Al Shamal', 13),
(126, 'Dukhan', 13),
(127, 'Zubarah', 13),

-- Japan Cities
(128, 'Tokyo', 21),
(129, 'Osaka', 21),
(130, 'Kyoto', 21),
(131, 'Yokohama', 21),
(132, 'Nagoya', 21),
(133, 'Sapporo', 21),
(134, 'Kobe', 21),
(135, 'Fukuoka', 21),
(136, 'Kawasaki', 21),
(137, 'Saitama', 21),

-- South Korea Cities
(138, 'Seoul', 22),
(139, 'Busan', 22),
(140, 'Incheon', 22),
(141, 'Daegu', 22),
(142, 'Daejeon', 22),
(143, 'Gwangju', 22),
(144, 'Suwon', 22),
(145, 'Ulsan', 22),
(146, 'Changwon', 22),
(147, 'Goyang', 22),

-- China Cities
(148, 'Beijing', 23),
(149, 'Shanghai', 23),
(150, 'Guangzhou', 23),
(151, 'Shenzhen', 23),
(152, 'Chongqing', 23),
(153, 'Tianjin', 23),
(154, 'Wuhan', 23),
(155, 'Dongguan', 23),
(156, 'Shenyang', 23),
(157, 'Hangzhou', 23),

-- Hong Kong Cities
(158, 'Hong Kong', 24),
(159, 'Kowloon', 24),
(160, 'New Territories', 24),

-- Germany Cities
(161, 'Berlin', 25),
(162, 'Munich', 25),
(163, 'Hamburg', 25),
(164, 'Frankfurt', 25),
(165, 'Cologne', 25),
(166, 'Stuttgart', 25),
(167, 'Düsseldorf', 25),
(168, 'Dortmund', 25),
(169, 'Leipzig', 25),
(170, 'Dresden', 25),

-- France Cities
(171, 'Paris', 26),
(172, 'Marseille', 26),
(173, 'Lyon', 26),
(174, 'Toulouse', 26),
(175, 'Nice', 26),
(176, 'Nantes', 26),
(177, 'Strasbourg', 26),
(178, 'Montpellier', 26),
(179, 'Bordeaux', 26),
(180, 'Lille', 26);

-- Insert sample customers for testing
INSERT IGNORE INTO customers (id, name, dob, nic) VALUES
(1, 'John Doe', '1990-01-15', '900123456V'),
(2, 'Jane Smith', '1985-05-22', '850987654V'),
(3, 'Robert Johnson', '1992-03-10', '920456789V'),
(4, 'Emily Davis', '1988-07-08', '880789123V'),
(5, 'Michael Wilson', '1995-11-25', '950321654V');

-- Insert sample mobile numbers
INSERT IGNORE INTO customer_mobile_numbers (customer_id, mobile_number) VALUES
(1, '0712345678'),
(1, '0723456789'),
(2, '0762345678'),
(3, '0745678901'),
(3, '0756789012'),
(3, '0778901234'),
(4, '0734567890'),
(5, '0789012345'),
(5, '0790123456');

-- Insert sample addresses
INSERT IGNORE INTO addresses (id, address_line_1, address_line_2, city_id, customer_id) VALUES
(1, '123 Main Street', 'Apt 4B', 1, 1),
(2, '456 Park Avenue', 'Suite 200', 21, 2),
(3, '789 Beach Road', NULL, 3, 3),
(4, '321 Hill Street', 'Floor 5', 2, 4),
(5, '654 Garden Lane', 'House 12', 1, 5);

-- Insert sample family relationships
INSERT IGNORE INTO family_members (id, customer_id, family_member_id, relationship) VALUES
(1, 1, 2, 'Spouse'),
(2, 1, 3, 'Brother'),
(3, 2, 1, 'Spouse'),
(4, 3, 1, 'Brother'),
(5, 4, 5, 'Sister'),
(6, 5, 4, 'Brother');

-- Create database user for application (optional - adjust as needed)
-- CREATE USER IF NOT EXISTS 'customer_app'@'localhost' IDENTIFIED BY 'secure_password';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON customer_management_db.* TO 'customer_app'@'localhost';
-- FLUSH PRIVILEGES;

-- Create indexes for better performance on frequently queried columns
CREATE INDEX IF NOT EXISTS idx_customers_name_dob ON customers(name, dob);
CREATE INDEX IF NOT EXISTS idx_customers_nic_name ON customers(nic, name);
CREATE INDEX IF NOT EXISTS idx_addresses_customer_city ON addresses(customer_id, city_id);
CREATE INDEX IF NOT EXISTS idx_family_members_customer_member ON family_members(customer_id, family_member_id);

-- Create full-text search indexes for better search performance (if supported)
-- ALTER TABLE customers ADD FULLTEXT INDEX ft_customers_name (name);
-- ALTER TABLE customers ADD FULLTEXT INDEX ft_customers_nic (nic);
-- ALTER TABLE addresses ADD FULLTEXT INDEX ft_addresses (address_line_1, address_line_2);

-- Create procedures for common operations
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS get_customer_by_nic(IN customer_nic VARCHAR(50))
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
    WHERE c.nic = customer_nic
    GROUP BY c.id, c.name, c.dob, c.nic, c.created_at, c.updated_at;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE IF NOT EXISTS get_customer_addresses(IN customer_id BIGINT)
BEGIN
    SELECT 
        a.id as address_id,
        a.address_line_1,
        a.address_line_2,
        ci.name as city_name,
        co.name as country_name,
        co.code as country_code,
        a.created_at
    FROM addresses a
    JOIN cities ci ON a.city_id = ci.id
    JOIN countries co ON ci.country_id = co.id
    WHERE a.customer_id = customer_id
    ORDER BY a.created_at DESC;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE IF NOT EXISTS get_customer_family_members(IN customer_id BIGINT)
BEGIN
    SELECT 
        fm.id as family_member_id,
        fm.relationship,
        fm_c.id as family_member_customer_id,
        fm_c.name as family_member_name,
        fm_c.nic as family_member_nic,
        fm_c.dob as family_member_dob,
        fm.created_at
    FROM family_members fm
    JOIN customers fm_c ON fm.family_member_id = fm_c.id
    WHERE fm.customer_id = customer_id
    ORDER BY fm.relationship, fm_c.name;
END //
DELIMITER ;

-- Create function to validate NIC format (Sri Lankan format)
DELIMITER //
CREATE FUNCTION IF NOT EXISTS is_valid_sri_lankan_nic(nic VARCHAR(50))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE is_valid BOOLEAN DEFAULT FALSE;
    
    -- Check for old format (9 digits + V) or new format (12 digits)
    IF nic REGEXP '^[0-9]{9}[vV]$' OR nic REGEXP '^[0-9]{12}$' THEN
        SET is_valid = TRUE;
    END IF;
    
    RETURN is_valid;
END //
DELIMITER ;

-- Create function to calculate age from date of birth
DELIMITER //
CREATE FUNCTION IF NOT EXISTS calculate_age(dob DATE)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE age INT;
    
    SET age = TIMESTAMPDIFF(YEAR, dob, CURDATE());
    
    -- Adjust if birthday hasn't occurred yet this year
    IF DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(dob, '%m%d') THEN
        SET age = age - 1;
    END IF;
    
    RETURN age;
END //
DELIMITER ;

-- Create trigger to validate NIC before insert
DELIMITER //
CREATE TRIGGER IF NOT EXISTS validate_customer_nic_before_insert
BEFORE INSERT ON customers
FOR EACH ROW
BEGIN
    IF NOT is_valid_sri_lankan_nic(NEW.nic) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid NIC format';
    END IF;
END //
DELIMITER ;

-- Create trigger to validate NIC before update
DELIMITER //
CREATE TRIGGER IF NOT EXISTS validate_customer_nic_before_update
BEFORE UPDATE ON customers
FOR EACH ROW
BEGIN
    IF NEW.nic <> OLD.nic AND NOT is_valid_sri_lankan_nic(NEW.nic) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid NIC format';
    END IF;
END //
DELIMITER ;

-- Create view for customer statistics
CREATE OR REPLACE VIEW customer_statistics AS
SELECT 
    COUNT(*) as total_customers,
    COUNT(DISTINCT CASE WHEN cmn.customer_id IS NOT NULL THEN c.id END) as customers_with_mobile_numbers,
    COUNT(DISTINCT CASE WHEN a.customer_id IS NOT NULL THEN c.id END) as customers_with_addresses,
    COUNT(DISTINCT CASE WHEN fm.customer_id IS NOT NULL THEN c.id END) as customers_with_family_members,
    AVG(calculate_age(c.dob)) as average_age,
    MIN(c.dob) as oldest_customer_dob,
    MAX(c.dob) as youngest_customer_dob
FROM customers c
LEFT JOIN customer_mobile_numbers cmn ON c.id = cmn.customer_id
LEFT JOIN addresses a ON c.id = a.customer_id
LEFT JOIN family_members fm ON c.id = fm.customer_id;

-- Create view for city statistics
CREATE OR REPLACE VIEW city_statistics AS
SELECT 
    ci.id as city_id,
    ci.name as city_name,
    co.name as country_name,
    co.code as country_code,
    COUNT(DISTINCT a.customer_id) as customer_count,
    COUNT(a.id) as address_count
FROM cities ci
LEFT JOIN countries co ON ci.country_id = co.id
LEFT JOIN addresses a ON ci.id = a.city_id
GROUP BY ci.id, ci.name, co.name, co.code
ORDER BY customer_count DESC;

-- Create view for country statistics
CREATE OR REPLACE VIEW country_statistics AS
SELECT 
    co.id as country_id,
    co.name as country_name,
    co.code as country_code,
    COUNT(DISTINCT ci.id) as city_count,
    COUNT(DISTINCT a.customer_id) as customer_count,
    COUNT(a.id) as address_count
FROM countries co
LEFT JOIN cities ci ON co.id = ci.country_id
LEFT JOIN addresses a ON ci.id = a.city_id
GROUP BY co.id, co.name, co.code
ORDER BY customer_count DESC;

-- Create event to clean up old audit logs (if audit tables are added in future)
-- CREATE EVENT IF NOT EXISTS cleanup_old_audit_logs
-- ON SCHEDULE EVERY 1 MONTH
-- DO
--     DELETE FROM audit_log WHERE created_at < DATE_SUB(NOW(), INTERVAL 1 YEAR);

-- Set up initial database configuration
SET GLOBAL innodb_file_per_table = ON;
SET GLOBAL innodb_file_format = Barracuda;
SET GLOBAL innodb_large_prefix = ON;

-- Add comments for documentation
ALTER TABLE countries COMMENT = 'Master data for countries with ISO codes';
ALTER TABLE cities COMMENT = 'Master data for cities linked to countries';
ALTER TABLE customers COMMENT = 'Main customer entity with personal information';
ALTER TABLE customer_mobile_numbers COMMENT 'Multiple mobile numbers per customer';
ALTER TABLE addresses COMMENT 'Customer addresses with city and country references';
ALTER TABLE family_members COMMENT 'Family relationships between customers';

-- Final verification queries
SELECT 'Database setup completed successfully' as status;
SELECT COUNT(*) as countries_inserted FROM countries;
SELECT COUNT(*) as cities_inserted FROM cities;
SELECT COUNT(*) as sample_customers_inserted FROM customers;
SELECT COUNT(*) as sample_mobile_numbers_inserted FROM customer_mobile_numbers;
SELECT COUNT(*) as sample_addresses_inserted FROM addresses;
SELECT COUNT(*) as sample_family_relationships_inserted FROM family_members;
