package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message postMessage(Message message){
        Connection conn = ConnectionUtil.getConnection();
        //Check if message text is the right size
        if(message.getMessage_text().length()>0 && message.getMessage_text().length()<=255){
            try {
                String sqlQuery = "SELECT * FROM account WHERE account_id = ?";
                PreparedStatement prepQuery = conn.prepareStatement(sqlQuery);
                prepQuery.setInt(1,message.getPosted_by());
                ResultSet QueryResult = prepQuery.executeQuery();

                //Check that the posting user id exists
                if(QueryResult.next()){
                    String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
                    PreparedStatement prepStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    prepStatement.setInt(1,message.getPosted_by());
                    prepStatement.setString(2,message.getMessage_text());
                    //long time = Instant.now().toEpochMilli();
                    //long time = System.currentTimeMillis();
                    //long time = 1669947792;
                    long time = message.getTime_posted_epoch();
                    prepStatement.setLong(3,time);
                    prepStatement.executeUpdate();
                    ResultSet keySet = prepStatement.getGeneratedKeys();
                    if(keySet.next()){
                        int message_id = (int) keySet.getLong(1);
                        return new Message(message_id, message.getPosted_by(), message.getMessage_text(), time);
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
    public List<Message> getAllMessages(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sqlQuery = "SELECT * FROM message";
            PreparedStatement prepQuery = conn.prepareStatement(sqlQuery);
            ResultSet result = prepQuery.executeQuery();
            //Get all the messages 
            while(result.next()){
                Message message = new Message(result.getInt("message_id"),result.getInt("posted_by"),
                result.getString("message_text"),result.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message getMessageById(int message_id){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sqlQuery = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement prepQuery = conn.prepareStatement(sqlQuery);
            prepQuery.setInt(1,message_id);
            ResultSet result = prepQuery.executeQuery();
            //Get the message
            if(result.next()){
                Message message = new Message(result.getInt("message_id"),result.getInt("posted_by"),
                result.getString("message_text"),result.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message deleteMessage(int message_id){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sqlQuery = "SELECT * FROM message WHERE message_id = ?";
            String sqlDelete = "DELETE FROM message WHERE message_id = ?";
            //String sqlQuery = "DELETE FROM message WHERE message_id = ? RETURNING *";
            PreparedStatement prepQuery = conn.prepareStatement(sqlQuery);
            prepQuery.setInt(1,message_id);
            PreparedStatement prepStatement = conn.prepareStatement(sqlDelete);
            prepStatement.setInt(1,message_id);
            ResultSet result = prepQuery.executeQuery();
            //prepStatement.execute();
            //boolean deleted = true;
            //boolean deleted = prepStatement.execute();
            //deleted = true;
            int deleted = prepStatement.executeUpdate();
            //if(deleted){
            if(deleted>0){
                //ResultSet result = prepStatement.getResultSet();
                //Get the message
                if(result.next()){
                    Message message = new Message(result.getInt("message_id"),result.getInt("posted_by"),
                    result.getString("message_text"),result.getLong("time_posted_epoch"));
                    return message;
                }
            }/* */
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message patchMessage(int message_id, String message_text){
        Connection conn = ConnectionUtil.getConnection();
        try {
            Message message = getMessageById(message_id);
            if(message != null && message_text.length()>0 && message_text.length()<=255){
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement prepStatement = conn.prepareStatement(sql);
                prepStatement.setString(1,message_text);
                prepStatement.setInt(2,message_id);
                int updated = prepStatement.executeUpdate();
                //if a message was updated, get the  updated message
                if(updated > 0){
                    return getMessageById(message_id);
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public List<Message> getAccountMessages(int account_id){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sqlQuery = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement prepQuery = conn.prepareStatement(sqlQuery);
            prepQuery.setInt(1,account_id);
            ResultSet result = prepQuery.executeQuery();
            //Get all the messages 
            while(result.next()){
                Message message = new Message(result.getInt("message_id"),result.getInt("posted_by"),
                result.getString("message_text"),result.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
