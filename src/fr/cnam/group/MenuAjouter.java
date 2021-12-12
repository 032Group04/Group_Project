package fr.cnam.group;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class MenuAjouter {
    private JButton ajouterUserButton;
    private JPanel PanelAjouter;
    private JButton ajouterAnnuaireButton;
    private JTextPane ajouterTextPane;
    private JButton associerMaterielButton;

    public MenuAjouter() {


    }



    public JButton getAjouterUserButton() {
        return ajouterUserButton;
    }

    public JButton getAjouterAnnuaireButton() {
        return ajouterAnnuaireButton;
    }

    public JTextPane getAjouterTextPane() {
        return ajouterTextPane;
    }

    public JPanel getPanelAjouter() {
        return PanelAjouter;
    }



}
