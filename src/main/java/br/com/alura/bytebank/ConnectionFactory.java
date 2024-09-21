package br.com.alura.bytebank;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection recuperarConexao() {
        Dotenv dotenv = Dotenv.load();

        String DB_HOST = dotenv.get("DB_HOST");
        String DB_PORT = dotenv.get("DB_PORT");
        String DB_NAME = dotenv.get("DB_NAME");
        String DB_USER = dotenv.get("DB_USER");
        String DB_PASSWORD = dotenv.get("DB_PASSWORD");

        try {
            return DriverManager
                    .getConnection(String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s",
                            DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
