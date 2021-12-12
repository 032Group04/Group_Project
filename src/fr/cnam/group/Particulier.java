package fr.cnam.group;

import java.sql.SQLException;

public class Particulier extends Utilisateur{

    private String setParticulierQuery = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n" +
            "    'derby.database.readOnlyAccessUsers', '%s')";

    public Particulier(String nom, String prenom, String date, int ref) throws Exception {
        super(nom, prenom, date, ref);
    }


    @Override
    public void register(String password) throws SQLException {
        String query = String.format(registerQuery,super.getIdentifiant(),password);
        int test = Main.sqlConnect.sendUpdate(query) ;
        System.out.println("return value of  register update query : " + test);
        query = String.format(setParticulierQuery, super.getIdentifiant());
        test = Main.sqlConnect.sendUpdate(query);
        System.out.println("return value of restriction settings update query : " + test);
    }

    @Override
    public void remove() {
        String query = String.format(removeQuery,super.getIdentifiant());
    }
}
