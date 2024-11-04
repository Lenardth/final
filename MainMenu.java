import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MainMenu extends JFrame {
    private double calculateEstimatedPrice(String initialPoint, String finalDestination) {
        return Math.random() * 100 + 20;
    }

    private boolean checkTaxiAvailability(String initialPoint, String finalDestination) {
        return Math.random() > 0.5;
    }

    private double walletBalance;
    private JLabel clockLabel;
    private JLabel notificationLabel;
    private JButton showNotificationsButton;
    private JLabel walletLabel;
    private ArrayList<Taxi> taxis;
    private DefaultListModel<String> notificationsModel;
    private JList<String> notificationList;
    private JComboBox<String> taxiComboBox;
    private JTable taxiTable;
    private DefaultTableModel taxiTableModel;
    private double currentFare;

    // New Colors based on Midnight blue, Royal blue, Burgundy red, and White
    private final Color BG_COLOR_DARK = new Color(25, 25, 112);  // Midnight blue
    private final Color BG_COLOR_LIGHT = new Color(70, 130, 180);  // Light blue
    private final Color PANEL_COLOR = new Color(12, 53, 120);  // Royal blue
    private final Color BUTTON_COLOR = Color.BLACK;  // Button color always black
    private final Color BUTTON_TEXT_COLOR = Color.BLACK;  // Button description always black
    private final Color TEXT_COLOR = Color.WHITE;  // White text
    private final Color ACCENT_COLOR = new Color(44, 130, 201);  // Accent blue

    private boolean isLightTheme = true;

    public MainMenu(double initialBalance) {
        this.walletBalance = initialBalance;
        taxis = new ArrayList<>();
        notificationsModel = new DefaultListModel<>();
        initializeTaxis();
        addSampleNewsNotifications();

        setTitle("Taxi Service Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use Mac OS specific settings if available
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem themeMenuItem = new JMenuItem("Switch Theme");
        editMenu.add(themeMenuItem);
        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        saveMenuItem.addActionListener(e -> saveTaxiData());
        exitMenuItem.addActionListener(e -> System.exit(0));
        themeMenuItem.addActionListener(e -> switchTheme());
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Taxi Service Dashboard v2.0", "About", JOptionPane.INFORMATION_MESSAGE));

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(isLightTheme ? BG_COLOR_LIGHT : BG_COLOR_DARK);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setBackground(PANEL_COLOR);

        JLabel logoLabel = new JLabel("Taxi Service Dashboard", JLabel.LEFT);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
        logoLabel.setForeground(TEXT_COLOR);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Arial", Font.BOLD, 24));
        clockLabel.setForeground(TEXT_COLOR);
        headerPanel.add(clockLabel, BorderLayout.EAST);
        startClock();

        JTextField searchField = new JTextField("Search...");
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setBackground(PANEL_COLOR);
        searchField.setForeground(TEXT_COLOR);
        searchField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.setBackground(BUTTON_COLOR);
        searchButton.setForeground(BUTTON_TEXT_COLOR);
        searchButton.setFocusPainted(false);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();
            if (!searchQuery.isEmpty()) {
                StringBuilder result = new StringBuilder();
                for (Taxi taxi : taxis) {
                    if (taxi.getTaxiNumber().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        searchQuery.equalsIgnoreCase(taxi.getStartTime()) ||
                        searchQuery.equalsIgnoreCase(taxi.getEndTime())) {
                        result.append("Taxi Number: ").append(taxi.getTaxiNumber())
                                .append(", Available Start Time: ").append(taxi.getStartTime())
                                .append(", Available End Time: ").append(taxi.getEndTime()).append("\n");
                    }
                }
                if (result.length() == 0) {
                    JOptionPane.showMessageDialog(this, "No taxis found for the given search.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, result.toString(), "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainContainer.add(headerPanel, BorderLayout.NORTH);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(isLightTheme ? BG_COLOR_LIGHT : BG_COLOR_DARK);
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));

        JButton bookTaxiButton = createModernSidebarButton("Book a Taxi", BUTTON_COLOR);
        JButton nearbyTaxiButton = createModernSidebarButton("See Nearby Taxis", BUTTON_COLOR);
        JButton taxiRoutesButton = createModernSidebarButton("View Taxi Routes", BUTTON_COLOR);
        JButton taxiScheduleButton = createModernSidebarButton("Taxi Schedule", BUTTON_COLOR);
        JButton themeSwitcher = createModernSidebarButton("Switch Theme", BUTTON_COLOR);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(bookTaxiButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(nearbyTaxiButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(taxiRoutesButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(taxiScheduleButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(themeSwitcher);

        mainContainer.add(sidebarPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setOpaque(false);

        JPanel walletPanel = new JPanel(new GridBagLayout());
        walletPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        walletPanel.setOpaque(false);

        walletLabel = new JLabel(String.format("Wallet Balance: R%.2f", walletBalance), SwingConstants.CENTER);
        walletLabel.setFont(new Font("Arial", Font.BOLD, 28));
        walletLabel.setForeground(TEXT_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel inputPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(PANEL_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        inputPanel.setLayout(new GridLayout(3, 2, 20, 20));
        inputPanel.setOpaque(false);

        JLabel initialPointLabel = new JLabel("Initial Point:");
        initialPointLabel.setFont(new Font("Arial", Font.BOLD, 20));
        initialPointLabel.setForeground(TEXT_COLOR);
        JTextField initialPointField = new JTextField();
        initialPointField.setBackground(PANEL_COLOR);
        initialPointField.setForeground(TEXT_COLOR);
        initialPointField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));

        JLabel finalDestinationLabel = new JLabel("Final Destination:");
        finalDestinationLabel.setFont(new Font("Arial", Font.BOLD, 20));
        finalDestinationLabel.setForeground(TEXT_COLOR);
        JTextField finalDestinationField = new JTextField();
        finalDestinationField.setBackground(PANEL_COLOR);
        finalDestinationField.setForeground(TEXT_COLOR);
        finalDestinationField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));

        JLabel taxiSelectionLabel = new JLabel("Select Taxi:");
        taxiSelectionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        taxiSelectionLabel.setForeground(TEXT_COLOR);
        taxiComboBox = new JComboBox<>(new String[]{"Standard Taxi", "Luxury Taxi", "Minibus"});
        taxiComboBox.setBackground(PANEL_COLOR);
        taxiComboBox.setForeground(TEXT_COLOR);

        inputPanel.add(initialPointLabel);
        inputPanel.add(initialPointField);
        inputPanel.add(finalDestinationLabel);
        inputPanel.add(finalDestinationField);
        inputPanel.add(taxiSelectionLabel);
        inputPanel.add(taxiComboBox);

        gbc.gridx = 0;
        gbc.gridy = 0;
        walletPanel.add(walletLabel, gbc);

        gbc.gridy = 1;
        walletPanel.add(inputPanel, gbc);

        gbc.gridy = 2;
        JPanel walletButtonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        walletButtonPanel.setOpaque(false);
        JButton addMoneyButton = createModernSidebarButton("Add Money", BUTTON_COLOR);
        JButton withdrawMoneyButton = createModernSidebarButton("Pay", BUTTON_COLOR);
        JButton showMapButton = createModernSidebarButton("Show Map", BUTTON_COLOR);

        walletButtonPanel.add(addMoneyButton);
        walletButtonPanel.add(withdrawMoneyButton);
        walletButtonPanel.add(showMapButton);

        walletPanel.add(walletButtonPanel, gbc);

        gbc.gridy = 3;
        JButton calculateButton = createModernSidebarButton("Calculate Fare & Availability", BUTTON_COLOR);
        walletPanel.add(calculateButton, gbc);

        gbc.gridy = 4;
        JLabel estimatedPriceLabel = new JLabel("Estimated Price: N/A");
        estimatedPriceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        estimatedPriceLabel.setForeground(TEXT_COLOR);
        walletPanel.add(estimatedPriceLabel, gbc);

        gbc.gridy = 5;
        JLabel taxiAvailabilityLabel = new JLabel("Taxi Availability: N/A");
        taxiAvailabilityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        taxiAvailabilityLabel.setForeground(TEXT_COLOR);
        walletPanel.add(taxiAvailabilityLabel, gbc);

        gbc.gridy = 6;
        taxiTableModel = new DefaultTableModel(new String[]{"Taxi Number", "Available Start Time", "Available End Time"}, 0);
        taxiTable = new JTable(taxiTableModel);
        taxiTable.setFillsViewportHeight(true);
        taxiTable.setFont(new Font("Arial", Font.PLAIN, 14));
        taxiTable.setRowHeight(30);

        JTableHeader header = taxiTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(ACCENT_COLOR);
        header.setForeground(TEXT_COLOR);

        JScrollPane tableScrollPane = new JScrollPane(taxiTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 200));
        walletPanel.add(tableScrollPane, gbc);

        contentPanel.add(walletPanel, "walletPanel");
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setPreferredSize(new Dimension(300, 300));
        notificationPanel.setBackground(PANEL_COLOR);

        notificationLabel = new JLabel("Notifications", JLabel.LEFT);
        notificationLabel.setFont(new Font("Arial", Font.BOLD, 28));
        notificationLabel.setForeground(TEXT_COLOR);
        notificationPanel.add(notificationLabel, BorderLayout.NORTH);

        notificationList = new JList<>(notificationsModel);
        notificationList.setBackground(PANEL_COLOR);
        notificationList.setForeground(TEXT_COLOR);
        notificationList.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane notificationScrollPane = new JScrollPane(notificationList);
        notificationPanel.add(notificationScrollPane, BorderLayout.CENTER);

        JButton hideNotificationsButton = new JButton("Hide Notifications");
        hideNotificationsButton.setFont(new Font("Arial", Font.BOLD, 16));
        hideNotificationsButton.setBackground(BUTTON_COLOR);
        hideNotificationsButton.setForeground(BUTTON_TEXT_COLOR);
        hideNotificationsButton.setFocusPainted(false);
        hideNotificationsButton.addActionListener(e -> {
            notificationPanel.setVisible(false);
            showNotificationsButton.setVisible(true);
        });

        showNotificationsButton = new JButton("Show Notifications");
        showNotificationsButton.setFont(new Font("Arial", Font.BOLD, 16));
        showNotificationsButton.setBackground(BUTTON_COLOR);
        showNotificationsButton.setForeground(BUTTON_TEXT_COLOR);
        showNotificationsButton.setFocusPainted(false);
        showNotificationsButton.setVisible(false);
        showNotificationsButton.addActionListener(e -> {
            notificationPanel.setVisible(true);
            showNotificationsButton.setVisible(false);
        });

        mainContainer.add(showNotificationsButton, BorderLayout.SOUTH);
        notificationPanel.add(hideNotificationsButton, BorderLayout.SOUTH);

        mainContainer.add(notificationPanel, BorderLayout.EAST);

        setContentPane(mainContainer);
        setVisible(true);

        bookTaxiButton.addActionListener(e -> {
            String initialPoint = JOptionPane.showInputDialog(this, "Enter initial point:");
            String finalDestination = JOptionPane.showInputDialog(this, "Enter final destination:");
            if (initialPoint != null && finalDestination != null && !initialPoint.isEmpty() && !finalDestination.isEmpty()) {
                currentFare = calculateEstimatedPrice(initialPoint, finalDestination);
                boolean isTaxiAvailable = checkTaxiAvailability(initialPoint, finalDestination);
                String message = "Estimated Price: R" + String.format("%.2f", currentFare) + "\nTaxi Availability: " + (isTaxiAvailable ? "Available" : "Not Available");
                JOptionPane.showMessageDialog(this, message, "Booking Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both initial and final destinations.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        nearbyTaxiButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "See Nearby Taxis..."));
        taxiRoutesButton.addActionListener(e -> {
            String initialPoint = initialPointField.getText();
            String finalDestination = finalDestinationField.getText();
            if (!initialPoint.isEmpty() && !finalDestination.isEmpty()) {
                try {
                    String encodedInitialPoint = URLEncoder.encode(initialPoint, StandardCharsets.UTF_8.toString());
                    String encodedFinalDestination = URLEncoder.encode(finalDestination, StandardCharsets.UTF_8.toString());
                    String googleMapsUrl = "https://www.google.com/maps/dir/?api=1&origin=" + encodedInitialPoint + "&destination=" + encodedFinalDestination;
                    Desktop.getDesktop().browse(new java.net.URI(googleMapsUrl));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error while opening Google Maps: " + ex.getMessage(), "Map Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both initial and final destinations.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        taxiScheduleButton.addActionListener(e -> showTaxiSchedule());
        themeSwitcher.addActionListener(e -> switchTheme());
        addMoneyButton.addActionListener(e -> adjustWalletBalance(50.0));
        withdrawMoneyButton.addActionListener(e -> {
            if (walletBalance >= currentFare) {
                adjustWalletBalance(-currentFare);
                JOptionPane.showMessageDialog(this, "Payment of R" + String.format("%.2f", currentFare) + " successful.", "Payment Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient balance. Please add more money to your wallet.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        calculateButton.addActionListener(e -> {
            String initialPoint = initialPointField.getText();
            String finalDestination = finalDestinationField.getText();
            String taxiType = (String) taxiComboBox.getSelectedItem();
            if (!initialPoint.isEmpty() && !finalDestination.isEmpty()) {
                currentFare = calculateEstimatedPrice(initialPoint, finalDestination);
                boolean isTaxiAvailable = checkTaxiAvailability(initialPoint, finalDestination);
                estimatedPriceLabel.setText("Estimated Price: R" + String.format("%.2f", currentFare) + " (" + taxiType + ")");
                taxiAvailabilityLabel.setText("Taxi Availability: " + (isTaxiAvailable ? "Available" : "Not Available"));
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both initial and final destinations.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        showMapButton.addActionListener(e -> {
            String initialPoint = initialPointField.getText();
            String finalDestination = finalDestinationField.getText();
            if (!initialPoint.isEmpty() && !finalDestination.isEmpty()) {
                try {
                    String encodedInitialPoint = URLEncoder.encode(initialPoint, StandardCharsets.UTF_8.toString());
                    String encodedFinalDestination = URLEncoder.encode(finalDestination, StandardCharsets.UTF_8.toString());
                    String googleMapsUrl = "https://www.google.com/maps/dir/?api=1&origin=" + encodedInitialPoint + "&destination=" + encodedFinalDestination;
                    Desktop.getDesktop().browse(new java.net.URI(googleMapsUrl));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error while opening Google Maps: " + ex.getMessage(), "Map Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both initial and final destinations.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loadTaxisIntoTable();
    }

    private void startClock() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                clockLabel.setText(sdf.format(new Date()));
            }
        }, 0, 1000);
    }

    private JButton createModernSidebarButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setBackground(color);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showTaxiSchedule() {
        if (taxis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No taxi schedule available.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        loadTaxisIntoTable();
    }

    private void switchTheme() {
        isLightTheme = !isLightTheme;
        Color panelBackground = isLightTheme ? BG_COLOR_LIGHT : BG_COLOR_DARK;
        Color labelForeground = TEXT_COLOR;
        Color buttonBackground = BUTTON_COLOR;
        Color buttonForeground = BUTTON_TEXT_COLOR;

        getContentPane().setBackground(panelBackground);
        for (Component component : getContentPane().getComponents()) {
            updateComponentTheme(component, panelBackground, labelForeground, buttonBackground, buttonForeground);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void updateComponentTheme(Component component, Color panelBackground, Color labelForeground, Color buttonBackground, Color buttonForeground) {
        if (component instanceof JPanel) {
            component.setBackground(panelBackground);
            for (Component child : ((JPanel) component).getComponents()) {
                updateComponentTheme(child, panelBackground, labelForeground, buttonBackground, buttonForeground);
            }
        } else if (component instanceof JLabel) {
            component.setForeground(labelForeground);
        } else if (component instanceof JButton) {
            component.setBackground(buttonBackground);
            component.setForeground(buttonForeground);
        } else if (component instanceof JTextField) {
            component.setBackground(PANEL_COLOR);
            component.setForeground(labelForeground);
        }
    }

    private void adjustWalletBalance(double amount) {
        walletBalance += amount;
        walletLabel.setText(String.format("Wallet Balance: R%.2f", walletBalance));
        JOptionPane.showMessageDialog(this, String.format("Wallet updated. New Balance: R%.2f", walletBalance), "Wallet Update", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initializeTaxis() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("taxis.ser"))) {
            taxis = (ArrayList<Taxi>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            taxis = new ArrayList<>();
            JOptionPane.showMessageDialog(this, "Failed to load taxi data. Starting with an empty list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTaxiData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("taxis.ser"))) {
            oos.writeObject(taxis);
            JOptionPane.showMessageDialog(this, "Taxi data saved successfully.", "Save Data", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save taxi data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTaxisIntoTable() {
        taxiTableModel.setRowCount(0);
        for (Taxi taxi : taxis) {
            taxiTableModel.addRow(new Object[]{taxi.getTaxiNumber(), taxi.getStartTime(), taxi.getEndTime()});
        }
    }

    private void addSampleNewsNotifications() {
        notificationsModel.addElement("Welcome to the Taxi Service App!");
        notificationsModel.addElement("Maintenance on Sunday.");
        notificationsModel.addElement("New routes to Pretoria.");
        notificationsModel.addElement("Holiday discounts available.");
        notificationsModel.addElement("Taxi availability increased on weekends.");
        notificationsModel.addElement("Introducing Luxury Taxi service.");
        notificationsModel.addElement("Safety measures updated.");
        notificationsModel.addElement("New payment options available.");
        notificationsModel.addElement("Service extended to new areas.");
        notificationsModel.addElement("Maintenance scheduled for next week.");
        notificationsModel.addElement("Special fares during rush hours.");
        notificationsModel.addElement("Driver training sessions ongoing.");
        notificationsModel.addElement("Customer satisfaction survey available.");
        notificationsModel.addElement("New partnership with local businesses.");
        notificationsModel.addElement("Emergency contact feature added.");
        notificationsModel.addElement("Real-time location sharing available.");
        notificationsModel.addElement("New app update released.");
        notificationsModel.addElement("Feedback requested for recent rides.");
        notificationsModel.addElement("Referral rewards now available.");
        notificationsModel.addElement("24/7 customer support launched.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu(100.0));
    }
}
