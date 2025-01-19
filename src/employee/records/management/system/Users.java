package employee.records.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Pattern;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Users extends JFrame implements ActionListener {

    JTextField tfUserID, tfUsername, tfPassword, tfRole, tfDepartment;
    JButton btnCreate, btnUpdate, btnDelete, btnClear, btnBack;
    JTable userTable;
    DefaultTableModel tableModel;

    Users() {
        getContentPane().setBackground(Color.white);
        setLayout(null);

        JLabel heading = new JLabel("User Management");
        heading.setBounds(150, 10, 200, 30);
        heading.setFont(new Font("Raleway", Font.BOLD, 18));
        add(heading);

        addField("User ID:", 30, 60, 100, 30);
        tfUserID = createTextField(150, 60, 200, 30);

        addField("Username:", 30, 100, 100, 30);
        tfUsername = createTextField(150, 100, 200, 30);

        addField("Password:", 30, 140, 100, 30);
        tfPassword = createTextField(150, 140, 200, 30);

        addField("Role:", 30, 180, 100, 30);
        tfRole = createTextField(150, 180, 200, 30);

        addField("Department:", 30, 220, 100, 30);
        tfDepartment = createTextField(150, 220, 200, 30);

        btnCreate = createButton("Create", 30, 280, 100, 30);
        btnUpdate = createButton("Update", 140, 280, 100, 30);
        btnDelete = createButton("Delete", 250, 280, 100, 30);
        btnClear = createButton("Clear", 360, 280, 100, 30);
        btnBack = createButton("Back", 470, 280, 100, 30);
        btnBack.addActionListener(e -> backToPreviousForm());

        tableModel = new DefaultTableModel(new String[]{"UserID", "Username", "Password", "Role", "Department"}, 0);
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                    populateFieldsFromSelectedRow();
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setBounds(30, 330, 540, 200);
        add(tableScrollPane);

        loadUserData();

        setSize(600, 600);
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
            createUser();
        } else if (ae.getSource() == btnUpdate) {
            updateUser();
        } else if (ae.getSource() == btnDelete) {
            deleteUser();
        } else if (ae.getSource() == btnClear) {
            clearFields();
        }
    }

    private void populateFieldsFromSelectedRow() {
        int selectedRow = userTable.getSelectedRow();
        tfUserID.setText(tableModel.getValueAt(selectedRow, 0).toString());
        tfUsername.setText(tableModel.getValueAt(selectedRow, 1).toString());
        tfPassword.setText(tableModel.getValueAt(selectedRow, 2).toString());
        tfRole.setText(tableModel.getValueAt(selectedRow, 3).toString());
        tfDepartment.setText(tableModel.getValueAt(selectedRow, 4).toString());
    }

    private void backToPreviousForm() {
        new AdminHome().setVisible(true);
        this.dispose();
    }

    private void createUser() {
        if (!validateFields()) return;

        try {
            Connexion conn = new Connexion();
            if (isUserIDUnique(conn, Integer.parseInt(tfUserID.getText()))) {
                String query = "INSERT INTO EUser VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.c.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(tfUserID.getText()));
                pstmt.setString(2, tfUsername.getText());
                pstmt.setString(3, tfPassword.getText());
                pstmt.setString(4, tfRole.getText());
                pstmt.setString(5, tfDepartment.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "User Created Successfully!");
                clearFields();
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "User ID must be unique!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateUser() {
        if (!validateFields()) return;

        try {
            Connexion conn = new Connexion();
            String query = "UPDATE EUser SET Username = ?, Password = ?, Role = ?, Department = ? WHERE UserID = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setString(1, tfUsername.getText());
            pstmt.setString(2, tfPassword.getText());
            pstmt.setString(3, tfRole.getText());
            pstmt.setString(4, tfDepartment.getText());
            pstmt.setInt(5, Integer.parseInt(tfUserID.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User Updated Successfully!");
            loadUserData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            Connexion conn = new Connexion();
            String query = "DELETE FROM EUser WHERE UserID = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(tfUserID.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User Deleted Successfully!");
            clearFields();
            loadUserData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void clearFields() {
        tfUserID.setText("");
        tfUsername.setText("");
        tfPassword.setText("");
        tfRole.setText("");
        tfDepartment.setText("");
    }

    private boolean validateFields() {
        if (tfUserID.getText().isEmpty() || tfUsername.getText().isEmpty() || tfPassword.getText().isEmpty() ||
                tfRole.getText().isEmpty() || tfDepartment.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!");
            return false;
        }

        if (!Pattern.matches("\\d+", tfUserID.getText())) {
            JOptionPane.showMessageDialog(this, "User ID must be a valid number!");
            return false;
        }

        return true;
    }

    private boolean isUserIDUnique(Connexion conn, int userID) {
        try {
            String query = "SELECT UserID FROM EUser WHERE UserID = ?";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            return !rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
            return false;
        }
    }

    private void loadUserData() {
        try {
            tableModel.setRowCount(0); // Clear existing data
            Connexion conn = new Connexion();
            String query = "SELECT * FROM EUser";
            Statement stmt = conn.c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getString("Department")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Users();
    }
}
