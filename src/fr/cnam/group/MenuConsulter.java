package fr.cnam.group;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MenuConsulter {
    private JPanel consultPane;


    private JPanel userSearchPanel;
    private JTextField nomUserField;
    private JTextField prenomField;
    private JTextField refUserField;
    private JFormattedTextField dateUserField;
    private JComboBox statutUserBox;
    private JCheckBox andPrenomUser;
    private JCheckBox andStatutUser;
    private JCheckBox andNomUser;
    private JCheckBox andDateUser;



    private JTable resultsTable;
    private JComboBox choiceBox;
    private JButton validerButton;




    private JTextField thanksField;
    private JCheckBox selectAllBox;


    private Connection connection;



    private enum Choice {user, annuaire}

    ;
    private Choice choice;
    private String fullSelect;
    private ResultSet resultUser;
    private String trySelect = "SELECT * FROM USERS";
    private boolean firstQuery;


    public MenuConsulter(Connection connect) {
        firstQuery = true;
        resultsTable.setVisible(false);
        resultsTable.setAutoCreateRowSorter(true);
        connection = connect;
        choice = Choice.user;
        choiceBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (choiceBox.getSelectedItem().toString() == "user") {


                    userSearchPanel.setVisible(true);
                    choice = Choice.user;
                } /*else if (choiceBox.getSelectedItem().toString() == "annuaire") {

                    userSearchPanel.setVisible(false);
                    AnnuaireSearchPanel.setVisible(true);
                    choice = Choice.annuaire;

                }*/
            }
        });

        selectAllBox.addActionListener((listener) ->{
            if (selectAllBox.isSelected()){
                userSearchPanel.setVisible(false);
            }
            else{
                userSearchPanel.setVisible(true);
            }
        });

        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    System.out.println("bouton valider activé");
                    if (choice == Choice.user ) {
                        sendQuery( (selectAllBox.isSelected()) ? trySelect : createUserQuery());

                        //sendQuery("SELECT * FROM USERS");
                    }



                    /* else if (choice == Choice.annuaire) {

                        sendQuery(createAnnuaireQuery());
                    }*/


                } catch (SQLException err) {
                    System.out.println("erreur levée au bouton valider");
                    JOptionPane.showMessageDialog(null, err.toString());
                }
