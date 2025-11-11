package com.library.dao;

import com.library.model.Transaction;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    
    public boolean issueBook(int bookId, int userId, Date dueDate) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert transaction
            String insertSql = "INSERT INTO transactions (book_id, user_id, due_date, status) " +
                             "VALUES (?, ?, ?, 'ISSUED')";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, bookId);
            insertStmt.setInt(2, userId);
            insertStmt.setDate(3, dueDate);
            insertStmt.executeUpdate();
            
            // Update book availability
            String updateSql = "UPDATE books SET available_copies = available_copies - 1 " +
                             "WHERE book_id = ? AND available_copies > 0";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, bookId);
            int updated = updateStmt.executeUpdate();
            
            if (updated == 0) {
                conn.rollback();
                return false;
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean returnBook(int transactionId, double fine) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get book ID from transaction
            String getBookSql = "SELECT book_id FROM transactions WHERE transaction_id = ?";
            PreparedStatement getStmt = conn.prepareStatement(getBookSql);
            getStmt.setInt(1, transactionId);
            ResultSet rs = getStmt.executeQuery();
            
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            
            int bookId = rs.getInt("book_id");
            
            // Update transaction
            String updateTransSql = "UPDATE transactions SET return_date = NOW(), " +
                                   "fine_amount = ?, status = 'RETURNED' WHERE transaction_id = ?";
            PreparedStatement updateTransStmt = conn.prepareStatement(updateTransSql);
            updateTransStmt.setDouble(1, fine);
            updateTransStmt.setInt(2, transactionId);
            updateTransStmt.executeUpdate();
            
            // Update book availability
            String updateBookSql = "UPDATE books SET available_copies = available_copies + 1 " +
                                 "WHERE book_id = ?";
            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
            updateBookStmt.setInt(1, bookId);
            updateBookStmt.executeUpdate();
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public List<Transaction> getActiveTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, b.title as book_title, u.full_name as user_name " +
                     "FROM transactions t " +
                     "JOIN books b ON t.book_id = b.book_id " +
                     "JOIN users u ON t.user_id = u.user_id " +
                     "WHERE t.status = 'ISSUED' " +
                     "ORDER BY t.due_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    public List<Transaction> getTransactionsByUser(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, b.title as book_title, u.full_name as user_name " +
                     "FROM transactions t " +
                     "JOIN books b ON t.book_id = b.book_id " +
                     "JOIN users u ON t.user_id = u.user_id " +
                     "WHERE t.user_id = ? " +
                     "ORDER BY t.issue_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, b.title as book_title, u.full_name as user_name " +
                     "FROM transactions t " +
                     "JOIN books b ON t.book_id = b.book_id " +
                     "JOIN users u ON t.user_id = u.user_id " +
                     "ORDER BY t.issue_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setBookId(rs.getInt("book_id"));
        transaction.setUserId(rs.getInt("user_id"));
        transaction.setIssueDate(rs.getTimestamp("issue_date"));
        transaction.setDueDate(rs.getDate("due_date"));
        transaction.setReturnDate(rs.getTimestamp("return_date"));
        transaction.setFineAmount(rs.getDouble("fine_amount"));
        transaction.setStatus(rs.getString("status"));
        transaction.setBookTitle(rs.getString("book_title"));
        transaction.setUserName(rs.getString("user_name"));
        return transaction;
    }
}
