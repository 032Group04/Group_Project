package fr.cnam.group;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class MenuPrincipal {
    private JPanel menuPrincipal;
    private JButton consulterButton;
    private JTextArea title;
    private JButton modifierButton;
    private JButton ajouterButton;
    private JButton requestButton;
    private JButton quitterButton;
    private JButton connecterButton;

    public JButton getConnecterButton() {
        return connecterButton;
    }

    public void setConnecterButton(JButton connecterButton) {
        this.connecterButton = connecterButton;
    }

    public JPanel getMenuPrincipal() {
        return menuPrincipal;
    }

    public JButton getConsulterButton() {
        return consulterButton;
    }

    public JTextArea getTitle() {
        return title;
    }

    public JButton getModifierButton() {
        return modifierButton;
    }

    public JButton getAjouterButton() {
        return ajouterButton;
    }

    public JButton getRequestButton() {
        return requestButton;
    }

    public JButton getQuitterButton() {
        return quitterButton;
    }


}
