<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Books - Library Management System</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f7fa;
        }
        
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .navbar h1 {
            font-size: 24px;
        }
        
        .navbar a {
            color: white;
            text-decoration: none;
            padding: 8px 16px;
            border-radius: 5px;
            margin-left: 10px;
        }
        
        .navbar a:hover {
            background: rgba(255, 255, 255, 0.2);
        }
        
        .container {
            max-width: 1400px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .header-section {
            background: white;
            padding: 25px;
            border-radius: 10px;
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .search-box {
            display: flex;
            gap: 10px;
        }
        
        .search-box input {
            padding: 10px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            width: 300px;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-weight: 600;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
        }
        
        .btn-success {
            background: #4caf50;
            color: white;
        }
        
        .btn-danger {
            background: #f44336;
            color: white;
        }
        
        .btn-warning {
            background: #ff9800;
            color: white;
        }
        
        table {
            width: 100%;
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }
        
        th {
            background: #667eea;
            color: white;
            padding: 15px;
            text-align: left;
        }
        
        td {
            padding: 15px;
            border-bottom: 1px solid #f0f0f0;
        }
        
        tr:hover {
            background: #f9f9f9;
        }
        
        .badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: 600;
        }
        
        .badge-success {
            background: #e8f5e9;
            color: #4caf50;
        }
        
        .badge-danger {
            background: #ffebee;
            color: #f44336;
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: #e8f5e9;
            color: #4caf50;
            border-left: 4px solid #4caf50;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <h1>📚 Library Management System</h1>
        <div>
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/books">Books</a>
            <a href="${pageContext.request.contextPath}/transactions">Transactions</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </nav>
    
    <div class="container">
        <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">
                Operation completed successfully!
            </div>
        <% } %>
        
        <div class="header-section">
            <h2>Book Collection</h2>
            <div style="display: flex; gap: 10px;">
                <form action="${pageContext.request.contextPath}/books" method="get" class="search-box">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="keyword" placeholder="Search by title, author, or ISBN..." value="${keyword}">
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
                <a href="${pageContext.request.contextPath}/books?action=add" class="btn btn-success">Add New Book</a>
            </div>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>ISBN</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Category</th>
                    <th>Total Copies</th>
                    <th>Available</th>
                    <th>Location</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="book" items="${books}">
                    <tr>
                        <td>${book.bookId}</td>
                        <td>${book.isbn}</td>
                        <td><strong>${book.title}</strong></td>
                        <td>${book.author}</td>
                        <td>${book.category}</td>
                        <td>${book.totalCopies}</td>
                        <td>
                            <c:choose>
                                <c:when test="${book.availableCopies > 0}">
                                    <span class="badge badge-success">${book.availableCopies} available</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-danger">Not available</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${book.shelfLocation}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/books?action=edit&id=${book.bookId}" 
                               class="btn btn-warning" style="font-size: 12px; padding: 5px 10px;">Edit</a>
                            <a href="${pageContext.request.contextPath}/books?action=delete&id=${book.bookId}" 
                               class="btn btn-danger" style="font-size: 12px; padding: 5px 10px;"
                               onclick="return confirm('Are you sure you want to delete this book?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
