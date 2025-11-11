package com.library.swing;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BooksPanel extends JPanel {
    private BookDAO bookDAO;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public BooksPanel() {
        bookDAO = new BookDAO();
        initializeUI();
        loadBooks();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("Book Collection");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        // Search and Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton searchBtn = createButton("Search", new Color(102, 126, 234));
        searchBtn.addActionListener(e -> searchBooks());
        
        JButton refreshBtn = createButton("Refresh", new Color(76, 175, 80));
        refreshBtn.addActionListener(e -> loadBooks());
        
        JButton addBtn = createButton("Add Book", new Color(76, 175, 80));
        addBtn.addActionListener(e -> showAddBookDialog());
        
        actionPanel.add(new JLabel("Search:"));
        actionPanel.add(searchField);
        actionPanel.add(searchBtn);
        actionPanel.add(refreshBtn);
        actionPanel.add(addBtn);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);
        
        // Table Panel
        String[] columns = {"ID", "ISBN", "Title", "Author", "Category", "Total", "Available", "Location"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        booksTable = new JTable(tableModel);
        booksTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        booksTable.setRowHeight(30);
        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        booksTable.getTableHeader().setBackground(new Color(102, 126, 234));
        booksTable.getTableHeader().setForeground(Color.WHITE);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        
        // Button Panel for selected book actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton editBtn = createButton("Edit Selected", new Color(255, 152, 0));
        JButton deleteBtn = createButton("Delete Selected", new Color(244, 67, 54));
        
        editBtn.addActionListener(e -> editSelectedBook());
        deleteBtn.addActionListener(e -> deleteSelectedBook());
        
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        
        // Main Content
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setOpaque(false);
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }
    
    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = bookDAO.getAllBooks();
        
        for (Book book : books) {
            Object[] row = {
                book.getBookId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getShelfLocation()
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchBooks() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Book> books = bookDAO.searchBooks(keyword);
        
        for (Book book : books) {
            Object[] row = {
                book.getBookId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getShelfLocation()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddBookDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Book", true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = createBookForm(null, dialog);
        dialog.add(formPanel);
        dialog.setVisible(true);
    }
    
    private void editSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        Book book = bookDAO.getBookById(bookId);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Book", true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = createBookForm(book, dialog);
        dialog.add(formPanel);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this book?",
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            int bookId = (int) tableModel.getValueAt(selectedRow, 0);
            if (bookDAO.deleteBook(bookId)) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete book", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private JPanel createBookForm(Book book, JDialog dialog) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField isbnField = new JTextField(book != null ? book.getIsbn() : "");
        JTextField titleField = new JTextField(book != null ? book.getTitle() : "");
        JTextField authorField = new JTextField(book != null ? book.getAuthor() : "");
        JTextField publisherField = new JTextField(book != null ? book.getPublisher() : "");
        JTextField yearField = new JTextField(book != null ? String.valueOf(book.getPublicationYear()) : "");
        JTextField categoryField = new JTextField(book != null ? book.getCategory() : "");
        JTextField copiesField = new JTextField(book != null ? String.valueOf(book.getTotalCopies()) : "1");
        JTextField availableField = new JTextField(book != null ? String.valueOf(book.getAvailableCopies()) : "1");
        JTextField locationField = new JTextField(book != null ? book.getShelfLocation() : "");
        
        panel.add(createFormField("ISBN:", isbnField));
        panel.add(createFormField("Title:", titleField));
        panel.add(createFormField("Author:", authorField));
        panel.add(createFormField("Publisher:", publisherField));
        panel.add(createFormField("Year:", yearField));
        panel.add(createFormField("Category:", categoryField));
        panel.add(createFormField("Total Copies:", copiesField));
        if (book != null) {
            panel.add(createFormField("Available:", availableField));
        }
        panel.add(createFormField("Location:", locationField));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createButton(book != null ? "Update" : "Add", new Color(102, 126, 234));
        JButton cancelBtn = createButton("Cancel", new Color(150, 150, 150));
        
        saveBtn.addActionListener(e -> {
            try {
                Book newBook = book != null ? book : new Book();
                newBook.setIsbn(isbnField.getText());
                newBook.setTitle(titleField.getText());
                newBook.setAuthor(authorField.getText());
                newBook.setPublisher(publisherField.getText());
                newBook.setPublicationYear(Integer.parseInt(yearField.getText()));
                newBook.setCategory(categoryField.getText());
                newBook.setTotalCopies(Integer.parseInt(copiesField.getText()));
                
                if (book != null) {
                    newBook.setAvailableCopies(Integer.parseInt(availableField.getText()));
                    if (bookDAO.updateBook(newBook)) {
                        JOptionPane.showMessageDialog(dialog, "Book updated successfully");
                        dialog.dispose();
                        loadBooks();
                    }
                } else {
                    newBook.setAvailableCopies(Integer.parseInt(copiesField.getText()));
                    if (bookDAO.addBook(newBook)) {
                        JOptionPane.showMessageDialog(dialog, "Book added successfully");
                        dialog.dispose();
                        loadBooks();
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        panel.add(buttonPanel);
        
        return panel;
    }
    
    private JPanel createFormField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel.setPreferredSize(new Dimension(100, 30));
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        panel.add(jLabel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
}
