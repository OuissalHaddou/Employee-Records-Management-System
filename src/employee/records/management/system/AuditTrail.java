package employee.records.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AuditTrail extends JFrame {
    JTable table;
    DefaultTableModel model;

    AuditTrail() {
        getContentPane().setBackground(Color.white);
        setLayout(null);

        JLabel heading = new JLabel("Audit Trail");
        heading.setBounds(200, 10, 300, 30);
        heading.setFont(new Font("Raleway", Font.BOLD, 18));
        add(heading);

        String[] columnNames = {"Change ID", "Changed By", "Change Description", "Date"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 60, 540, 300);
        add(sp);

        // Load data from database
        loadAuditTrail();

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

    private void loadAuditTrail() {
        try {
            Connexion conn = new Connexion();
            String query = "SELECT ChangeID, ChangedBy, ChangeDescription, ChangeDate FROM AuditTrail";
            PreparedStatement pstmt = conn.c.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ChangeID"),
                        rs.getString("ChangedBy"),
                        rs.getString("ChangeDescription"),
                        rs.getString("ChangeDate")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading audit trail: " + e.getMessage());
        }
    }
}
