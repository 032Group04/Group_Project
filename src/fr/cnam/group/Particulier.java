package fr.cnam.group;

import java.sql.SQLException;

public class Particulier extends Utilisateur{

    private String setStatutQuery = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n" +
            "    'derby.database.readOnlyAccessUsers', '%s')";

    public Particulier(String nom, String prenom, String date, int ref) throws Exception {
        super(nom, prenom, date, ref);
    }


    @Override
    public void register(String password) throws SQLException {
        super.register(password);
    }

    @Override
    public void remove() {
        String query = String.format(removeQuery,super.getIdentifiant());
    }
}
