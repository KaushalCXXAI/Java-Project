package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    private BookDAO bookDAO;
    
    @Override
    public void init() {
        bookDAO = new BookDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteBook(request, response);
                break;
            case "search":
                searchBooks(request, response);
                break;
            default:
                listBooks(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addBook(request, response);
        } else if ("update".equals(action)) {
            updateBook(request, response);
        }
    }
    
    private void listBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Book> books = bookDAO.getAllBooks();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/WEB-INF/views/books.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/book-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("id"));
        Book book = bookDAO.getBookById(bookId);
        request.setAttribute("book", book);
        request.getRequestDispatcher("/WEB-INF/views/book-form.jsp").forward(request, response);
    }
    
    private void addBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Book book = new Book();
        book.setIsbn(request.getParameter("isbn"));
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setPublisher(request.getParameter("publisher"));
        book.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
        book.setCategory(request.getParameter("category"));
        book.setTotalCopies(Integer.parseInt(request.getParameter("totalCopies")));
        book.setAvailableCopies(Integer.parseInt(request.getParameter("totalCopies")));
        book.setShelfLocation(request.getParameter("shelfLocation"));
        
        if (bookDAO.addBook(book)) {
            response.sendRedirect(request.getContextPath() + "/books?success=added");
        } else {
            request.setAttribute("error", "Failed to add book");
            request.getRequestDispatcher("/WEB-INF/views/book-form.jsp").forward(request, response);
        }
    }
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Book book = new Book();
        book.setBookId(Integer.parseInt(request.getParameter("bookId")));
        book.setIsbn(request.getParameter("isbn"));
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setPublisher(request.getParameter("publisher"));
        book.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
        book.setCategory(request.getParameter("category"));
        book.setTotalCopies(Integer.parseInt(request.getParameter("totalCopies")));
        book.setAvailableCopies(Integer.parseInt(request.getParameter("availableCopies")));
        book.setShelfLocation(request.getParameter("shelfLocation"));
        
        if (bookDAO.updateBook(book)) {
            response.sendRedirect(request.getContextPath() + "/books?success=updated");
        } else {
            request.setAttribute("error", "Failed to update book");
            request.setAttribute("book", book);
            request.getRequestDispatcher("/WEB-INF/views/book-form.jsp").forward(request, response);
        }
    }
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("id"));
        
        if (bookDAO.deleteBook(bookId)) {
            response.sendRedirect(request.getContextPath() + "/books?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/books?error=delete_failed");
        }
    }
    
    private void searchBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Book> books = bookDAO.searchBooks(keyword);
        request.setAttribute("books", books);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/WEB-INF/views/books.jsp").forward(request, response);
    }
}
