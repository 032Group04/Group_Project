package fr.cnam.group;

import javax.sql.DataSource;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class SQLConnexion {

    public String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String host = "jdbc:derby:database";
    private Connection connection;
    public SQLConnexion() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class.forName(driver).getConstructor().newInstance();
        connection = null;
    }


    public Connection connect(String url,String user, String password) {
        try{
            connection = DriverManager.getConnection(url, user, password);
            if (connection.isValid(0)) {
                JOptionPane.showMessageDialog(null,"connexion réussie","succès",
                        JOptionPane.WARNING_MESSAGE);

            }
        }catch(SQLException err){
            JOptionPane.showMessageDialog(null,"erreur de connexion","erreur",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return connection;


    }
    public Connection connect() {
        try{
            connection = DriverManager.getConnection(host,"admin","password");
            if (connection.isValid(0)) {
                JOptionPane.showMessageDialog(null,"connexion réussie","succès",
                        JOptionPane.WARNING_MESSAGE);

            }
        }catch(SQLException err){
            JOptionPane.showMessageDialog(null,"erreur de connexion","erreur",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return connection;


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
