package employee.records.management.system;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ManagerUpdateEmployee extends JFrame implements ActionListener {
    JTextField tfJobTitle, tfEmploymentStatus;
    JButton btnSave, btnBack;
    int employeeId;
    String username;

    ManagerUpdateEmployee(int employeeId, String username) {
        this.employeeId = employeeId;
        this.username = username;

        setLayout(null);

        JLabel heading = new JLabel("Update Employee Details");
        heading.setBounds(150, 20, 300, 30);
        add(heading);

        JLabel lblJobTitle = new JLabel("Job Title:");
        lblJobTitle.setBounds(50, 80, 100, 30);
        add(lblJobTitle);

        tfJobTitle = new JTextField();
        tfJobTitle.setBounds(150, 80, 200, 30);
        add(tfJobTitle);

        JLabel lblEmploymentStatus = new JLabel("Employment Status:");
        lblEmploymentStatus.setBounds(50, 130, 150, 30);
        add(lblEmploymentStatus);

        tfEmploymentStatus = new JTextField();
        tfEmploymentStatus.setBounds(200, 130, 200, 30);
        add(tfEmploymentStatus);

        btnSave = new JButton("Save");
        btnSave.setBounds(100, 200, 100, 30);
        btnSave.addActionListener(this);
        add(btnSave);

        btnBack = new JButton("Back");
        btnBack.setBounds(250, 200, 100, 30);
        btnBack.addActionListener(this);
        add(btnBack);

        // Load current employee details
        loadEmployeeDetails();

        setSize(500, 300);
        setLocation(500, 300);
        setVisible(true);
    }

    private void loadEmployeeDetails() {
        try {
            Connexion conn = new Connexion();

            String query = "SELECT JobTitle, EmploymentStatus FROM Employee WHERE EmployeeID = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setInt(1, employeeId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tfJobTitle.setText(rs.getString("JobTitle"));
                tfEmploymentStatus.setText(rs.getString("EmploymentStatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSave) {
            try {
                Connexion conn = new Connexion();

                String query = "UPDATE Employee SET JobTitle = ?, EmploymentStatus = ? WHERE EmployeeID = ?";
                PreparedStatement pstmt = conn.c.prepareStatement(query);
                pstmt.setString(1, tfJobTitle.getText());
                pstmt.setString(2, tfEmploymentStatus.getText());
                pstmt.setInt(3, employeeId);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
                setVisible(false);
                new ManagerEmployeeView(username);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            new ManagerEmployeeView(username);
        }
    }
}
