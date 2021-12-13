package fr.cnam.group;

import org.apache.derby.jdbc.EmbeddedDriver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private Utilisateur currentUser;

    public SQLConnexion()  {
        EmbeddedDriver driver = new EmbeddedDriver();

        connection = null;
    }

    public void openConnect(MyWindow owner){
        dialog = new ConnectDialog(this);
        dialog.pack();
        dialog.setVisible(true);
    }


    public boolean connect(String url,String user, String password) {
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
            return false;
        }

        return true;


    }
    public boolean connect() {
        try{
            connection = DriverManager.getConnection(host,"admin","password");
            if (connection.isValid(0)) {
                JOptionPane.showMessageDialog(null,"connexion réussie","succès",
                        JOptionPane.WARNING_MESSAGE);

            }
        }catch(SQLException err){
            JOptionPane.showMessageDialog(null,"erreur de connexion","erreur",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;


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

    public ResultSet sendQuery(String query) throws SQLException {

        try (Statement statement = connection.createStatement()) {

            System.out.println(query);
            ResultSet resultTache = statement.executeQuery(query);
            return resultTache;



        }
    }
    public int sendUpdate(String query) throws SQLException {

        String fullSelect;
        int result;
        try (Statement statement = connection.createStatement()) {
            fullSelect = query;
            System.out.println(fullSelect);
            result = statement.executeUpdate(fullSelect);
            return result;
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("dialog event");
        /*
        connection à la base de données
         */

        if (e.getSource() == dialog.getButtonOK()) {

            user = dialog.getUserField().getText();
            password = dialog.getPasswordField().getText();

            if(connect(SQLConnexion.defaultURL ,user,password)) {
                System.out.println("connection is valid");

                    /*
                    si connection réussie, vérification du statut de l'utilisateur dans la base de données.
                     */
                try {
                    if (user.equals("admin")) { // utilisateur root pour tests
                        System.out.println("root user admin");
                        currentUser = new RootUser();
                    } else {
                        Utilisateur.createUserFromDataBase(user);
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    EndConnection();
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "erreur",
                            JOptionPane.WARNING_MESSAGE);
                }

            }
        }
        else if(e.getSource() == dialog.getButtonDisconnect()){
            EndConnection();
        }
    }

    public Utilisateur getCurrentUser() {
        return currentUser;
    }
}
