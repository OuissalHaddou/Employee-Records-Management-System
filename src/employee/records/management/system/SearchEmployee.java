package employee.records.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class SearchEmployee extends JFrame implements ActionListener {
    JTextField tfSearch;
    JButton btnSearch, btnBack; 
    JTable table;
    DefaultTableModel model;
    JComboBox<String> cbEmploymentStatusFilter, cbDepartmentFilter, cbHireDateFilter;

    SearchEmployee() {
        getContentPane().setBackground(Color.white);
        setLayout(null);

        JLabel heading = new JLabel("Employee Management System");
        heading.setBounds(200, 10, 300, 25); 
        heading.setFont(new Font("Raleway", Font.BOLD, 16)); 
        add(heading);

        
        addField("Search:", 30, 50, 80, 25);
        tfSearch = createTextField(110, 50, 150, 25);
        btnSearch = createButton("Search", 270, 50, 90, 25);

       
        addField("Employment Status:", 30, 90, 120, 25);
        cbEmploymentStatusFilter = createComboBox(new String[]{"All", "Full-time", "Part-time"}, 150, 90, 100, 25);

        addField("Department:", 260, 90, 90, 25);
        cbDepartmentFilter = createComboBox(new String[]{"All", "HR", "Finance", "IT", "Sales", "Marketing", "Operations"}, 350, 90, 100, 25);

        addField("Hire Date (Year):", 30, 130, 120, 25);
        cbHireDateFilter = createComboBox(new String[]{"All","2025","2024", "2023", "2022", "2021", "2020"}, 150, 130, 100, 25);

        
        String[] columnNames = {"ID", "Name", "Job", "Dept.", "Hire Date", "Status", "Contact", "Address"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 170, 540, 300); 
        add(sp);

        
        btnBack = createButton("Back", 30, 480, 90, 25); 

        
        setSize(600, 550); 
        setLocation(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSearch) {
            searchEmployee();
        } else if (ae.getSource() == btnBack) {
            setVisible(false);
            new AdminHome(); 
        }
    }

    private void searchEmployee() {
        try {
            Connexion conn = new Connexion();
            String query = "SELECT * FROM Employee WHERE 1=1";

            if (!tfSearch.getText().isEmpty()) {
                query += " AND (EmployeeID LIKE ? OR FullName LIKE ? OR Department LIKE ? OR JobTitle LIKE ?)";
            }

            if (!cbEmploymentStatusFilter.getSelectedItem().toString().equals("All")) {
                query += " AND EmploymentStatus = ?";
            }

            if (!cbDepartmentFilter.getSelectedItem().toString().equals("All")) {
                query += " AND Department = ?";
            }

            if (!cbHireDateFilter.getSelectedItem().toString().equals("All")) {
                query += " AND EXTRACT(YEAR FROM HireDate) = ?";
            }

            PreparedStatement pstmt = conn.c.prepareStatement(query);
            int paramIndex = 1;

            if (!tfSearch.getText().isEmpty()) {
                String search = "%" + tfSearch.getText().trim() + "%";
                pstmt.setString(paramIndex++, search);
                pstmt.setString(paramIndex++, search);
                pstmt.setString(paramIndex++, search);
                pstmt.setString(paramIndex++, search);
            }

            if (!cbEmploymentStatusFilter.getSelectedItem().toString().equals("All")) {
                pstmt.setString(paramIndex++, cbEmploymentStatusFilter.getSelectedItem().toString());
            }

            if (!cbDepartmentFilter.getSelectedItem().toString().equals("All")) {
                pstmt.setString(paramIndex++, cbDepartmentFilter.getSelectedItem().toString());
            }

            if (!cbHireDateFilter.getSelectedItem().toString().equals("All")) {
                pstmt.setString(paramIndex++, cbHireDateFilter.getSelectedItem().toString());
            }

            ResultSet rs = pstmt.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getString("JobTitle"),
                        rs.getString("Department"),
                        rs.getString("HireDate"),
                        rs.getString("EmploymentStatus"),
                        rs.getString("ContactInformation"),
                        rs.getString("Address")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addField(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        add(label);
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        add(textField);
        return textField;
    }

    private JComboBox<String> createComboBox(String[] items, int x, int y, int width, int height) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBounds(x, y, width, height);
        add(comboBox);
        return comboBox;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        add(button);
        button.addActionListener(this);
        return button;
    }

    public static void main(String[] args) {
        new SearchEmployee();
    }
}
