package org.apollo.apollowebcrud.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ApolloConnection {
    protected static Connection connection;
    public ApolloConnection(){
        if(connection==null){
            try{
                Dotenv dotenv = Dotenv.load();
                Class.forName("org.postgresql.Driver");
                String DATABASE_URL = dotenv.get("DATABASE_URL");
                String us = dotenv.get("USER_DB");
                String pass = dotenv.get("PASSWORD_DB");
                connection = DriverManager.getConnection(DATABASE_URL, us, pass);
            } catch (ClassNotFoundException | SQLException ex){
                Logger.getLogger(ApolloConnection.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException("Falha na conexão do banco de dados", ex);
            }
        }
    }
    public Connection getConnection() {
        return connection;
    }
}
