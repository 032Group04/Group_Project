package fr.cnam.group;

public class Administrateur extends Utilisateur{

    public static String setAdministrateurStatutQuery = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n" +
            "    'derby.database.fullAccessUsers', '%s')";

    public Administrateur(String identifiant, String nom, String prenom, String date, int ref) throws Exception {
        super(identifiant, nom, prenom, date, ref);
    }

    public static boolean registerUser(String identifiant, String nom, String prenom,String date,String password) throws Exception {
        if(isDateFormatOk(date)) {
            String registerQuery = String.format(AjouterUser.ajouterUserQuery, nom, prenom, date, User_statut.Administrateur); // ajout dans la bd
            int affectedRows = Main.sqlConnect.sendUpdate(registerQuery);
            if (affectedRows == 0) {
                throw new Exception("echec de l'ajout dans la base de données");
            } else {


                System.out.println(String.format("Particulier.registeruser() : %d colonne(s) affectée(s)", affectedRows));
                int ref = checkId(nom,prenom,date);
                if (ref > 0) {

                    String accessQuery = String.format(registerUserAccessQuery, identifiant, password);
                    affectedRows = Main.sqlConnect.sendUpdate(accessQuery);// enregistrer identifiants
                    System.out.println("Administrateur.registerUser() : creating login affectedRows = " + affectedRows);
//                    if (affectedRows == 0) {
//                        throw new Exception("echec enregistrement identifiants");
//                    }
//                    else {
                        String statutQuery = String.format(setAdministrateurStatutQuery, identifiant);
                        System.out.println("Administrateur.registerUser() : authoriusation query = " + statutQuery);
                        affectedRows = Main.sqlConnect.sendUpdate(statutQuery);
                        System.out.println("Administrateur.registerUser() : setting authorisation level: affectedRows = " + affectedRows);
//                        if (affectedRows == 0) {
//
//                            //throw new Exception("echec selection niveau d'accès"); vérifier résultat changement properties
//                        }
//                    }

                }
            }

        }
        else{ throw new Exception("Wrong date format, date should be YYYY-MM-DD");}
        return true;
    }



    @Override
    public boolean remove() throws Exception {
        return super.remove();


    }
}
