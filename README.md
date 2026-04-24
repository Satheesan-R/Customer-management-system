# Customer Management System

A comprehensive customer management system built with Spring Boot (Java 8+), React JS, MariaDB, and Maven. This system provides full CRUD operations for customers, including bulk upload functionality via Excel files.

## Features

### Core Functionality
- **Customer Management**: Create, Read, Update, Delete customers
- **Search & Filter**: Search customers by name, NIC, or both with pagination
- **Bulk Operations**: Upload up to 1M customer records via Excel files
- **Address Management**: Multiple addresses per customer with city/country master data
- **Mobile Numbers**: Multiple mobile numbers per customer
- **Family Relationships**: Link customers as family members with relationship types

### Technical Features
- **Optimized Database Calls**: Minimal DB queries with proper joins and fetching strategies
- **Memory-Efficient Processing**: Streaming Excel processing for large files
- **Comprehensive Testing**: JUnit tests for services and controllers
- **Responsive Design**: Mobile-friendly React frontend
- **RESTful API**: Well-structured REST endpoints with proper HTTP status codes

## Technology Stack

### Backend
- **Java 8+** (compatible with Java 17)
- **Spring Boot 4.0.5**
- **Spring Data JPA** with Hibernate
- **MariaDB** database
- **Apache POI** for Excel processing
- **JUnit 5** for testing
- **Maven** for dependency management

### Frontend
- **React JS**
- **Axios** for HTTP requests
- **CSS3** with responsive design
- **Modern JavaScript (ES6+)**

### Database
- **MariaDB 10.2+**
- **InnoDB** storage engine
- **Foreign key constraints**
- **Indexes for performance optimization**

## Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── service/             # Business logic
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── entity/              # JPA entities
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── config/              # Configuration classes
│   │   │   └── DemoApplication.java # Main application class
│   │   └── resources/
│   │       ├── application.properties # Spring Boot configuration
│   │       ├── schema.sql           # Database DDL script
│   │       └── data.sql             # Master data DML script
│   └── test/                        # JUnit tests
├── frontend-cms/                     # React frontend
│   ├── src/
│   │   ├── components/               # React components
│   │   ├── App.js                   # Main React component
│   │   └── App.css                  # Styling
│   ├── public/                      # Static assets
│   └── package.json                 # Node.js dependencies
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## Prerequisites

- **Java 8+** (recommended: Java 17)
- **Maven 3.6+**
- **Node.js 14+** and npm
- **MariaDB 10.2+**

## Setup Instructions

### 1. Database Setup

#### Install MariaDB
```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install mariadb-server

# macOS (using Homebrew)
brew install mariadb

# Windows
# Download from https://mariadb.org/download/
```

#### Create Database
```sql
-- Connect to MariaDB
mysql -u root -p

-- Create database
CREATE DATABASE customer_management_db;

-- Create user (optional)
CREATE USER 'customer_app'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON customer_management_db.* TO 'customer_app'@'localhost';
FLUSH PRIVILEGES;
```

#### Run Schema and Data Scripts
```bash
# Navigate to project directory
cd demo

# Execute schema script
mysql -u root -p customer_management_db < src/main/resources/schema.sql

# Execute data script (master data and sample data)
mysql -u root -p customer_management_db < src/main/resources/data.sql
```

### 2. Backend Setup

#### Configure Application Properties
Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.application.name=demo
spring.datasource.url=jdbc:mariadb://localhost:3306/customer_management_db
spring.datasource.username=root
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# File Upload Configuration
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# Server Configuration
server.port=8080
```

#### Build and Run Backend
```bash
# Navigate to project directory
cd demo

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run

# Or run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

#### Install Dependencies
```bash
# Navigate to frontend directory
cd frontend-cms

# Install npm dependencies
npm install
```

#### Run Frontend
```bash
# Start development server
npm start

# The frontend will start on http://localhost:3000
```

## API Endpoints

### Customer Management
- `GET /customers` - Get all customers (with pagination)
- `POST /customers` - Create new customer
- `GET /customers/{id}` - Get customer by ID
- `PUT /customers/{id}` - Update customer
- `DELETE /customers/{id}` - Delete customer
- `GET /customers/nic/{nic}` - Get customer by NIC
- `GET /customers/search` - Search customers (supports name, nic filters)

### Address Management
- `POST /customers/{customerId}/addresses` - Add address to customer
- `DELETE /customers/addresses/{addressId}` - Remove address

### Mobile Number Management
- `POST /customers/{customerId}/mobile-numbers` - Add mobile number
- `DELETE /customers/{customerId}/mobile-numbers/{mobileNumber}` - Remove mobile number

### Family Member Management
- `POST /customers/{customerId}/family-members` - Add family member
- `DELETE /customers/family-members/{familyMemberId}` - Remove family member

