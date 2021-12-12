package fr.cnam.group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Utilisateur {





    static String registerQuery = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n" +
            "    'derby.user.%s', '%s')"; //1st variable : user name; 2nd variable : password
    static String removeQuery =  "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n" +
            "    'derby.user.%s', null)";// variable : user name
    private String identifiant;

    private String nom_user;
    private String prenom_user;
    private String date_user;
    private int ref_user;

    public Utilisateur(String nom, String prenom, String date, int ref) throws Exception {
        this.nom_user = nom;
        this.prenom_user = prenom;
        this.date_user = date;
        if (!isDateFormatOk(date_user)){
            throw new Exception("wrong date format; required format : YYYY-MM-DD");
        }
        this.ref_user = ref;
        this.identifiant = nom+'_'+prenom+'_'+ref;


    }

    public static ResultSet checkUserStatus(String identifiant) throws SQLException {
        String[] userDatas = identifiant.split("_");
        String checkQuery = String.format("SELECT * FROM users \n" +
                "WHERE nom_user = '%s'AND\n"+
                "prenom_user = '%s' AND \n"+
                "date_user = '%s'",userDatas[0],userDatas[1],userDatas[2]);
        ResultSet resultSet = Main.sqlConnect.sendQuery(checkQuery);
        if(resultSet.first()) {
            return resultSet;
        }
        else return null;
    }
    public boolean isDateFormatOk(String date) throws Exception {
        System.out.println("date checked : " + date);
        if (date.matches("^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d$") ||
                 date.matches("^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$")){
            return true;
        }
        else return false;
    }

    public static String formatNames(String name){
        name = name.replaceFirst("[a-zA-Z]", String.valueOf(Character.toUpperCase(name.charAt(0))));
        return name.split("\\s")[0];

    }

    public int checkId() throws SQLException {
        String selectQuery = "";
        int userId;
        ResultSet slct;

        selectQuery = "SELECT ref_user FROM USERS \nWHERE nom_user = '" + nom_user + "' AND PRENOM_USER = '" + prenom_user +
                "' AND DATE_USER = '" + date_user  + "'";

        System.out.println(selectQuery);
        try (Statement statement = Main.sqlConnect.getConnection().createStatement()) {
            slct = statement.executeQuery(selectQuery);
            if (slct.next()) {
                userId = slct.getInt("ref_user");
                System.out.println("id trouvée" + userId);
            } else {
                System.out.println("pas de next");
                userId = -1;
            }
        }
        return userId;
    }

    public abstract void register(String password) throws SQLException;
    public abstract void remove();

    public String getIdentifiant() {
        return identifiant;
    }

    public int getRef_user() {
        return ref_user;
    }

    public String getNom_user() {
        return nom_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }

    public String getPrenom_user() {
        return prenom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public String getDate_user() {
        return date_user;
    }

    public void setDate_user(String date_user) {
        this.date_user = date_user;
    }
}
