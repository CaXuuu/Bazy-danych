package org.example;

import org.example.database.DatabaseConnection;
import org.example.auth.AccountManager;
import org.example.auth.Account;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection connection = new DatabaseConnection();
        try {
            connection.connect("database.sql");
            createTable(connection);

            AccountManager manager = new AccountManager(connection);

            int id = manager.register("adam", "mojeHaslo123");
            System.out.println("Nowy id: " + id);

            boolean ok = manager.authenticate("adam", "mojeHaslo123");
            System.out.println("Autentykacja poprawna? " + ok);

            Account acc = manager.getAccount("adam");
            System.out.println("Account (po nazwie): " + acc);

            Account acc2 = manager.getAccount(id);
            System.out.println("Account (po id): " + acc2);

            connection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(DatabaseConnection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS account (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL)";
        connection.getConnection().prepareStatement(sql).execute();
    }
}