### Bulk Operations
- `POST /customers/bulk-upload` - Bulk upload customers via Excel file

## Database Schema

### Core Tables
- **customers**: Main customer entity
- **countries**: Master data for countries
- **cities**: Master data for cities linked to countries
- **customer_mobile_numbers**: Multiple mobile numbers per customer
- **addresses**: Customer addresses with city references
- **family_members**: Family relationships between customers

### Key Features
- **Foreign Key Constraints**: Data integrity enforced at database level
- **Indexes**: Optimized for common query patterns
- **Views**: Pre-defined views for complex queries
- **Stored Procedures**: Common database operations
- **Triggers**: Data validation and audit trails

## Excel Upload Format

### Required Columns (in order)
1. **Name** (mandatory) - Customer full name
2. **Date of Birth** (mandatory) - Format: YYYY-MM-DD, DD/MM/YYYY, or MM/DD/YYYY
3. **NIC** (mandatory) - Unique national identification number
4. **Mobile Numbers** (optional) - Additional columns for multiple numbers

### Example Excel Structure
```
| Name          | Date of Birth | NIC         | Mobile 1    | Mobile 2    |
|---------------|---------------|-------------|-------------|-------------|
| John Doe      | 1990-01-15    | 900123456V  | 0712345678  | 0723456789  |
| Jane Smith    | 15/01/1985    | 850987654V  | 0762345678  |             |
| Robert Johnson| 03/10/1992    | 920456789V  | 0745678901  | 0756789012  |
```

### Bulk Upload Features
- **Streaming Processing**: Handles files up to 1M records efficiently
- **Error Reporting**: Detailed error messages for failed rows
- **Batch Processing**: Processes in batches of 1000 records
- **Memory Optimization**: Minimal memory usage for large files
- **Progress Tracking**: Real-time upload progress

## Testing

### Backend Tests
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=CustomerServiceTest

# Run with coverage report
./mvnw test jacoco:report
```

### Frontend Tests
```bash
# Navigate to frontend directory
cd frontend-cms

# Run tests (if configured)
npm test
```

## Performance Considerations

### Database Optimization
- **Connection Pooling**: HikariCP configured by default
- **Query Optimization**: Lazy loading and proper join strategies
- **Indexing**: Strategic indexes on frequently queried columns
- **Batch Processing**: Bulk operations for better performance

### Memory Management
- **Excel Streaming**: Processes large files without loading entire file into memory
- **Pagination**: Limits data transfer between frontend and backend
- **Lazy Loading**: JPA entities loaded only when needed

### Caching Strategy
- **Application-level caching**: Consider Redis for frequently accessed data
- **Database caching**: MariaDB query cache enabled by default
- **Browser caching**: Static assets cached appropriately

## Security Considerations

### Data Validation
- **Input Validation**: Server-side validation for all inputs
- **NIC Format Validation**: Custom validation for Sri Lankan NIC format
- **File Upload Security**: File type and size restrictions

### Authentication & Authorization
- **CORS Configuration**: Properly configured for frontend access
- **Input Sanitization**: Prevent SQL injection and XSS attacks
- **Error Handling**: Generic error messages to prevent information leakage

## Deployment

### Production Deployment
```bash
# Build production JAR
./mvnw clean package -DskipTests

# Run with production profile
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker Deployment
```dockerfile
# Dockerfile example
FROM openjdk:17-jre-slim
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Frontend Build
```bash
# Navigate to frontend directory
cd frontend-cms

# Build for production
npm run build

# Deploy build/ directory to web server
```

## Troubleshooting

### Common Issues

#### Database Connection Issues
- Verify MariaDB is running
- Check database credentials in application.properties
- Ensure database exists and user has proper permissions

#### Excel Upload Issues
- Verify file format (.xlsx or .xls)
- Check file size limits (configured to 100MB)
- Ensure required columns are present and in correct order

#### Frontend Issues
- Clear browser cache and cookies
- Check browser console for JavaScript errors
- Verify backend API is accessible

### Log Analysis
```bash
# View application logs
tail -f logs/application.log

# Check for specific error patterns
grep "ERROR" logs/application.log
```

## Contributing

### Code Style
- Follow Java Code Conventions
- Use meaningful variable and method names
- Add proper Javadoc comments
- Write unit tests for new functionality

### Git Workflow
1. Create feature branch from main
2. Make changes with proper commit messages
3. Run tests and ensure they pass
4. Create pull request for review

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For technical support or questions:
- Check the troubleshooting section
- Review the API documentation
- Contact the development team

## Version History

- **v1.0.0** - Initial release with core functionality
- **v1.1.0** - Added bulk upload functionality
- **v1.2.0** - Enhanced search and filtering capabilities
- **v1.3.0** - Added family member relationships
- **v1.4.0** - Performance optimizations and bug fixes
