package fr.cnam.group;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnexion {

    public String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String host = "jdbc:derby:database";
    private Connection connection;
    public SQLConnexion() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName(driver).newInstance();
    }


    public void connect(String url,String user, String password) {
        try{
            connection = DriverManager.getConnection(url, user, password);
            if (connection.isValid(0)) {
                JOptionPane.showMessageDialog(null,"connexion réussie","succès",
                        JOptionPane.WARNING_MESSAGE);
            }
        }catch(SQLException err){
            JOptionPane.showMessageDialog(null,"erreur de connexion","erreur",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public void EndConnection(){
        try {
            System.out.println("end connection");
            connection.close();
            if(connection.isClosed()){
                System.out.println("déconnexion réussie");
            }
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

}
