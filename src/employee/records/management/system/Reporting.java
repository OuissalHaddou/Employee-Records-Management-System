package employee.records.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Reporting extends JFrame {
    JTable table;
    DefaultTableModel model;

    Reporting() {
        getContentPane().setBackground(Color.white);
        setLayout(null);

        JLabel heading = new JLabel("Reports");
        heading.setBounds(200, 10, 300, 30);
        heading.setFont(new Font("Raleway", Font.BOLD, 18));
        add(heading);

        String[] columnNames = {"Report Type", "Details"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 60, 540, 300);
        add(sp);

        // Generate reports
        generateReports();

        JButton back = new JButton("Back");
        back.setBounds(250, 380, 100, 30);
        back.addActionListener(e -> {
            setVisible(false);
            new AdminHome();
        });
        add(back);

        setSize(600, 450);
        setLocation(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void generateReports() {
        try {
            Connexion conn = new Connexion();

            // Example Report: Number of employees per department
            String departmentReportQuery = "SELECT Department, COUNT(*) AS EmployeeCount FROM Employee GROUP BY Department";
            PreparedStatement deptStmt = conn.c.prepareStatement(departmentReportQuery);
            ResultSet deptRs = deptStmt.executeQuery();
            model.addRow(new Object[]{"Employees per Department", ""});
            while (deptRs.next()) {
                model.addRow(new Object[]{
                        "   " + deptRs.getString("Department"),
                        deptRs.getInt("EmployeeCount") + " employees"
                });
            }

            // Example Report: Number of employees by status
            String statusReportQuery = "SELECT EmploymentStatus, COUNT(*) AS EmployeeCount FROM Employee GROUP BY EmploymentStatus";
            PreparedStatement statusStmt = conn.c.prepareStatement(statusReportQuery);
            ResultSet statusRs = statusStmt.executeQuery();
            model.addRow(new Object[]{"Employees by Employment Status", ""});
            while (statusRs.next()) {
                model.addRow(new Object[]{
                        "   " + statusRs.getString("EmploymentStatus"),
                        statusRs.getInt("EmployeeCount") + " employees"
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating reports: " + e.getMessage());
        }
    }
}
