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
    private JButton validerButton;
    private JTextField thanksField;
    private JCheckBox selectAllBox;
    private Connection connection;
    private String fullSelect;

    private String trySelect = "SELECT * FROM USERS";
    private boolean firstQuery;


    public MenuConsulter(Connection connect) {
        firstQuery = true;
        resultsTable.setVisible(false);
        resultsTable.setAutoCreateRowSorter(true);

        connection = connect;



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

                    showThanks();
                    System.out.println("autoCommit is :" + Main.sqlConnect.getConnection().getAutoCommit());


                    sendQuery((selectAllBox.isSelected()) ? trySelect : createUserQuery() );



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



    public JPanel getConsultPane() {
        return consultPane;
    }



    public JButton getValiderButton() {
        return validerButton;
    }
}
