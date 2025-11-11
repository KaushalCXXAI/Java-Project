<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${book != null ? 'Edit' : 'Add'} Book - Library Management System</title>
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
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }
        
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 14px;
        }
        
        .form-group input:focus,
        .form-group select:focus {
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
            <h2>${book != null ? 'Edit Book' : 'Add New Book'}</h2>
            
            <form method="post" action="${pageContext.request.contextPath}/books">
                <input type="hidden" name="action" value="${book != null ? 'update' : 'add'}">
                <c:if test="${book != null}">
                    <input type="hidden" name="bookId" value="${book.bookId}">
                </c:if>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="isbn">ISBN *</label>
                        <input type="text" id="isbn" name="isbn" value="${book.isbn}" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="title">Title *</label>
                        <input type="text" id="title" name="title" value="${book.title}" required>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="author">Author *</label>
                        <input type="text" id="author" name="author" value="${book.author}" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="publisher">Publisher</label>
                        <input type="text" id="publisher" name="publisher" value="${book.publisher}">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="publicationYear">Publication Year</label>
                        <input type="number" id="publicationYear" name="publicationYear" 
                               value="${book.publicationYear}" min="1800" max="2100">
                    </div>
                    
                    <div class="form-group">
                        <label for="category">Category</label>
                        <select id="category" name="category">
                            <option value="Programming" ${book.category == 'Programming' ? 'selected' : ''}>Programming</option>
                            <option value="Fiction" ${book.category == 'Fiction' ? 'selected' : ''}>Fiction</option>
                            <option value="Non-Fiction" ${book.category == 'Non-Fiction' ? 'selected' : ''}>Non-Fiction</option>
                            <option value="Science" ${book.category == 'Science' ? 'selected' : ''}>Science</option>
                            <option value="Mathematics" ${book.category == 'Mathematics' ? 'selected' : ''}>Mathematics</option>
                            <option value="History" ${book.category == 'History' ? 'selected' : ''}>History</option>
                            <option value="Biography" ${book.category == 'Biography' ? 'selected' : ''}>Biography</option>
                            <option value="Other" ${book.category == 'Other' ? 'selected' : ''}>Other</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="totalCopies">Total Copies *</label>
                        <input type="number" id="totalCopies" name="totalCopies" 
                               value="${book.totalCopies != null ? book.totalCopies : 1}" min="1" required>
                    </div>
                    
                    <c:if test="${book != null}">
                        <div class="form-group">
                            <label for="availableCopies">Available Copies *</label>
                            <input type="number" id="availableCopies" name="availableCopies" 
                                   value="${book.availableCopies}" min="0" required>
                        </div>
                    </c:if>
                    
                    <div class="form-group">
                        <label for="shelfLocation">Shelf Location</label>
                        <input type="text" id="shelfLocation" name="shelfLocation" value="${book.shelfLocation}">
                    </div>
                </div>
                
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">
                        ${book != null ? 'Update Book' : 'Add Book'}
                    </button>
                    <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