//                    queryTache="SELECT * " +
//                            "FROM tache ";
//                            /*whereId +
//                            whereNom +
//                            whereDomaine +
//                            priorite +
//                            description; */


            }
        });
    }


    public String createUserQuery() {
        System.out.println("debut creation query utilisateur");

        fullSelect = "SELECT * " +
                "FROM USERS " +
                "WHERE ref_user = %d " +
                "%s LOCATE('%s', nom_user) != 0 " +
                "%s LOCATE('%s', prenom_user) !=0 " +
                "%s date_user = '%s' " +
                "%s statut_user = '%s' ";

        String whereId = refUserField.getText();
        int id = 0;
        try {
            id = Integer.parseInt(whereId);
        } catch (NumberFormatException err) {
            id = -1;

        }

        String whereNom = nomUserField.getText();
        if (whereNom == "" || whereNom.isEmpty()) {
            whereNom = "///";

        }
        String opNom = "OR";

        String wherePrenom = prenomField.getText();
        System.out.println("wherePrenom" + wherePrenom + "suite");
        if (wherePrenom.isEmpty() || wherePrenom == "") {
            System.out.println("prénom est vide");
            wherePrenom = "///";
        }
        String opPrenom = "OR";

        String whereDate = dateUserField.getText();
        if(whereDate.isEmpty()){
            whereDate = "01/01/2000";
        }
        String opDate = "OR";

        String whereStatut = statutUserBox.getSelectedItem().toString();
        if (whereStatut.isEmpty()) System.out.println("whereStatut est vide");
        String opStatut = "OR";


        if (andNomUser.isSelected()) {
            opNom = "AND";
        }
        if (andPrenomUser.isSelected()) {
            opPrenom = "AND";
        }
        if (andDateUser.isSelected()) {
            opDate = "AND";
        }
        if (andStatutUser.isSelected()) {
            opStatut = "AND";
        }

        return String.format(fullSelect, id, opNom, whereNom, opPrenom, wherePrenom, opDate, whereDate, opStatut, whereStatut);
    }

    public void sendQuery(String query) throws SQLException {
        showThanks();
        try (Statement statement = connection.createStatement()) {
            fullSelect = query;
            System.out.println(fullSelect);
            ResultSet resultTache = statement.executeQuery(fullSelect);
            ResultsTableModel resultsTableModel = new ResultsTableModel(resultTache);
            resultsTable.setModel(resultsTableModel);
            resultsTable.setVisible(true);
            consultPane.updateUI();


        }
    }

    public void showThanks() {
        if (firstQuery) {
            Timer t = new Timer(5000, null);
            t.setRepeats(false);
            thanksField.setVisible(true);
            t.start();
            t.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    thanksField.setVisible(false);
                    firstQuery = false;
                }
            });
        }
    }

   // à modifier pour annuaire

    /*public String createAnnuaireQuery() {
        System.out.println("debut creation materiel");
        fullSelect = "SELECT * " +
                "FROM materiel " +
                "WHERE id_materiel = %d " +
                "%s LOCATE('%s', nom_materiel) != 0 " +
                "%s LOCATE('%s', taille_materiel) != 0 " +
                "%s LOCATE('%s', description_materiel) !=0 " +
                "%s nom_domaine = '%s' ";

        String whereId = idMaterielField.getText();
        int id;
        try {
            id = Integer.parseInt(whereId);
        } catch (NumberFormatException err) {
            id = -1;
        }

        String opNom = "OR";
        String whereNom = nomMaterielField.getText();
        if (whereNom == "" || whereNom.isEmpty()) {
            whereNom = "///";
        }

        String opTaille = "OR";
        String whereTaille = tailleMaterielField.getText();
        if (whereTaille == "" || whereTaille.isEmpty()) {
            whereTaille = "///";
        }

        String opDescription = "OR";
        String description = descriptionMaterielField.getText();
        System.out.println("description" + description + "suite");
        if (description.isEmpty() || description == "") {
            System.out.println("descript est vide");
            description = "///";
        }

        String opDomaine = "OR";
        String whereDomaine = domaineMaterielBox.getSelectedItem().toString();
        if (whereDomaine.isEmpty()) System.out.println("wheredomaine est vide");

        String opRenouvelable = "OR";
        String renouvelable;

        if (andNomMateriel.isSelected()) {
            opNom = "AND";
        }
        if (andTailleMateriel.isSelected()) {
            opTaille = "AND";
        }
        if (andDescriptionMateriel.isSelected()) {
            opDescription = "AND";
        }
        if (andDomaineMateriel.isSelected()) {
            opDomaine = "AND";
        }
        if (andRenouvelable.isSelected()) {
            opRenouvelable = "AND";
        }

        if (renouvelableCheckBox.isSelected()) {
            if (trueRadioButton.isSelected()) {
                renouvelable = "true";
            } else {
                renouvelable = "false";
            }
            fullSelect += "%s renouvelable = %s ; ";
            return String.format(fullSelect, id, opNom, whereNom, opTaille, whereTaille, opDescription, description, opDomaine, whereDomaine, opRenouvelable, renouvelable);
        } else {
            fullSelect += ";";
            return String.format(fullSelect, id, opNom, whereNom, opTaille, whereTaille, opDescription, description, opDomaine, whereDomaine);
        }


    }*/

    public JPanel getConsultPane() {
        return consultPane;
    }

    public JComboBox getChoiceBox() {
        return choiceBox;
    }

    public JButton getValiderButton() {
        return validerButton;
    }
}
