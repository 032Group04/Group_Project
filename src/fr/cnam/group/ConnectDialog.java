package fr.cnam.group;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ConnectDialog extends JDialog {
    private JPanel connexionPane;
    private JButton buttonOK;
    private JButton buttonDisconnect;
    private JTextField databaseField;
    private JTextField userField;
    private JTextPane connexionTextPane;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JLabel userLabel;
    private JLabel databaseLabel;
    private JTextField hostField;
    private JLabel hostLabel;
    private JButton validerButton;

    public ConnectDialog(SQLConnexion sqlConnect) {
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        setContentPane(connexionPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(sqlConnect);

        buttonDisconnect.addActionListener(sqlConnect);


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        // call onCancel() on ESCAPE
        connexionPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    public JPanel getConnexionPane() {
        return connexionPane;
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    public JButton getButtonDisconnect() {
        return buttonDisconnect;
    }

    public JTextField getDatabaseField() {
        return databaseField;
    }

    public JTextField getHostField() {
        return hostField;
    }

    public JTextField getUserField() {
        return userField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }



}
