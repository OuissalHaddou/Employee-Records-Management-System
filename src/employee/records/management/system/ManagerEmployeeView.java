package employee.records.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class ManagerEmployeeView extends JFrame implements ActionListener {
    JTable table;
    DefaultTableModel model;
    JButton btnUpdate, btnBack;
    String username;

    ManagerEmployeeView(String username) {
        this.username = username;

        setLayout(null);

        JLabel heading = new JLabel("Employees in Your Department");
        heading.setBounds(150, 20, 300, 30);
        add(heading);

        // Table to display employees
        String[] columnNames = {"Employee ID", "Full Name", "Job Title", "Employment Status"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 80, 600, 300);
        add(sp);

        // Buttons
        btnUpdate = new JButton("Update Employee");
        btnUpdate.setBounds(100, 400, 150, 30);
        btnUpdate.addActionListener(this);
        add(btnUpdate);

        btnBack = new JButton("Back");
        btnBack.setBounds(300, 400, 150, 30);
        btnBack.addActionListener(this);
        add(btnBack);

        // Load employees in manager's department
        loadEmployees();

        setSize(700, 500);
        setLocation(400, 200);
        setVisible(true);
    }

    private void loadEmployees() {
        try {
            Connexion conn = new Connexion();

            // Query to fetch employees in the manager's department
            String query = "SELECT * FROM Employee WHERE Department = (SELECT Department FROM EUser WHERE Username = ?)";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            model.setRowCount(0); // Clear table
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getString("JobTitle"),
                        rs.getString("EmploymentStatus")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnUpdate) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int employeeId = (int) model.getValueAt(selectedRow, 0);
                setVisible(false);
                new ManagerUpdateEmployee(employeeId, username); // Navigate to update form
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to update.");
            }
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            new ManagerHome(username);
        }
    }
}
