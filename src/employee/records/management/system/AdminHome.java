package employee.records.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminHome extends JFrame implements ActionListener {

    JButton user, employee, auditTrail, reporting, search, logout;

    AdminHome() {

        // Set up the frame
        getContentPane().setBackground(Color.white);
        setLayout(null);

        // Heading label
        JLabel heading = new JLabel("Employee Management System");
        heading.setBounds(70, 20, 300, 30);
        heading.setFont(new Font("Raleway", Font.BOLD, 20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        add(heading);

        // Create and add buttons
        user = createButton("Users", 100, 80);
        employee = createButton("Employees", 100, 140);
        auditTrail = createButton("Audit Trail", 100, 200);
        reporting = createButton("Reporting", 100, 260);
        search = createButton("Search", 100, 320);
        logout = createButton("Logout", 100, 380);

        // Set frame size and location
        setSize(420, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to create buttons with consistent styles
    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 200, 40);
        button.setFont(new Font("Raleway", Font.PLAIN, 16));
        button.setBackground(new Color(173, 216, 230)); // Light blue background
        button.setFocusPainted(false);
        button.addActionListener(this);
        add(button);
        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == user) {
            setVisible(false);
            new Users(); // Navigate to Users page
        } else if (ae.getSource() == employee) {
            setVisible(false);
            new Employees(); // Navigate to Employees page
        } else if (ae.getSource() == auditTrail) {
            setVisible(false);
            new AuditTrail(); // Navigate to Audit Trail page
        } else if (ae.getSource() == reporting) {
            setVisible(false);
            new Reporting(); // Navigate to Reporting page
        } else if (ae.getSource() == search) {
            setVisible(false);
            new SearchEmployee(); // Navigate to Search Employee page
        } else if (ae.getSource() == logout) {
            setVisible(false);
            new Login(); // Navigate to Login page
        }
    }

    public static void main(String[] args) {
        new AdminHome();
    }
}
