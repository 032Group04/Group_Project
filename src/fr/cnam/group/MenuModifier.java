package fr.cnam.group;



import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MenuModifier {
    private JPanel modifierMainPane;

    private JPanel tacheSearchPanel;
    private JTextField nomUserField;
    private JTextField prenomUserField;
    private JTextField idUserField;
    private JComboBox prioriteTacheBox;
    private JComboBox domaineTacheBox;
    private JPanel materielSearchPanel;
    private JTextField nomMaterielField;
    private JTextField descriptionMaterielField;
    private JTextField idMaterielField;
    private JComboBox domaineMaterielBox;






    private JButton validerButton;
    private JTable resultsTable;

    private enum Step {Search, Select, change, disabled}

    ;
    private Step tacheStep;

    private JCheckBox andNomUser;
    private JCheckBox andPrenomUser;
    private JCheckBox andPrioriteTache;
    private JCheckBox andDomaineTache;

    private JTextField stepField;
    private JButton supprimerButton;


    private int selectedRow;
    private int selectedCol;
    private int selectId;
    private Connection connection;


    public MenuModifier(Connection connect) {
        connection = connect;
        //resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        validerButton.setText("chercher");
        supprimerButton.setEnabled(false);
        idUserField.setEditable(true);

        resultsTable.setVisible(true);

        tacheStep = Step.Search;
        stepField.setText("remplir le formulaire de recherche");


        validerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    /*partie taches*/


                    if (tacheStep == Step.Search) {
                        System.out.println("if step search validé");
                        search();

                        idUserField.setEditable(false);

                    } else if (tacheStep == Step.Select) {
                        System.out.println("selectTache activé");
                        selectResult();
                        showForm(); //affiche les valeurs de la tache selectionnée
                        System.out.println("step vaut: " + tacheStep.toString());

                    } else if (tacheStep == Step.change) {
                        System.out.println("modify activé");
//                            System.out.println(createUpdateQuery());

                        supprimerButton.setEnabled(false);
                        int affectedRows = sendUpdate(createUpdateQuery());

                        if (affectedRows == 0) {
                            JOptionPane.showMessageDialog(null, "aucun changement effectué");
                        } else {
                            JOptionPane.showMessageDialog(null, String.format("%d colonne(s) affectée(s)", affectedRows));

                        }

                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if ((tacheStep == Step.change) ) {

                        validerButton.setEnabled(false);
                        int affectedRows = sendUpdate(createDeleteQuery());

                        if (affectedRows == 0) {
                            JOptionPane.showMessageDialog(null, "aucun changement effectué");
                        } else {
                            JOptionPane.showMessageDialog(null, String.format("%d colonne(s) affectée(s)", affectedRows));

                        }

                    }

                } catch (SQLException err) {
                    JOptionPane.showMessageDialog(null, err.toString());
                }
            }
        });

    }



    public String createDeleteQuery() {
        String query = "";

        query = "DELETE FROM USERS \n" +
                    "WHERE REF_USER = " +
                    selectId + ";";

        return query;
    }

    public String createUpdateQuery() {
        String query = "";

            query = "UPDATE USERS \n" +
                    "SET NOM_USER = '" +
                    nomUserField.getText() +
                    "',\nPRENOM_USER = '" +
                    prenomUserField.getText() +

                    "'\nWHERE id_tache = " + selectId;


        return query;
    }

    public void search() throws SQLException {
        System.out.println("research");
        resultsTable.setVisible(true);

            if (sendQuery(createUserQuery())) {
                validerButton.setText("selectionner une tache");

                stepField.setText("Selectionner une tache et valider ou supprimer");
                tacheStep = Step.Select;
            }

    }

    public void selectResult() throws SQLException {

        selectedRow = resultsTable.getSelectedRow();
        selectedCol = resultsTable.getSelectedColumn();
        selectId = Integer.parseInt(resultsTable.getModel().getValueAt(selectedRow, 0).toString());
        //chosenField.setText(resultsTable.getModel().getValueAt(selectedRow,1).toString());


            tacheStep = Step.change;
            supprimerButton.setEnabled(true);
            stepField.setText("entrer les valeurs à modifier");

        validerButton.setText("modifier");


        /*
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tacheSearchPanel.setVisible(true);
                materielSearchPanel.setVisible(false);
                retourButton.setEnabled(false);
                resultsTable.setVisible(false);
                stepField.setText("remplir le formulaire de recherche de tache et valider.");
                step = AjoutMaterielAssocie.Step.tacheSearch;
            }
        });

         */
    }



    public void showForm() throws SQLException {
        String query = "SELECT * FROM USERS \n" +
                "WHERE REF_USER = %d;";


        query = String.format(query, selectId);
        System.out.println(query);
        sendQuery(query);

        idUserField.setText(resultsTable.getValueAt(0, 0).toString()); // colonne 0: id
        idUserField.setEditable(false);
        nomUserField.setText(resultsTable.getValueAt(0, 1).toString()); // colonne 1: nom
          //colonne 2: prenom
        prenomUserField.setText(resultsTable.getValueAt(0, 2).toString());

    }

    public String createUserQuery() {
        String fullSelect;
        System.out.println("debut creation tache");
        fullSelect = "SELECT REF_USER, PRENOM_USER, NOM_USER " +
                "FROM USERS " +
                "WHERE REF_USER = %d " +
                "%s LOCATE('%s', NOM_USER) != 0 " +
                "%s LOCATE('%s', PRENOM_USER) !=0; " ;

        String whereId = idUserField.getText();
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

        String prenom = prenomUserField.getText();
        System.out.println("prenom" + prenom + "suite");
        if (prenom.isEmpty() || prenom == "") {
            System.out.println("descript est vide");
            prenom = "///";
        }
        String opPrenom = "OR";




        if (andNomUser.isSelected()) {
            opNom = "AND";
        }
        if (andPrenomUser.isSelected()) {
            opPrenom = "AND";
        }


        return String.format(fullSelect, id, opNom, whereNom, opPrenom, prenom);
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

    public boolean sendQuery(String query) throws SQLException {
        String fullSelect;
        try (Statement statement = connection.createStatement()) {
            fullSelect = query;
            System.out.println("valeur de query dans sendQuery: " + fullSelect);
            ResultSet resultTache = statement.executeQuery(fullSelect);

            System.out.println("results.next");
            ResultsTableModel resultsTableModel = new ResultsTableModel(resultTache);

            resultsTable.setModel(resultsTableModel);
            System.out.println("rowcount: " + resultsTable.getRowCount());
            if (resultsTable.getRowCount() > 0) {
                resultsTable.setVisible(true);
                modifierMainPane.updateUI();
                return true;

            } else {
                JOptionPane.showMessageDialog(null, "aucun résultat");
                return false;
            }

        }
    }


    public JPanel getModifierMainPane() {
        return modifierMainPane;
    }

   /* public static void main(String[] args) {
        JFrame frame = new JFrame("MenuModifier");
        frame.setContentPane(new MenuModifier().modifierMainPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    String query ="UPDATE " + choiceBox.getSelectedItem().toString() +
                "\nSET "+ resultsTable.getColumnName(0) + " = %s, \n" + //id
                resultsTable.getColumnName(1) + " = '%s', \n" +         //nom
                resultsTable.getColumnName(2) + " = '%s', \n" +;

    */

}
