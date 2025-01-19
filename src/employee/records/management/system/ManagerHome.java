package employee.records.management.system;

import javax.swing.*;
import java.awt.event.*;

public class ManagerHome extends JFrame implements ActionListener {
    JButton btnViewEmployees, btnLogout;
    String username; 

    ManagerHome(String username) {
        this.username = username; 

        setLayout(null);

        JLabel heading = new JLabel("Manager Home");
        heading.setBounds(150, 20, 200, 30);
        add(heading);

        btnViewEmployees = new JButton("View My Department");
        btnViewEmployees.setBounds(100, 80, 200, 30);
        btnViewEmployees.addActionListener(this);
        add(btnViewEmployees);

        btnLogout = new JButton("Logout");
        btnLogout.setBounds(100, 130, 200, 30);
        btnLogout.addActionListener(this);
        add(btnLogout);

        setSize(400, 300);
        setLocation(500, 300);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnViewEmployees) {
            setVisible(false);
            new ManagerEmployeeView(username); 
        } else if (ae.getSource() == btnLogout) {
            setVisible(false);
            new Login(); 
        }
    }
}
