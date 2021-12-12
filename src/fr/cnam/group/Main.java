package fr.cnam.group;

import java.lang.reflect.InvocationTargetException;

public class Main {
    SQLConnexion sqlConnexion;
    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.sqlConnexion = new SQLConnexion();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        main.sqlConnexion.connect(SQLConnexion.host ,"admin","password");


    }
}
