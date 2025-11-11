package com.library.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int userId;
    private Timestamp issueDate;
    private Date dueDate;
    private Timestamp returnDate;
    private double fineAmount;
    private String status; // ISSUED, RETURNED, OVERDUE
    
    // For display purposes
    private String bookTitle;
    private String userName;
    
    // Constructors
    public Transaction() {}
    
    public Transaction(int transactionId, int bookId, int userId, 
                      Timestamp issueDate, Date dueDate, String status) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
    }
    
    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Timestamp getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(Timestamp issueDate) {
        this.issueDate = issueDate;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Timestamp getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }
    
    public double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", fineAmount=" + fineAmount +
                '}';
    }
}
