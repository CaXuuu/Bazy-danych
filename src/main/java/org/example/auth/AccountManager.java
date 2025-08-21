package org.example.auth;

import org.example.database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class AccountManager {
    private final DatabaseConnection db;

    public AccountManager(DatabaseConnection db) {
        this.db = db;
    }

    // Dodawanie nowego użytkownika
    public int register(String name, String password) throws SQLException {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO account (name, password) VALUES (?, ?)";
        PreparedStatement stmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, name);
        stmt.setString(2, hashed);
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1); // zwraca id nowego użytkownika
        else return -1;
    }

    // Weryfikacja loginu i hasła
    public boolean authenticate(String name, String password) throws SQLException {
        String sql = "SELECT password FROM account WHERE name = ?";
        PreparedStatement stmt = db.getConnection().prepareStatement(sql);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String hash = rs.getString("password");
            return BCrypt.checkpw(password, hash);
        }
        return false;
    }

    // Pobieranie użytkownika po nazwie
    public Account getAccount(String name) throws SQLException {
        String sql = "SELECT * FROM account WHERE name = ?";
        PreparedStatement stmt = db.getConnection().prepareStatement(sql);
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Account(rs.getInt("id"), rs.getString("name"));
        }
        return null;
    }

    // Pobieranie użytkownika po id
    public Account getAccount(int id) throws SQLException {
        String sql = "SELECT * FROM account WHERE id = ?";
        PreparedStatement stmt = db.getConnection().prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Account(rs.getInt("id"), rs.getString("name"));
        }
        return null;
    }
}