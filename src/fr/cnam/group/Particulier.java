package fr.cnam.group;

import java.sql.SQLException;

public class Particulier extends Utilisateur{

    public static String setParticulierStatutQuery = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(\n" +
            "    'derby.database.readOnlyAccessUsers', '%s')";

    public Particulier(String identifiant, String nom, String prenom, String date, int ref) throws Exception {
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
                    System.out.println("Particulier.registerUser() : creating login affectedRows = " + affectedRows);

                        String statutQuery = String.format(setParticulierStatutQuery, identifiant);
                        System.out.println("Particulier.registeruser() : setting authorisation level: " + statutQuery);
                        affectedRows = Main.sqlConnect.sendUpdate(statutQuery);
                        System.out.println("Particulier.registeruser() : affectedRows = " + affectedRows);
//                        if (affectedRows == 0) {
//
//                            //throw new Exception("echec selection niveau d'accès"); vérifier résultat changement properties
//                        }


                }
            }

        }
        else{ throw new Exception("Wrong date format, date should be YYYY-MM-DD");}
        return true;
    }

    public static Particulier createInvite(String identifiant) throws Exception {
        //ajouter une table invité et initialiser ref en utilisant methode generateIdentifiant
        System.out.println("creating invité...");
        int ref = 1;
        //String identifiant = generateIdentifiant("Invite", "Invite", ref);

        String accessQuery = String.format(registerUserAccessQuery, identifiant, "password");
        System.out.println("Particulier.createInvite() : query created : " + accessQuery);
        int affectedRows = Main.sqlConnect.sendUpdate(accessQuery);// enregistrer identifiants

        System.out.println("Particulier.createInvite() : creating login affectedRows = " + affectedRows);
//        if (affectedRows == 0) {
//            throw new Exception("echec enregistrement identifiants");
//        }
//        else {
            String statutQuery = String.format(setParticulierStatutQuery, identifiant, "password");
            affectedRows = Main.sqlConnect.sendUpdate(statutQuery);
        System.out.println("Particulier.createInvite() : setting authorisation level: affectedRows = " + affectedRows);
//            if (affectedRows == 0) {
//                throw new Exception("echec selection niveau d'accès");
//            }
//        }

//        String statutQuery = String.format(, identifiant, "password");
//        int affectedRows = Main.sqlConnect.sendUpdate(statutQuery);
//        if (affectedRows == 0) {
//            throw new Exception("aucun changement effectué");
//        }
        return  new Particulier(identifiant, "Invite", "Invite", "2000-01-01", ref);
    }

    @Override
    public boolean remove() throws Exception {
        return super.remove();

    }
}
