package employee.records.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Pattern;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class HRHome extends JFrame implements ActionListener {

    JTextField tfEmployeeID, tfFullName, tfJobTitle, tfDepartment, tfHireDate, tfEmploymentStatus, tfContactInfo, tfAddress;
    JButton btnCreate, btnUpdate, btnDelete, btnClear, btnLogout;
    JTable employeeTable;
    DefaultTableModel tableModel;

    HRHome() {
        getContentPane().setBackground(Color.white);
        setLayout(null);

        JLabel heading = new JLabel("Employee Management");
        heading.setBounds(150, 10, 200, 30);
        heading.setFont(new Font("Raleway", Font.BOLD, 18));
        add(heading);

        addField("Employee ID:", 30, 60, 100, 30);
        tfEmployeeID = createTextField(150, 60, 200, 30);

        addField("Full Name:", 30, 100, 100, 30);
        tfFullName = createTextField(150, 100, 200, 30);

        addField("Job Title:", 30, 140, 100, 30);
        tfJobTitle = createTextField(150, 140, 200, 30);

        addField("Department:", 30, 180, 100, 30);
        tfDepartment = createTextField(150, 180, 200, 30);

        addField("Hire Date (YYYY-MM-DD):", 30, 220, 150, 30);
        tfHireDate = createTextField(200, 220, 150, 30);

        addField("Employment Status:", 30, 260, 150, 30);
        tfEmploymentStatus = createTextField(200, 260, 150, 30);

        addField("Contact Info (Email):", 30, 300, 150, 30);
        tfContactInfo = createTextField(200, 300, 150, 30);

        addField("Address:", 30, 340, 100, 30);
        tfAddress = createTextField(150, 340, 200, 30);

         btnCreate = createButton("Create", 30, 400, 100, 30);
        btnUpdate = createButton("Update", 140, 400, 100, 30);
        btnDelete = createButton("Delete", 250, 400, 100, 30);
        btnClear = createButton("Clear", 360, 400, 100, 30);
        btnLogout = createButton("Back", 470, 400, 100, 30);
        

        tableModel = new DefaultTableModel(new String[]{"EmployeeID", "Full Name", "Job Title", "Department", "Hire Date", "Employment Status", "Contact Info", "Address"}, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && employeeTable.getSelectedRow() != -1) {
                    populateFieldsFromSelectedRow();
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        tableScrollPane.setBounds(30, 450, 540, 200);
        add(tableScrollPane);

        loadEmployeeData();

        setSize(600, 700);
        setLocation(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addField(String label, int x, int y, int width, int height) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, width, height);
        add(lbl);
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, width, height);
        add(tf);
        return tf;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, width, height);
        btn.addActionListener(this);
        add(btn);
        return btn;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnCreate) {
            createEmployee();
        } else if (ae.getSource() == btnUpdate) {
            updateEmployee();
        } else if (ae.getSource() == btnDelete) {
            deleteEmployee();
        } else if (ae.getSource() == btnClear) {
            clearFields();
        }else{
            setVisible(false);
            new Login(); 
        }
    }

    private void populateFieldsFromSelectedRow() {
        int selectedRow = employeeTable.getSelectedRow();
        tfEmployeeID.setText(tableModel.getValueAt(selectedRow, 0).toString());
        tfFullName.setText(tableModel.getValueAt(selectedRow, 1).toString());
        tfJobTitle.setText(tableModel.getValueAt(selectedRow, 2).toString());
        tfDepartment.setText(tableModel.getValueAt(selectedRow, 3).toString());
        tfHireDate.setText(tableModel.getValueAt(selectedRow, 4).toString());
        tfEmploymentStatus.setText(tableModel.getValueAt(selectedRow, 5).toString());
        tfContactInfo.setText(tableModel.getValueAt(selectedRow, 6).toString());
        tfAddress.setText(tableModel.getValueAt(selectedRow, 7).toString());
    }

    private void createEmployee() {
         if (!validateFields()) return;

        try {
            Connexion conn = new Connexion();
            if (isEmployeeIDUnique(conn, Integer.parseInt(tfEmployeeID.getText()))) {
                String query = "INSERT INTO Employee VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";
                PreparedStatement pstmt = conn.c.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(tfEmployeeID.getText()));
                pstmt.setString(2, tfFullName.getText());
                pstmt.setString(3, tfJobTitle.getText());
                pstmt.setString(4, tfDepartment.getText());
                pstmt.setString(5, tfHireDate.getText());
                pstmt.setString(6, tfEmploymentStatus.getText());
                pstmt.setString(7, tfContactInfo.getText());
                pstmt.setString(8, tfAddress.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee Created Successfully!");
                clearFields();
                loadEmployeeData();
            } else {
                JOptionPane.showMessageDialog(this, "Employee ID must be unique!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private void updateEmployee() {
         if (!validateFields()) return;

        try {
            Connexion conn = new Connexion();
            String query = "UPDATE Employee SET FullName = ?, JobTitle = ?, Department = ?, HireDate = TO_DATE(?, 'YYYY-MM-DD'), EmploymentStatus = ?, ContactInformation = ?, Address = ? WHERE EmployeeID = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setString(1, tfFullName.getText());
            pstmt.setString(2, tfJobTitle.getText());
            pstmt.setString(3, tfDepartment.getText());
            pstmt.setString(4, tfHireDate.getText());
            pstmt.setString(5, tfEmploymentStatus.getText());
            pstmt.setString(6, tfContactInfo.getText());
            pstmt.setString(7, tfAddress.getText());
            pstmt.setInt(8, Integer.parseInt(tfEmployeeID.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Updated Successfully!");
            loadEmployeeData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private void deleteEmployee() {
        try {
            Connexion conn = new Connexion();
            String query = "DELETE FROM Employee WHERE EmployeeID = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(tfEmployeeID.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Deleted Successfully!");
            clearFields();
            loadEmployeeData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private void clearFields() {
        tfEmployeeID.setText("");
        tfFullName.setText("");
        tfJobTitle.setText("");
        tfDepartment.setText("");
        tfHireDate.setText("");
        tfEmploymentStatus.setText("");
        tfContactInfo.setText("");
        tfAddress.setText("");
    }

    private boolean validateFields() {
        if (tfEmployeeID.getText().isEmpty() || tfFullName.getText().isEmpty() || tfJobTitle.getText().isEmpty() ||
                tfDepartment.getText().isEmpty() || tfHireDate.getText().isEmpty() || tfEmploymentStatus.getText().isEmpty() ||
                tfContactInfo.getText().isEmpty() || tfAddress.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!");
            return false;
        }

        if (!Pattern.matches("\\d+", tfEmployeeID.getText())) {
            JOptionPane.showMessageDialog(this, "Employee ID must be a valid number!");
            return false;
        }

        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", tfHireDate.getText())) {
            JOptionPane.showMessageDialog(this, "Hire Date must be in YYYY-MM-DD format!");
            return false;
        }

        if (!Pattern.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", tfContactInfo.getText())) {
            JOptionPane.showMessageDialog(this, "Contact Info must be a valid email!");
            return false;
        }

        return true;
    }

    private boolean isEmployeeIDUnique(Connexion conn, int employeeID) {
        try {
            String query = "SELECT EmployeeID FROM Employee WHERE EmployeeID = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();
            return !rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
            return false;
        }
    }

    private void loadEmployeeData() {
        try {
            tableModel.setRowCount(0); // Clear existing data
            Connexion conn = new Connexion();
            String query = "SELECT * FROM Employee";
            Statement stmt = conn.c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getString("JobTitle"),
                        rs.getString("Department"),
                        rs.getDate("HireDate"),
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

    public static void main(String[] args) {
        new HRHome();
    }
}

