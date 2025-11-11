# Library Management System - Setup Guide

This guide will walk you through the complete setup process for both the web and desktop applications.

## 📋 Prerequisites Checklist

Before starting, ensure you have the following installed:

- [ ] **Java Development Kit (JDK) 11 or higher**
  - Download from: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
  - Verify: `java -version` and `javac -version`

- [ ] **Apache Maven 3.6+**
  - Download from: https://maven.apache.org/download.cgi
  - Verify: `mvn -version`

- [ ] **MySQL Server 8.x**
  - Download from: https://dev.mysql.com/downloads/mysql/
  - Verify: `mysql --version`

- [ ] **Apache Tomcat 9.x** (for web application)
  - Download from: https://tomcat.apache.org/download-90.cgi

## 🗄️ Step 1: Database Setup

### 1.1 Start MySQL Server

**Windows:**
```cmd
net start MySQL80
```

**Linux/Mac:**
```bash
sudo systemctl start mysql
# or
sudo service mysql start
```

### 1.2 Create Database and Import Schema

```bash
# Login to MySQL
mysql -u root -p

# Execute the schema file
source database/schema.sql

# Or manually copy-paste the SQL content
```

Alternatively, you can use MySQL Workbench:
1. Open MySQL Workbench
2. Connect to your MySQL server
3. Open `database/schema.sql`
4. Execute the script

### 1.3 Verify Database Creation

```sql
USE library_management;
SHOW TABLES;
SELECT * FROM users;
```

You should see 5 tables and 1 admin user.

## ⚙️ Step 2: Configure Database Connection

Edit the file: `src/main/java/com/library/util/DatabaseConnection.java`

Update the following lines with your MySQL credentials:

```java
private static final String URL = "jdbc:mysql://localhost:3306/library_management?useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_mysql_password";
```

## 🔨 Step 3: Build the Project

Open a terminal in the project root directory and run:

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the source code
- Run tests (if any)
- Package the application into a WAR file

Expected output location: `target/library-management.war`

## 🌐 Step 4: Deploy Web Application

### Option A: Using Apache Tomcat

#### 4.1 Configure Tomcat

1. Download and extract Apache Tomcat 9.x
2. Set environment variables:
   - **CATALINA_HOME**: Path to Tomcat directory
   - **JAVA_HOME**: Path to JDK directory

#### 4.2 Deploy WAR File

**Method 1: Manual Deployment**
```bash
# Copy WAR file to Tomcat webapps directory
cp target/library-management.war /path/to/tomcat/webapps/
```

**Method 2: Tomcat Manager**
1. Start Tomcat
2. Navigate to `http://localhost:8080/manager`
3. Upload the WAR file using the web interface

#### 4.3 Start Tomcat

**Windows:**
```cmd
cd C:\path\to\tomcat\bin
startup.bat
```

**Linux/Mac:**
```bash
cd /path/to/tomcat/bin
./startup.sh
```

#### 4.4 Access Web Application

Open your browser and navigate to:
```
http://localhost:8080/library-management/
```

**Default Login:**
- Username: `admin`
- Password: `admin123`

### Option B: Using Maven Tomcat Plugin

Add to `pom.xml` (already included if you followed the setup):

```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <port>8080</port>
        <path>/library-management</path>
    </configuration>
</plugin>
```

Run:
```bash
mvn tomcat7:run
```

## 🖥️ Step 5: Run Desktop Application

### Option A: Using Maven

```bash
mvn exec:java -Dexec.mainClass="com.library.swing.LibraryApp"
```

### Option B: Using IDE

1. **IntelliJ IDEA:**
   - Right-click `LibraryApp.java`
   - Select "Run 'LibraryApp.main()'"

2. **Eclipse:**
   - Right-click `LibraryApp.java`
   - Select "Run As" → "Java Application"

3. **VS Code:**
   - Open `LibraryApp.java`
   - Click "Run" above the main method

### Option C: Using JAR File

Create an executable JAR:

```bash
mvn clean package

# Run the JAR
java -cp target/library-management-1.0.0.jar:target/lib/* com.library.swing.LibraryApp
```

**Note:** Ensure MySQL JDBC driver is in the classpath.

## 🧪 Step 6: Testing

### Test Web Application

1. **Login Test:**
   - Navigate to login page
   - Enter: admin / admin123
   - Should redirect to dashboard

2. **Book Management:**
   - Go to Books section
   - Add a new book
   - Search for the book
   - Edit and verify changes

3. **Transaction Test:**
   - Go to Transactions
   - Issue a book to a member
   - Verify it appears in active transactions
   - Return the book

### Test Desktop Application

1. **Launch Application:**
   - Run LibraryApp
   - Login with admin credentials

2. **Navigate Panels:**
   - Check Dashboard statistics
   - View Books panel
   - Check Transactions panel

3. **CRUD Operations:**
   - Add a new book
   - Edit existing book
   - Issue and return a book

## 🔧 Troubleshooting

### Issue: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Solution:**
```bash
# Verify MySQL connector is in pom.xml
mvn dependency:tree | grep mysql

# Force download
mvn clean install -U
```

### Issue: "Access denied for user 'root'@'localhost'"

**Solution:**
- Verify MySQL username and password in `DatabaseConnection.java`
- Reset MySQL password if necessary
- Check MySQL user permissions

### Issue: "Table 'library_management.users' doesn't exist"

**Solution:**
```sql
# Re-run the schema file
mysql -u root -p library_management < database/schema.sql
```

### Issue: Tomcat Port 8080 Already in Use

**Solution:**
```bash
# Windows - Find and kill process
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Or change Tomcat port in server.xml
```

### Issue: Swing Application Not Displaying Properly

**Solution:**
- Check display scaling settings
- Try different Look and Feel
- Update graphics drivers

## 📊 Verification Checklist

After setup, verify the following:

- [ ] Database tables created successfully
- [ ] Default admin user exists
- [ ] Sample books are loaded
- [ ] Web application accessible at localhost:8080
- [ ] Can login to web interface
- [ ] Desktop application launches
- [ ] Can perform CRUD operations on books
- [ ] Can issue and return books
- [ ] Dashboard shows correct statistics

## 🎯 Quick Start Commands

```bash
# Complete setup from scratch
cd LibraryManagementSystem

# 1. Build project
mvn clean install

# 2. Start web app (in one terminal)
# Deploy WAR to Tomcat or:
mvn tomcat7:run

# 3. Start desktop app (in another terminal)
mvn exec:java -Dexec.mainClass="com.library.swing.LibraryApp"
```

## 📞 Support

If you encounter any issues:

1. Check the error logs:
   - **Tomcat logs:** `tomcat/logs/catalina.out`
   - **Application logs:** Console output

2. Verify all prerequisites are correctly installed

3. Ensure database connection parameters are correct

4. Check firewall settings for port 8080 and 3306

## 🎓 Next Steps

After successful setup:

1. Explore the web interface features
2. Test the desktop application
3. Add more users and books
4. Customize the application as needed
5. Review the code structure in README.md

---

**Setup Complete! 🎉**

You now have a fully functional Library Management System running on both web and desktop platforms.
