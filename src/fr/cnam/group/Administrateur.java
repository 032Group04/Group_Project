package fr.cnam.group;

import java.sql.SQLException;

public class Administrateur extends Utilisateur{

    private String setStatutQuery = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n" +
            "    'derby.database.fullAccessUsers', '%s')";

    public Administrateur(String nom, String prenom, String date, int ref) throws Exception {
        super(nom, prenom, date, ref);
    }


    @Override
    public void register(String password) throws SQLException {
        super.register(password);


    }

    @Override
    public void remove() {

    }
}
