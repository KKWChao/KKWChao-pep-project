package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;


public class AccountDAO {


    /** 
     * GETTING ALL ACCOUNTS
     * @return list of all accounts
     */
    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password")
                    );

                accounts.add(account);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return accounts;
    }


    /**
     * GETTING ACCOUNT BY ID
     * @param account_id -> account id
     * @return account
     */
    public Account getAccountById(int account_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password")
                    );
                return account;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }
    
    /**
     * ADDING AN ACCOUNT
     * @param account -> account input 
     * @return account created
     */
    public Account addAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        // Check password length and username length
        if (account.getPassword().length() < 4 || account.getUsername().length() == 0) {
            return null;
        } 

        try {
            // Checking if username exists
            String usernameSql = "SELECT username FROM account WHERE username = ?";
            PreparedStatement preparedUsernameStatement = connection.prepareStatement(usernameSql);

            preparedUsernameStatement.setString(1, account.username);

            ResultSet rs = preparedUsernameStatement.executeQuery();

            if (rs.next()) {
                return null;
            }

            // Adding the account
            String sql = "INSERT INTO account(username, password) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();

            ResultSet preparedStatementResultSet = preparedStatement.getGeneratedKeys();

            if (preparedStatementResultSet.next()) {
                int generated_account_id = (int) preparedStatementResultSet.getInt(1);
                return new Account(
                    generated_account_id, 
                    account.getUsername(), 
                    account.getPassword()
                    );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }

    /**
     * UPDATING AN ACCOUNT
     * @param account_id -> account id
     * @param account -> updated info
     */
    public void updateAccount(int account_id, Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE account SET username = ?, password = ? WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setInt(3, account_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }

    /**
     * LOGGING IN
     * @param account_name
     * @param account_password
     * @returns full account info
     */
    public Account loginAccount(String account_name, String account_password) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement  = connection.prepareStatement(sql);

            preparedStatement.setString(1, account_name);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password")
                    );

                // password check
                if (account.getPassword().equals(account_password)) {
                    return account;
                } else {
                    return null;
                }

            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
