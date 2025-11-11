<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Issue Book - Library Management System</title>
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
            max-width: 800px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .form-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }
        
        .form-container h2 {
            margin-bottom: 30px;
            color: #333;
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }
        
        .form-group select,
        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 14px;
        }
        
        .form-group select:focus,
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
        }
        
        .btn-secondary {
            background: #6c757d;
            color: white;
        }
        
        .error-message {
            background: #ffebee;
            color: #f44336;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            border-left: 4px solid #f44336;
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
        <div class="form-container">
            <h2>Issue Book</h2>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <form method="post" action="${pageContext.request.contextPath}/transactions">
                <input type="hidden" name="action" value="issue">
                
                <div class="form-group">
                    <label for="bookId">Select Book *</label>
                    <select id="bookId" name="bookId" required>
                        <option value="">-- Select a Book --</option>
                        <c:forEach var="book" items="${books}">
                            <c:if test="${book.availableCopies > 0}">
                                <option value="${book.bookId}">
                                    ${book.title} by ${book.author} (Available: ${book.availableCopies})
                                </option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="userId">Select Member *</label>
                    <select id="userId" name="userId" required>
                        <option value="">-- Select a Member --</option>
                        <c:forEach var="user" items="${users}">
                            <c:if test="${user.status == 'ACTIVE' && user.role != 'ADMIN'}">
                                <option value="${user.userId}">
                                    ${user.fullName} (${user.username})
                                </option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="days">Loan Period (Days) *</label>
                    <input type="number" id="days" name="days" value="14" min="1" max="90" required>
                </div>
                
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Issue Book</button>
                    <a href="${pageContext.request.contextPath}/transactions" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
