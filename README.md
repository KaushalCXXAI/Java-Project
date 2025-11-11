# Library Management System

A comprehensive Library Management System built using Java with both **Web Application** (Servlets, JSP, JDBC) and **Desktop Application** (Swing) interfaces.

## Features

### Core Functionality
- **User Authentication & Authorization**
  - Role-based access (Admin, Librarian, Member)
  - Secure login system
  
- **Book Management**
  - Add, edit, delete, and search books
  - Track book availability and location
  - ISBN, title, author, publisher, category management
  - Multiple copies support

- **Transaction Management**
  - Issue books to members
  - Return books with fine calculation
  - Track due dates and overdue books
  - Transaction history

- **Dashboard & Reports**
  - Real-time statistics
  - Active transactions monitoring
  - User and book analytics

## Technology Stack

### Backend
- **Java 11** - Core programming language
- **JDBC** - Database connectivity
- **MySQL** - Relational database
- **Servlets** - Web request handling
- **JSP** - Dynamic web pages

### Frontend
- **JSP with JSTL** - Web interface
- **Java Swing** - Desktop GUI
- **CSS** - Styling for web interface

### Build Tool
- **Maven** - Dependency management and build

## Project Structure

```
LibraryManagementSystem/
├── database/
│   └── schema.sql                    # Database schema
├── src/
│   └── main/
│       ├── java/com/library/
│       │   ├── dao/                  # Data Access Objects
│       │   │   ├── BookDAO.java
│       │   │   ├── UserDAO.java
│       │   │   └── TransactionDAO.java
│       │   ├── model/                # Entity classes
│       │   │   ├── Book.java
│       │   │   ├── User.java
│       │   │   └── Transaction.java
│       │   ├── servlet/              # Web servlets
│       │   │   ├── LoginServlet.java
│       │   │   ├── DashboardServlet.java
│       │   │   ├── BookServlet.java
│       │   │   └── TransactionServlet.java
│       │   ├── swing/                # Desktop GUI
│       │   │   ├── LibraryApp.java
│       │   │   ├── MainFrame.java
│       │   │   ├── DashboardPanel.java
│       │   │   ├── BooksPanel.java
│       │   │   └── TransactionsPanel.java
│       │   └── util/
│       │       └── DatabaseConnection.java
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── views/            # JSP pages
│           │   │   ├── login.jsp
│           │   │   ├── dashboard.jsp
│           │   │   ├── books.jsp
│           │   │   ├── book-form.jsp
│           │   │   ├── transactions.jsp
│           │   │   └── issue-book.jsp
│           │   └── web.xml           # Web application config
│           └── index.jsp
└── pom.xml                           # Maven configuration
```

## Prerequisites

- **JDK 11** or higher
- **Apache Tomcat 9.x** (for web application)
- **MySQL 8.x**
- **Maven 3.6+**

## Installation & Setup

### 1. Database Setup

```sql
-- Create database and import schema
mysql -u root -p
source database/schema.sql
```

### 2. Configure Database Connection

Edit `src/main/java/com/library/util/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/library_management";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Deploy Web Application

1. Copy the generated WAR file from `target/library-management.war`
2. Deploy to Tomcat's `webapps` directory
3. Start Tomcat server
4. Access: `http://localhost:8080/library-management/`

### 5. Run Desktop Application

```bash
# Using Maven
mvn exec:java -Dexec.mainClass="com.library.swing.LibraryApp"

# Or compile and run directly
javac -d bin -cp "lib/*" src/main/java/com/library/swing/*.java
java -cp "bin:lib/*" com.library.swing.LibraryApp
```

## Default Credentials

**Admin Account:**
- Username: `admin`
- Password: `admin123`

## Usage Guide

### Web Application

1. **Login**: Navigate to the login page and enter credentials
2. **Dashboard**: View system statistics and quick actions
3. **Manage Books**: 
   - Browse all books
   - Search by title, author, or ISBN
   - Add new books
   - Edit or delete existing books
4. **Manage Transactions**:
   - Issue books to members
   - Return books
   - View transaction history
   - Calculate fines

### Desktop Application

1. **Launch**: Run the LibraryApp main class
2. **Login**: Enter your credentials
3. **Navigate**: Use the top navigation bar to switch between:
   - Dashboard: View statistics
   - Books: Manage book collection
   - Transactions: Handle book loans and returns

## Database Schema

### Tables

#### users
- `user_id` (PK)
- `username` (UNIQUE)
- `password`
- `full_name`
- `email` (UNIQUE)
- `phone`
- `role` (ADMIN, LIBRARIAN, MEMBER)
- `registration_date`
- `status` (ACTIVE, INACTIVE, SUSPENDED)

#### books
- `book_id` (PK)
- `isbn` (UNIQUE)
- `title`
- `author`
- `publisher`
- `publication_year`
- `category`
- `total_copies`
- `available_copies`
- `shelf_location`
- `added_date`

#### transactions
- `transaction_id` (PK)
- `book_id` (FK)
- `user_id` (FK)
- `issue_date`
- `due_date`
- `return_date`
- `fine_amount`
- `status` (ISSUED, RETURNED, OVERDUE)

## Features in Detail

### Book Management
- Full CRUD operations
- Real-time availability tracking
- Advanced search functionality
- Category-based organization
- ISBN validation

### User Management
- Role-based access control
- User status management
- Secure authentication

### Transaction Management
- Automated due date calculation
- Fine calculation system
- Transaction history
- Active loan tracking

## API Endpoints (Web)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/login` | GET/POST | User authentication |
| `/logout` | GET | User logout |
| `/dashboard` | GET | Dashboard view |
| `/books` | GET | List all books |
| `/books?action=add` | GET/POST | Add new book |
| `/books?action=edit&id={id}` | GET/POST | Edit book |
| `/books?action=delete&id={id}` | GET | Delete book |
| `/books?action=search&keyword={keyword}` | GET | Search books |
| `/transactions` | GET | List transactions |
| `/transactions?action=issue` | GET/POST | Issue book |
| `/transactions?action=return&id={id}` | GET | Return book |

## Screenshots

### Web Interface
- Modern, responsive design
- Gradient color scheme (Purple/Blue)
- Clean, professional layout
- Easy navigation

### Desktop Interface
- Native look and feel
- Table-based data views
- Dialog forms for data entry
- Color-coded buttons

## Development

### Adding New Features

1. **Model**: Create entity class in `com.library.model`
2. **DAO**: Implement database operations in `com.library.dao`
3. **Servlet**: Create servlet handler in `com.library.servlet`
4. **View**: Add JSP page in `WEB-INF/views`
5. **Swing**: Add panel in `com.library.swing`

### Code Style
- Follow Java naming conventions
- Use meaningful variable names
- Add comments for complex logic
- Handle exceptions properly

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify MySQL is running
   - Check database credentials
   - Ensure database schema is created

2. **Tomcat Deployment Issues**
   - Check Tomcat logs
   - Verify JDK version compatibility
   - Ensure WAR file is built correctly

3. **Swing Application Crashes**
   - Check classpath includes MySQL driver
   - Verify database connectivity
   - Review console error messages

## Future Enhancements

- [ ] Email notifications for due dates
- [ ] Book reservation system
- [ ] Report generation (PDF/Excel)
- [ ] Barcode scanning support
- [ ] Multi-language support
- [ ] Book recommendations
- [ ] Mobile application
- [ ] REST API for external integration

## License

This project is created for educational purposes.

## Contributors

- Your Name - Initial Development

## Support

For issues and questions, please create an issue in the project repository.

---

**Happy Coding! 📚**
