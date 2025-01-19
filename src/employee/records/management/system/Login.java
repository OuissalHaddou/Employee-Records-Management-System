
package employee.records.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class Login extends JFrame implements ActionListener{
    
    JTextField tfUsername;
            
    JPasswordField tfPassword;
    
    Login() {
    
        getContentPane().setBackground(Color.white);
        setLayout(null);
        
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(40, 20, 100, 30);
        add(lblUsername);
        
        tfUsername = new JTextField();
        tfUsername.setBounds(150, 20, 150, 30);
        add(tfUsername);
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(40, 70, 100, 30);
        add(lblPassword);
        
        tfPassword = new JPasswordField();
        tfPassword.setBounds(150, 70, 150, 30);
        add(tfPassword);
        
        JButton login = new JButton("LOGIN");
        login.setBounds(150, 120, 100, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        add(login);
        
        setSize(400, 200);
        setLocation(450, 200);
        setVisible(true);
    
    }
    public void actionPerformed(ActionEvent ae) {
    try {
        String username = tfUsername.getText().trim();
        String password = new String(tfPassword.getPassword()).trim();

        System.out.println("Debug - Username: " + username);
        System.out.println("Debug - Password: " + password);

        Connexion conn = new Connexion();
        String query = "SELECT * FROM EUSER WHERE USERNAME = ? AND PASSWORD = ?";
        PreparedStatement pstmt = conn.c.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String role = rs.getString("Role");
            System.out.println("Debug - Role: " + role);
            setVisible(false);

            switch (role) {
                case "HR":
                    new HRHome();
                    break;
                case "Manager":
                    new ManagerHome(username);
                    break;
                case "Admin":
                    new AdminHome();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid role");
                    setVisible(true);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password");
            tfUsername.setText("");
            tfPassword.setText("");
            setVisible(true);
        }
    } catch (Exception e) {
        System.err.println("Error occurred: " + e.getMessage());
        e.printStackTrace();
    }
}


    
    public static void main(String[] args) {
        new Login();
    }
    
}
