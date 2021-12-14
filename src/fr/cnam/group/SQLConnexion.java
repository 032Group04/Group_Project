package fr.cnam.group;

import org.apache.derby.jdbc.EmbeddedDriver;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class SQLConnexion extends WindowAdapter implements ActionListener {

    public String driverPath = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String defaultURL = "jdbc:derby:database";
    private ConnectDialog dialog;

    private String identifiant;
    private String password;
    private String invite;

    private Connection connection;

    private Utilisateur currentUser;

    public SQLConnexion()  {
        EmbeddedDriver driver = new EmbeddedDriver();
        currentUser = null;
        connection = null;
        invite = null;
    }

    public void openConnect(MyWindow owner){
        dialog = new ConnectDialog(this,owner);
        dialog.pack();
        dialog.setVisible(true);
    }


    public boolean connect(String url,String user, String password) {
        try{
            connection = DriverManager.getConnection(url, user, password);
            if (connection.isValid(0)) {
                connection.setSchema("GROUP_DB");
                //connection.setAutoCommit(false);
                if(user.equals("admin") && invite != null) {
                    System.out.println("temporary connection as admin : success");

                }
                else{
                    JOptionPane.showMessageDialog(null, user+" : connexion réussie", "succès",
                            JOptionPane.WARNING_MESSAGE);
                    dialog.dispose();
                }

            }
        }catch(SQLException err){
            JOptionPane.showMessageDialog(null,"erreur de connexion","erreur",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;


    }

    public void removeInvite() throws Exception {
        connection.close();
        if(connect(defaultURL,"admin","adminPassword")){
            sendUpdate(String.format(Utilisateur.removeQuery,invite));
        }
        else{
            throw new Exception("erreur lors de l'accès admin à la suppression de l'invité");
        }
    }


    public void EndConnection(){
        try {
            System.out.println("end connection");

            if(connection!= null && connection.isValid(0) ){
                if (invite != null){
                    System.out.println("removing invite");
                    removeInvite();
                    invite = null;
                }
                connection.close();
            }


        } catch(SQLException e){
            System.err.println(e.getMessage());

        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
        }
        finally {

            if (invite != null) {
                System.out.println("déconnexion réussie; invite set to null");
                invite = null;
            }
            else{
                JOptionPane.showMessageDialog(null, "deconnexion réussie","déconnexion réussie",JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }
    public void EndConnection(boolean quitting){
        try {
            System.out.println("end connection");

            if(connection!= null && connection.isValid(0) ){
                if (invite != null){
                    System.out.println("removing invite");
                    removeInvite();
                    invite = null;
                }
                connection.close();
            }


        } catch(SQLException e){
            System.err.println(e.getMessage());

        }catch(Exception e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
        }
        finally {

            System.out.println("deconnexion réussie");

        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement sendQuery(String query) throws SQLException {
        System.out.println("sendQuery() : query = " +query);
        Statement statement = connection.createStatement();

            System.out.println(query);
            ResultSet resultTache = statement.executeQuery(query);

            return statement;




    }
    public int sendUpdate(String query) throws SQLException {
        System.out.println("sendUpdate() : query = " +query);
        String fullSelect;
        int result;
        try (Statement statement = connection.createStatement()) {
            fullSelect = query;
            //System.out.println(fullSelect);
            result = statement.executeUpdate(fullSelect);
            return result;
        }
    }

    public boolean isIdentifiantAvailable(String identifiant) throws Exception {
        String checkQuery = String.format("SELECT * FROM users \n"+
                "WHERE identifiant_user = '%s'", identifiant);
        Statement statement = sendQuery(checkQuery);
        ResultSet slct = statement.getResultSet();
        if(slct.next()){
            throw new Exception(slct.getString("identifiant_user")+ " is already taken");
        }
        return true;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("dialog event");
        /*
        connection à la base de données
         */

        if (e.getSource() == dialog.getButtonOK()) {

            if (dialog.getInviteBox().isSelected() ){
                System.out.println("connecting as invité");
                try {
                    if (dialog.getUserField().getText().isEmpty()){
                        JOptionPane.showMessageDialog(null,"vous devez saisir un identifiant");
                    }
                    else {

                        invite = dialog.getUserField().getText();
                        if(connect(defaultURL,"admin","adminPassword")){

                           currentUser = Particulier.createInvite(invite);
                           connection.close();

                           System.out.println("invité created ; currentUser is : " + currentUser.getIdentifiant_user()+ " as " + currentUser.getClass().getSimpleName());

                        }
                        else{

                            throw new Exception("fail connecting as invite");
                        }


                        if (connect(defaultURL, currentUser.getIdentifiant_user(), "password")) {
                            System.out.println("connection as invité is valid");
                            System.out.println("currentUser is : " + currentUser.getIdentifiant_user()+ " as " + currentUser.getClass().getSimpleName());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    System.out.println("removing invite's login : " + invite);
                    try {
                        sendUpdate(String.format(Utilisateur.removeQuery,invite));
                    } catch (SQLException exc) {
                        exc.printStackTrace();
                    }
                    finally {
                        EndConnection();
                    }


                    JOptionPane.showMessageDialog(null, ex.getMessage(), "erreur",
                            JOptionPane.WARNING_MESSAGE);
                }


            }
            else {
                identifiant = dialog.getUserField().getText();
                password = dialog.getPasswordField().getText();

                if (connect(defaultURL, identifiant, password)) {
                    System.out.println("connection is valid");

                    /*
                    si connection réussie, vérification du statut de l'utilisateur dans la base de données.
                     */
                    try {
                        if (identifiant.equals("admin")) { // utilisateur root pour tests
                            System.out.println("root user admin");
                            currentUser = new RootUser();
                        } else {
                            currentUser = Utilisateur.createUserFromDataBase(identifiant);
                        }
                        if (currentUser == null) {
                            throw new Exception("erreur d'authentification");

                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        EndConnection();
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "erreur",
                                JOptionPane.WARNING_MESSAGE);
                    }

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

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("window closing");
        EndConnection(true);



    }
}
