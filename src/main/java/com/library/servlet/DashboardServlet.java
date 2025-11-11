package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private BookDAO bookDAO;
    private UserDAO userDAO;
    private TransactionDAO transactionDAO;
    
    @Override
    public void init() {
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
        transactionDAO = new TransactionDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get statistics
        int totalBooks = bookDAO.getAllBooks().size();
        int totalUsers = userDAO.getAllUsers().size();
        int activeTransactions = transactionDAO.getActiveTransactions().size();
        
        request.setAttribute("totalBooks", totalBooks);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("activeTransactions", activeTransactions);
        
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
