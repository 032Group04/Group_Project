package fr.cnam.group;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {
    SQLConnexion sqlConnexion;
    public static void main(String[] args) throws Exception {
//        Main main = new Main();
//        try {
//            main.sqlConnexion = new SQLConnexion();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        main.sqlConnexion.connect(SQLConnexion.defaultURL ,"admin","password");

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        MyWindow myWindow = new MyWindow();



        TopMenu topMenu = new TopMenu();
        topMenu.getReturnToMain().setVisible(false);
        MenuPrincipal menu1 = new MenuPrincipal();
        myWindow.setJMenuBar(topMenu);

        myWindow.setVisible(true);
        myWindow.pack();
        /*ouverture du menu principal*/


        myWindow.setContentPane(menu1.getMenuPrincipal());
        myWindow.setMinimumSize(new Dimension(600,600));

        SQLConnexion sqlConnect = new SQLConnexion();
        //ouverture de la connexion à la bd


        //bouton connexion
        menu1.getConnecterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sqlConnect.openConnect(myWindow);
                //menu1.getConnecterButton().setText("déconnexion");


            }

        });

        menu1.getConsulterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (sqlConnect.getConnection() != null && sqlConnect.getConnection().isValid(0)) {

                        topMenu.getReturnToMain().setVisible(true);
                        topMenu.getReturnToMain().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                myWindow.setContentPane(menu1.getMenuPrincipal());
                                topMenu.getReturnToMain().setVisible(false);
                            }
                        });
                        System.out.println("test 1");
                        MenuConsulter menuConsulter = new MenuConsulter(sqlConnect.getConnection());
                        System.out.println("test 2");
                        myWindow.setContentPane(menuConsulter.getConsultPane());
                    } else {
                        JOptionPane.showMessageDialog(null, "vous n'êtes pas connecté à une base de données");
                    }
                }catch (SQLException err){
                    JOptionPane.showMessageDialog(null,err.toString());
                }
            }
        });


        menu1.getAjouterButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (sqlConnect.getConnection() != null && sqlConnect.getConnection().isValid(0)) {

                        topMenu.getReturnToMain().setVisible(true);
                        topMenu.getReturnToMain().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                myWindow.setContentPane(menu1.getMenuPrincipal());
                                topMenu.getReturnToMain().setVisible(false);
                            }
                        });
                        /*menu ajouter une tache ou un materiel*/
                        MenuAjouter menuAjouter = new MenuAjouter();
                        myWindow.setContentPane(menuAjouter.getPanelAjouter());

                        menuAjouter.getAjouterUserButton().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                /*ouverture du menu ajouter un utilisateur*/
                                AjouterUser ajouterUser = new AjouterUser(sqlConnect.getConnection());
                                myWindow.setContentPane(ajouterUser.getPanelAjouterUser());
                                ajouterUser.getValiderButton().addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try{
                                            int affectedRows = ajouterUser.ajouterUserQuery();

                                            if(affectedRows ==0){
                                                JOptionPane.showMessageDialog(null,"aucun changement effectué");
                                            }
                                            else{
                                                JOptionPane.showMessageDialog(null,String.format("%d colonne(s) affectée(s)", affectedRows));

                                            }
                                        } catch (SQLException err){
                                            JOptionPane.showMessageDialog(null, err.toString());
                                        }

                                    }
                                });
                            }
                        });


                        // a transformer en ajouter entrée annuaire

//                        menuAjouter.getAjouterMaterielButton().addActionListener(new ActionListener() {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//
//                                /*menu ajouter materiel*/
//                                AjouterMateriel ajouterMateriel = new AjouterMateriel();
//                                myWindow.setContentPane(ajouterMateriel.getAjouterMaterielPanel());
//                                ajouterMateriel.getValider().addActionListener(new ActionListener() {
//                                    @Override
//                                    public void actionPerformed(ActionEvent e) {
//                                        try{
//                                            int[] affectedRows = ajouterMateriel.ajouterMaterielQuery(sqlConnect.getConnection());
//                                            int materielResult = affectedRows[0];
//                                            int stockResult = affectedRows[1];
//                                            if(materielResult ==0 && stockResult == 0){
//                                                JOptionPane.showMessageDialog(null,"aucun changement effectué");
//                                            }
//                                            else if (stockResult > 0 && materielResult != 0){
//                                                JOptionPane.showMessageDialog(null,String.format("%d colonne(s) materiel affectée(s) \n%d colonne(s) stock affectées.",
//                                                        materielResult, stockResult));
//                                            }
//                                            else if(materielResult !=0 && stockResult == -1){
//                                                JOptionPane.showMessageDialog(null,String.format("%d colonne(s) materiel affectée(s).", materielResult));
//                                            }
//                                        } catch (SQLException err){
//                                            JOptionPane.showMessageDialog(null, err.toString());
//                                        }
//                                    }
//                                });
//                            }
//                        });//fin du listener menu ajouter materiel


                    } else {
                        JOptionPane.showMessageDialog(null, "vous n'êtes pas connecté à une base de données");
                    }
                }catch (SQLException err){
                    JOptionPane.showMessageDialog(null,err.toString());
                }
            }
        });//fin du listener menu ajouter




        menu1.getQuitterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sqlConnect.getConnection() == null){
                    System.exit(0);
                }
                else {
                    sqlConnect.EndConnection();
                    System.exit(0);
                }
            }
        });
        topMenu.getQuit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sqlConnect.getConnection() == null){
                    System.exit(0);
                }
                else {
                    sqlConnect.EndConnection();
                    System.exit(0);
                }
            }
        });


    }
}
