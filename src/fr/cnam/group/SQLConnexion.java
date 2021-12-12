package fr.cnam.group;

import org.apache.derby.jdbc.EmbeddedDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import org.apache.derby.jdbc.EmbeddedDriver;
import java.sql.*;

public class SQLConnexion implements ActionListener {

    public String driverPath = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String defaultURL = "jdbc:derby:database";
    private ConnectDialog dialog;
    private String host;
    private String database;
    private String user;
    private String password;
    private String url;
    private Connection connection;

    public SQLConnexion()  {
        EmbeddedDriver driver = new EmbeddedDriver();

        connection = null;
    }

    public void openConnect(MyWindow owner){
        dialog = new ConnectDialog(this);
        dialog.pack();
        dialog.setVisible(true);
    }


    public Connection connect(String url,String user, String password) {
        try{
            connection = DriverManager.getConnection(url, user, password);
            if (connection.isValid(0)) {
                connection.setSchema("GROUP_DB");
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

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("dialog event");
        if (e.getSource() == dialog.getButtonOK()) {
//            host = dialog.getHostField().getText();
//            database = dialog.getDatabaseField().getText();
//            user = dialog.getUserField().getText();
//            password = dialog.getPasswordField().getText();
//            url = host + database + "?useSSL=false&serverTimezone=UTC";
//            connect(url, user, password);
            connect(SQLConnexion.defaultURL ,"admin","password");
        }
        else if(e.getSource() == dialog.getButtonDisconnect()){
            EndConnection();
        }
    }
}
