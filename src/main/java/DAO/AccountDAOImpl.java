package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import Model.Account;
public class AccountDAOImpl implements AccountDAO {
    private final Connection connection;

    public AccountDAOImpl(Connection connection) {
        this.connection = connection;
    }
    

    @Override
    public boolean userExistsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }    


    @Override
    public Account registerAccount(Account account) {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.username);
            statement.setString(2, account.password);
    
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return new Account(generatedId, account.username, account.password);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Account loginAccount(String username, String password) {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int accountId = resultSet.getInt("account_id");
                    return new Account(accountId, username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
        @Override
    public boolean userExistsById(int userId) {
        String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}