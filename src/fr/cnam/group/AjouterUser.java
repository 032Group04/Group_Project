package fr.cnam.group;



import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

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
    private JComboBox statutUserBox;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JTextField identifiantField;
    private JLabel identifiantLabel;

    public static String ajouterUserQuery = "INSERT INTO USERS (identifiant_user, nom_User, prenom_User, date_User, statut_user) " +
            "VALUES ('%s', '%s', '%s', '%s', '%s')";
    private int addedUserId;
    private Connection connection;

    private Utilisateur utilisateur;

    public AjouterUser() {
        addedUserId = 0;
        connection = Main.sqlConnect.getConnection();
        statutUserBox.addActionListener(e -> {
            if (statutUserBox.getSelectedItem().equals(Utilisateur.User_statut.Particulier.toString())){
                passwordField.setVisible(false);
                passwordLabel.setVisible(false);
            }
            else {
                passwordField.setVisible(false);
                passwordLabel.setVisible(false);
            }
        });
        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    if (nomUserField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Veuillez remplir le champ nom", "erreur", JOptionPane.WARNING_MESSAGE);
                        throw new Exception();
                    } else if (prenomUserField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Veuillez remplir le champ prénom", "erreur", JOptionPane.WARNING_MESSAGE);
                        throw new Exception();
                    }
                    else if(dateUserField.getText().isEmpty()/* && vérifier format date */) {
                        JOptionPane.showMessageDialog(null, "Veuillez remplir le champ date", "erreur", JOptionPane.WARNING_MESSAGE);
                        throw new Exception();
                    }
                    else if(passwordField.getPassword().toString().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Veuillez remplir le champ date", "erreur", JOptionPane.WARNING_MESSAGE);
                        throw new Exception();
                    }
                    else if(identifiantField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Veuillez remplir le champ identifiant", "erreur", JOptionPane.WARNING_MESSAGE);
                        throw new Exception();
                    }
                    if (statutUserBox.getSelectedItem().toString().equals(Utilisateur.User_statut.Administrateur.toString())){
//                        int affectedRows = ajouterUserQuery();
//                        Utilisateur utilisateur = new Administrateur();
//                        if (affectedRows == 0) {
//                            JOptionPane.showMessageDialog(null, "aucun changement effectué");
//                        } else {
//                            JOptionPane.showMessageDialog(null, String.format("%d colonne(s) affectée(s)", affectedRows));
//
//                        }
                        Administrateur.registerUser(identifiantField.getText(), nomUserField.getText(),prenomUserField.getText(),dateUserField.getText(), passwordField.getText());

                    }
                    else{
                        Particulier.registerUser(identifiantField.getText(),nomUserField.getText(),prenomUserField.getText(),dateUserField.getText(), passwordField.getText());
                    }


                } catch (SQLException err) {
                    JOptionPane.showMessageDialog(null, err.toString(),"erreur",JOptionPane.ERROR_MESSAGE);
                } catch (Exception er) {
                    er.printStackTrace();
                }

            }
        });
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
