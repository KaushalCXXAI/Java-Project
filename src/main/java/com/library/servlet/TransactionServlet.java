package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.UserDAO;
import com.library.model.Book;
import com.library.model.Transaction;
import com.library.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {
    private TransactionDAO transactionDAO;
    private BookDAO bookDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() {
        transactionDAO = new TransactionDAO();
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "issue":
                showIssueForm(request, response);
                break;
            case "return":
                returnBook(request, response);
                break;
            default:
                listTransactions(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("issue".equals(action)) {
            issueBook(request, response);
        }
    }
    
    private void listTransactions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        request.setAttribute("transactions", transactions);
        request.getRequestDispatcher("/WEB-INF/views/transactions.jsp").forward(request, response);
    }
    
    private void showIssueForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Book> books = bookDAO.getAllBooks();
        List<User> users = userDAO.getAllUsers();
        
        request.setAttribute("books", books);
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/issue-book.jsp").forward(request, response);
    }
    
    private void issueBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        int days = Integer.parseInt(request.getParameter("days"));
        
        // Calculate due date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        Date dueDate = new Date(cal.getTimeInMillis());
        
        if (transactionDAO.issueBook(bookId, userId, dueDate)) {
            response.sendRedirect(request.getContextPath() + "/transactions?success=issued");
        } else {
            request.setAttribute("error", "Failed to issue book. Book may not be available.");
            showIssueForm(request, response);
        }
    }
    
    private void returnBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int transactionId = Integer.parseInt(request.getParameter("id"));
        double fine = 0.0;
        
        String fineParam = request.getParameter("fine");
        if (fineParam != null && !fineParam.isEmpty()) {
            fine = Double.parseDouble(fineParam);
        }
        
        if (transactionDAO.returnBook(transactionId, fine)) {
            response.sendRedirect(request.getContextPath() + "/transactions?success=returned");
        } else {
            response.sendRedirect(request.getContextPath() + "/transactions?error=return_failed");
        }
    }
}
