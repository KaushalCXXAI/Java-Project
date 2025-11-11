package com.library.swing;

import com.library.model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public MainFrame(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library Management System - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Top Navigation Bar
        JPanel navBar = createNavigationBar();
        
        // Content Panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(245, 247, 250));
        
        // Add panels
        contentPanel.add(new DashboardPanel(), "dashboard");
        contentPanel.add(new BooksPanel(), "books");
        contentPanel.add(new TransactionsPanel(), "transactions");
        
        mainPanel.add(navBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createNavigationBar() {
        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(102, 126, 234));
        navBar.setPreferredSize(new Dimension(0, 60));
        navBar.setLayout(new BorderLayout());
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Left side - Title
        JLabel titleLabel = new JLabel("📚 Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        // Center - Navigation Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);
        
        JButton dashboardBtn = createNavButton("Dashboard");
        JButton booksBtn = createNavButton("Books");
        JButton transactionsBtn = createNavButton("Transactions");
        
        dashboardBtn.addActionListener(e -> cardLayout.show(contentPanel, "dashboard"));
        booksBtn.addActionListener(e -> cardLayout.show(contentPanel, "books"));
        transactionsBtn.addActionListener(e -> cardLayout.show(contentPanel, "transactions"));
        
        buttonPanel.add(dashboardBtn);
        buttonPanel.add(booksBtn);
        buttonPanel.add(transactionsBtn);
        
        // Right side - User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel(currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(244, 67, 54));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());
        
        rightPanel.add(userLabel);
        rightPanel.add(Box.createHorizontalStrut(15));
        rightPanel.add(logoutBtn);
        
        navBar.add(titleLabel, BorderLayout.WEST);
        navBar.add(buttonPanel, BorderLayout.CENTER);
        navBar.add(rightPanel, BorderLayout.EAST);
        
        return navBar;
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(118, 75, 162));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 60, 130));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(118, 75, 162));
            }
        });
        
        return button;
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}
