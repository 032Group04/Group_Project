package fr.cnam.group;



import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AjouterUser {
    private JPanel PanelAjouterUser;
    private JTextPane ajouterUserTextPane;
    private JTextField nomUserField;
    private JButton validerButton;
    private JLabel nomUserLabel;
    private JTextField prenomUserField;
    private JLabel prenomUserLabel;

    private JFormattedTextField dateUserField;
    private JLabel dateLabel;

    private String query;
    private int addedUserId;
    private Connection connection;

    private Utilisateur utilisateur;

    public AjouterUser() {
        addedUserId = 0;
        connection = Main.sqlConnect.getConnection();
        query = "INSERT INTO USERS (nom_User, prenom_User, date_User) " +
                "VALUES ('%s', '%s', '%s')";
    }

    public int ajouterUserQuery() throws SQLException {
        if (nomUserField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir le champ nom", "erreur", JOptionPane.WARNING_MESSAGE);
            return 0;
        } else if (prenomUserField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir le champ prénom", "erreur", JOptionPane.WARNING_MESSAGE);
            return 0;
        }
        else if(dateUserField.getText().isEmpty()/* && vérifier format date */) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir le champ date", "erreur", JOptionPane.WARNING_MESSAGE);
            return 0;
        }
        else{
            try (Statement statement = connection.createStatement()) {

                query = String.format(query, nomUserField.getText(), prenomUserField.getText(),
                        dateUserField.getText());
                System.out.println(query);
                return statement.executeUpdate(query);


            }
        }
    }

    public int checkId() throws SQLException {
        String selectQuery = "";
        ResultSet slct;

        selectQuery = "SELECT ref_user FROM USERS \nWHERE nom_user = '" + nomUserField.getText() + "' AND PRENOM_USER = '" + prenomUserField.getText() +
                    "' AND DATE_USER = '" + dateUserField.getText()  + "';";

        System.out.println(selectQuery);
        try (Statement statement = connection.createStatement()) {
            slct = statement.executeQuery(selectQuery);
            if (slct.next()) {
                addedUserId = slct.getInt("ref_user");
                System.out.println("id trouvée" + addedUserId);
            } else {
                System.out.println("pas de next");
                addedUserId = -1;
            }
        }
        return addedUserId;
    }



    public JTextField getNomUserField() {
        return nomUserField;
    }

    public JButton getValiderButton() {
        return validerButton;
    }

    public JTextField getPrenomUserField() {
        return prenomUserField;
    }

    public JPanel getPanelAjouterUser() {
        return PanelAjouterUser;
    }



}
