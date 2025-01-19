
package employee.records.management.system;


import java.sql.*;


public class Connexion {
    
    public Connection c;
    public Connexion(){
               
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String uname = "system";
            String upass = "123456";
            c = DriverManager.getConnection(url, uname, upass);
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return c;
    }

    public void closeConnection() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
