package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public Account registerAccount(Account account){
        Connection conn = ConnectionUtil.getConnection();

        //Check if username and password are large enough
        if(account.getUsername().length()>0 && account.getPassword().length() >=4 ){
            try {
                String sqlQuery = "SELECT * FROM account WHERE username = ?";
                PreparedStatement prepQuery = conn.prepareStatement(sqlQuery);
                prepQuery.setString(1,account.getUsername());
                ResultSet QueryResult = prepQuery.executeQuery();

                //Check that the username does not already exist
                if(QueryResult.next()==false){
                    String sql = "INSERT INTO account (username, password) VALUES (?,?)";
                    PreparedStatement prepStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    prepStatement.setString(1,account.getUsername());
                    prepStatement.setString(2,account.getPassword());
                    prepStatement.executeUpdate();
                    ResultSet keySet = prepStatement.getGeneratedKeys();
                    if(keySet.next()){
                        int account_id = (int) keySet.getLong(1);
                        return new Account(account_id, account.getUsername(), account.getPassword());
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            
        }
        
        return null;
    }
    public Account loginAccount(Account account){
        Connection conn = ConnectionUtil.getConnection();

        //Check if username and password are large enough to be valid
        //if(account.getUsername().length()>0 && account.getPassword().length() >=4 ){
        try {
            String sqlQuery = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement prepQuery = conn.prepareStatement(sqlQuery);
            prepQuery.setString(1,account.getUsername());
            prepQuery.setString(2, account.getPassword());
            ResultSet QueryResult = prepQuery.executeQuery();
            //Check that there is a match
            if(QueryResult.next()){
                //ResultSet keySet = prepQuery.getGeneratedKeys();
                //int account_id = (int) keySet.getLong(1);
                int account_id = QueryResult.getInt("account_id");
                return new Account(account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
            
        //}
        return null;

    }
    
}
