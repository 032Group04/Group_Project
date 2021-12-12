package fr.cnam.group;

public class Main {
    SQLConnexion sqlConnexion;
    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.sqlConnexion = new SQLConnexion();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        main.sqlConnexion.connect(SQLConnexion.host ,"admin","password");


    }
}
