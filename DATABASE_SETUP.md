# Database Setup Guide

## 🚀 Quick Setup for Your Manager

### Option 1: Automatic Setup (Recommended)
The application will automatically create the database and sample data on first run.

### Option 2: Manual Setup

#### Step 1: Install MariaDB/MySQL
```bash
# Download and install MariaDB from https://mariadb.org/download/
# Or use MySQL from https://dev.mysql.com/downloads/mysql/
```

#### Step 2: Create Database
```sql
-- Connect to MariaDB/MySQL and run:
CREATE DATABASE customer_management_db;
```

#### Step 3: Update Application Properties (if needed)
The application is configured to work with:
- **Database**: customer_management_db
- **Host**: localhost:3306
- **Username**: root
- **Password**: 123

If your credentials are different, update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/customer_management_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### Step 4: Run the Application
```bash
# Backend
./mvnw spring-boot:run

# Frontend (in another terminal)
cd frontend-cms
npm start
```

## 📊 Sample Data

The application automatically creates sample data including:
- 5 Countries (Sri Lanka, India, USA, UK, Australia)
- 10 Cities (Colombo, Kandy, Mumbai, Delhi, New York, etc.)
- 10 Sample Customers with addresses and mobile numbers

## 🔍 Test the Application

1. **Access the application**: http://localhost:3000
2. **View existing customers**: Click "Customer List"
3. **Add new customer**: Click "Add Customer"
4. **Upload Excel file**: Click "Bulk Upload"

## 📝 Excel Upload Format

For bulk upload, use this format:
| Name | Date of Birth | NIC | Mobile 1 | Mobile 2 | Mobile 3 |
|------|---------------|-----|----------|----------|----------|
| John Doe | 1990-01-15 | 123456789V | 0771234567 | 0712345678 | 0753456789 |

## 🛠️ Troubleshooting

### Database Connection Issues
- Ensure MariaDB/MySQL is running
- Check database name, username, and password
- Verify firewall isn't blocking port 3306

### Application Won't Start
- Check Java version (requires Java 17+)
- Ensure Maven dependencies are downloaded
- Check for compilation errors

### Frontend Issues
- Ensure Node.js is installed (version 16+)
- Run `npm install` in frontend-cms directory
- Check port 3000 is not in use

## 📞 Support

For any issues, contact:
- **Developer**: Satheeshan
- **Email**: your-email@example.com
- **Phone**: 0744122354 (Oshadi)
