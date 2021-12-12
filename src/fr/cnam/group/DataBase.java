package fr.cnam.group;

import java.sql.Connection;
import java.sql.ResultSet;

public class DataBase {
    private ResultSet results;
    private Connection connection;

    public DataBase(SQLConnexion sqlconnect){
        //connection = sqlconnect.connect();
    }
}
