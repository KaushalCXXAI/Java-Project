<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Transactions - Library Management System</title>
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
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-weight: 600;
        }
        
        .btn-success {
            background: #4caf50;
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
        
        .badge-warning {
            background: #fff3e0;
            color: #ff9800;
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
                Transaction completed successfully!
            </div>
        <% } %>
        
        <div class="header-section">
            <h2>Book Transactions</h2>
            <a href="${pageContext.request.contextPath}/transactions?action=issue" class="btn btn-success">Issue New Book</a>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>Transaction ID</th>
                    <th>Book Title</th>
                    <th>User Name</th>
                    <th>Issue Date</th>
                    <th>Due Date</th>
                    <th>Return Date</th>
                    <th>Fine</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="transaction" items="${transactions}">
                    <tr>
                        <td>${transaction.transactionId}</td>
                        <td><strong>${transaction.bookTitle}</strong></td>
                        <td>${transaction.userName}</td>
                        <td><fmt:formatDate value="${transaction.issueDate}" pattern="dd MMM yyyy"/></td>
                        <td><fmt:formatDate value="${transaction.dueDate}" pattern="dd MMM yyyy"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${transaction.returnDate != null}">
                                    <fmt:formatDate value="${transaction.returnDate}" pattern="dd MMM yyyy"/>
                                </c:when>
                                <c:otherwise>
                                    -
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>₹ ${transaction.fineAmount}</td>
                        <td>
                            <c:choose>
                                <c:when test="${transaction.status == 'ISSUED'}">
                                    <span class="badge badge-warning">Issued</span>
                                </c:when>
                                <c:when test="${transaction.status == 'RETURNED'}">
                                    <span class="badge badge-success">Returned</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-danger">Overdue</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${transaction.status == 'ISSUED'}">
                                <a href="${pageContext.request.contextPath}/transactions?action=return&id=${transaction.transactionId}" 
                                   class="btn btn-warning" style="font-size: 12px; padding: 5px 10px;">Return Book</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
