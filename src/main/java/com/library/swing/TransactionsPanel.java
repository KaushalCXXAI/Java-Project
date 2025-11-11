package com.library.swing;

import com.library.dao.BookDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.UserDAO;
import com.library.model.Book;
import com.library.model.Transaction;
import com.library.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TransactionsPanel extends JPanel {
    private TransactionDAO transactionDAO;
    private BookDAO bookDAO;
    private UserDAO userDAO;
    private JTable transactionsTable;
    private DefaultTableModel tableModel;
    
    public TransactionsPanel() {
        transactionDAO = new TransactionDAO();
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
        
        initializeUI();
        loadTransactions();
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
        
        JLabel titleLabel = new JLabel("Book Transactions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);
        
        JButton issueBtn = createButton("Issue Book", new Color(76, 175, 80));
        JButton refreshBtn = createButton("Refresh", new Color(102, 126, 234));
        
        issueBtn.addActionListener(e -> showIssueBookDialog());
        refreshBtn.addActionListener(e -> loadTransactions());
        
        actionPanel.add(refreshBtn);
        actionPanel.add(issueBtn);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);
        
        // Table Panel
        String[] columns = {"ID", "Book Title", "User Name", "Issue Date", "Due Date", "Return Date", "Fine", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transactionsTable = new JTable(tableModel);
        transactionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        transactionsTable.setRowHeight(30);
        transactionsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        transactionsTable.getTableHeader().setBackground(new Color(102, 126, 234));
        transactionsTable.getTableHeader().setForeground(Color.WHITE);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton returnBtn = createButton("Return Selected", new Color(255, 152, 0));
        returnBtn.addActionListener(e -> returnSelectedBook());
        
        buttonPanel.add(returnBtn);
        
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
        button.setPreferredSize(new Dimension(130, 35));
        return button;
    }
    
    private void loadTransactions() {
        tableModel.setRowCount(0);
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        
        for (Transaction transaction : transactions) {
            Object[] row = {
                transaction.getTransactionId(),
                transaction.getBookTitle(),
                transaction.getUserName(),
                dateFormat.format(transaction.getIssueDate()),
                dateFormat.format(transaction.getDueDate()),
                transaction.getReturnDate() != null ? dateFormat.format(transaction.getReturnDate()) : "-",
                "₹ " + transaction.getFineAmount(),
                transaction.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showIssueBookDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Issue Book", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Book Selection
        JLabel bookLabel = new JLabel("Select Book:");
        bookLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        List<Book> books = bookDAO.getAllBooks();
        DefaultComboBoxModel<String> bookModel = new DefaultComboBoxModel<>();
        for (Book book : books) {
            if (book.getAvailableCopies() > 0) {
                bookModel.addElement(book.getBookId() + " - " + book.getTitle() + " (" + book.getAvailableCopies() + " available)");
            }
        }
        JComboBox<String> bookCombo = new JComboBox<>(bookModel);
        bookCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // User Selection
        JLabel userLabel = new JLabel("Select Member:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        List<User> users = userDAO.getAllUsers();
        DefaultComboBoxModel<String> userModel = new DefaultComboBoxModel<>();
        for (User user : users) {
            if (user.getStatus().equals("ACTIVE") && !user.getRole().equals("ADMIN")) {
                userModel.addElement(user.getUserId() + " - " + user.getFullName());
            }
        }
        JComboBox<String> userCombo = new JComboBox<>(userModel);
        userCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Days Selection
        JLabel daysLabel = new JLabel("Loan Period (Days):");
        daysLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(14, 1, 90, 1));
        daysSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        panel.add(bookLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(bookCombo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(userLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(userCombo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(daysLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(daysSpinner);
        panel.add(Box.createVerticalStrut(25));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton issueBtn = createButton("Issue Book", new Color(76, 175, 80));
        JButton cancelBtn = createButton("Cancel", new Color(150, 150, 150));
        
        issueBtn.addActionListener(e -> {
            try {
                String bookSelection = (String) bookCombo.getSelectedItem();
                String userSelection = (String) userCombo.getSelectedItem();
                
                if (bookSelection == null || userSelection == null) {
                    JOptionPane.showMessageDialog(dialog, "Please select both book and user", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int bookId = Integer.parseInt(bookSelection.split(" - ")[0]);
                int userId = Integer.parseInt(userSelection.split(" - ")[0]);
                int days = (int) daysSpinner.getValue();
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, days);
                Date dueDate = new Date(cal.getTimeInMillis());
                
                if (transactionDAO.issueBook(bookId, userId, dueDate)) {
                    JOptionPane.showMessageDialog(dialog, "Book issued successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadTransactions();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to issue book", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(issueBtn);
        buttonPanel.add(cancelBtn);
        panel.add(buttonPanel);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void returnSelectedBook() {
        int selectedRow = transactionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String status = (String) tableModel.getValueAt(selectedRow, 7);
        if (status.equals("RETURNED")) {
            JOptionPane.showMessageDialog(this, "This book has already been returned", "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
        
        String fineStr = JOptionPane.showInputDialog(this, "Enter fine amount (if any):", "0.00");
        double fine = 0.0;
        
        if (fineStr != null) {
            try {
                fine = Double.parseDouble(fineStr);
            } catch (NumberFormatException e) {
                fine = 0.0;
            }
            
            if (transactionDAO.returnBook(transactionId, fine)) {
                JOptionPane.showMessageDialog(this, "Book returned successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTransactions();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return book", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
