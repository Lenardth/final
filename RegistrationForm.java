import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;

public class RegistrationForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField nameField, emailField, phoneField, addressField, usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JCheckBox termsCheckBox, newsletterCheckBox;
    private JComboBox<String> genderComboBox, countryComboBox;
    private JRadioButton studentRadioButton, employeeRadioButton;
    private ButtonGroup occupationGroup;
    private HashMap<String, String> users;

    public RegistrationForm() {
        setTitle("User Registration");
        setSize(500, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        users = loadUsers();  // Load users from file

        JPanel registrationPanel = new GradientPanel();  // Use custom JPanel here
        registrationPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name Field
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setForeground(UIManager.getColor("Label.foreground"));
        nameField = new JTextField(15);
        nameField.setToolTipText("Enter your full name");

        gbc.gridx = 0;
        gbc.gridy = 0;
        registrationPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        registrationPanel.add(nameField, gbc);

        // Username Field
        JLabel usernameLabel = new JLabel("Enter your username:");
        usernameLabel.setForeground(UIManager.getColor("Label.foreground"));
        usernameField = new JTextField(15);
        usernameField.setToolTipText("Enter a unique username");

        gbc.gridx = 0;
        gbc.gridy = 1;
        registrationPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        registrationPanel.add(usernameField, gbc);

        // Email Field
        JLabel emailLabel = new JLabel("Enter your email:");
        emailLabel.setForeground(UIManager.getColor("Label.foreground"));
        emailField = new JTextField(15);
        emailField.setToolTipText("Enter a valid email address");

        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        registrationPanel.add(emailField, gbc);

        // Phone Number Field
        JLabel phoneLabel = new JLabel("Enter your phone number:");
        phoneLabel.setForeground(UIManager.getColor("Label.foreground"));
        phoneField = new JTextField(15);
        phoneField.setToolTipText("Enter your phone number");

        gbc.gridx = 0;
        gbc.gridy = 3;
        registrationPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        registrationPanel.add(phoneField, gbc);

        // Address Field
        JLabel addressLabel = new JLabel("Enter your address:");
        addressLabel.setForeground(UIManager.getColor("Label.foreground"));
        addressField = new JTextField(15);
        addressField.setToolTipText("Enter your address");

        gbc.gridx = 0;
        gbc.gridy = 4;
        registrationPanel.add(addressLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        registrationPanel.add(addressField, gbc);

        // Gender Selection
        JLabel genderLabel = new JLabel("Select your gender:");
        genderLabel.setForeground(UIManager.getColor("Label.foreground"));
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setToolTipText("Select your gender");

        gbc.gridx = 0;
        gbc.gridy = 5;
        registrationPanel.add(genderLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        registrationPanel.add(genderComboBox, gbc);

        // Country Selection
        JLabel countryLabel = new JLabel("Select your country:");
        countryLabel.setText("Select your city:");
        countryLabel.setForeground(UIManager.getColor("Label.foreground"));
        String[] countries = {"South Africa - Cape Town", "South Africa - Johannesburg", "South Africa - Durban", "South Africa - Pretoria", "South Africa - Bloemfontein"};
        countryComboBox = new JComboBox<>(countries);
        countryComboBox.setToolTipText("Select your city");

        gbc.gridx = 0;
        gbc.gridy = 6;
        registrationPanel.add(countryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        registrationPanel.add(countryComboBox, gbc);

        // Occupation Radio Buttons
        JLabel occupationLabel = new JLabel("Select your occupation:");
        occupationLabel.setForeground(UIManager.getColor("Label.foreground"));
        studentRadioButton = new JRadioButton("Student");
        employeeRadioButton = new JRadioButton("Employee");
        occupationGroup = new ButtonGroup();
        occupationGroup.add(studentRadioButton);
        occupationGroup.add(employeeRadioButton);
        studentRadioButton.setOpaque(false);
        employeeRadioButton.setOpaque(false);

        gbc.gridx = 0;
        gbc.gridy = 7;
        registrationPanel.add(occupationLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        registrationPanel.add(studentRadioButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        registrationPanel.add(employeeRadioButton, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Create a password:");
        passwordLabel.setForeground(UIManager.getColor("Label.foreground"));
        passwordField = new JPasswordField(15);
        passwordField.setToolTipText("Create a strong password");

        gbc.gridx = 0;
        gbc.gridy = 9;
        registrationPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        registrationPanel.add(passwordField, gbc);

        // Confirm Password Field
        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        confirmPasswordLabel.setForeground(UIManager.getColor("Label.foreground"));
        confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setToolTipText("Retype your password");

        gbc.gridx = 0;
        gbc.gridy = 10;
        registrationPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        registrationPanel.add(confirmPasswordField, gbc);

        // Terms and Conditions Checkbox
        termsCheckBox = new JCheckBox("I agree to the terms and conditions");
        termsCheckBox.setOpaque(false);
        termsCheckBox.setForeground(UIManager.getColor("Label.foreground"));

        gbc.gridx = 1;
        gbc.gridy = 11;
        registrationPanel.add(termsCheckBox, gbc);

        // Newsletter Subscription Checkbox
        newsletterCheckBox = new JCheckBox("Subscribe to our newsletter");
        newsletterCheckBox.setOpaque(false);
        newsletterCheckBox.setForeground(UIManager.getColor("Label.foreground"));

        gbc.gridx = 1;
        gbc.gridy = 12;
        registrationPanel.add(newsletterCheckBox, gbc);

        // Register Button
        RoundedButton registerButton = new RoundedButton("Register Now", UIManager.getColor("Button.background"), UIManager.getColor("Button.foreground"));
        registerButton.setToolTipText("Click to complete registration");

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(UIManager.getColor("Button.highlight"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(UIManager.getColor("Button.background"));
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 13;
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        registerButton.setForeground(UIManager.getColor("Button.foreground"));
        registrationPanel.add(registerButton, gbc);

        setContentPane(registrationPanel);
        setVisible(true);
    }

    // Method to handle registration
    private void registerUser() {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String nameRegex = "^[a-zA-Z ]+$";
        String phoneRegex = "^[0-9]{10}$";
        String passwordRegex = ".{4,}";  // Password must be at least 4 characters long

        String name = nameField.getText();
        if (!name.matches(nameRegex)) {
            JOptionPane.showMessageDialog(this, "Name can only contain letters and spaces.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = usernameField.getText();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Invalid Username", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (users.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another one.", "Username Taken", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String email = emailField.getText();
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String phone = phoneField.getText();
        if (!phone.matches(phoneRegex)) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits and cannot contain special characters or letters.", "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.matches(passwordRegex)) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!termsCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(this, "You must agree to the terms and conditions to register.", "Terms Not Accepted", JOptionPane.WARNING_MESSAGE);
            return;
        }

        users.put(username, password);
        saveUsers();
        JOptionPane.showMessageDialog(this, "Registration successful!\nName: " + name + "\nUsername: " + username + "\nEmail: " + email);
        this.dispose();  // Close registration form
        new LoginForm();  // Open login form after registration
    }

    // Save users to .ser file
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.ser"))) {
            oos.writeObject(users);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving users data!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load users from .ser file
    private HashMap<String, String> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.ser"))) {
            System.out.println("Attempting to load users from file...");
            return (HashMap<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found, creating new users HashMap...");
            return new HashMap<>();  // No users file yet
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users data!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationForm());
    }
}
