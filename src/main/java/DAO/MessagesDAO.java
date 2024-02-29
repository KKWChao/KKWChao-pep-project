package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;


public class MessagesDAO {

    /** 
     * GET ALL MESSAGES
     * @return All flights
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        
        try  {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                    );

                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return messages;
        
    }

    /** 
     * GET MESSAGES BY ID
     * @param message_id = message id
     * @return message of specified message_id
     */
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try  {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                
                return message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return null;
    }
 
    /** 
     * GET MESSAGES BY USER ID
     * @param account_d = user id
     * @return all messages with user id = param
     */
    public List<Message> getAllMessageByUserId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        
        try  {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 

        return messages;
    }

    /**
     * CREATE MESSAGES
     * @param message
     * @return mesage created
     */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        // block creation of message if blank
        if (message.getMessage_text().length() <= 0) {
            return null;
        }

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement preparedStatment = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatment.setInt(1, message.getPosted_by());
            preparedStatment.setString(2, message.getMessage_text());
            preparedStatment.setLong(3, message.getTime_posted_epoch());

            preparedStatment.executeUpdate();
            ResultSet preparedKeyResultSet = preparedStatment.getGeneratedKeys();

            if (preparedKeyResultSet.next()) {
                int generated_account_id = (int) preparedKeyResultSet.getInt(1);
                return new Message(
                    generated_account_id, 
                    message.getPosted_by(), 
                    message.getMessage_text(), 
                    message.getTime_posted_epoch()
                    );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
         
        return null;
    }

    /**
     * UPDATE MESSAGE
     * @param message_id
     * @param message
     * @return updated message
     */
    public Message updateMessage(int message_id, Message message) {
        Connection connection = ConnectionUtil.getConnection();

        // updating message logic check
        if (message.getMessage_text().length() > 255 ||  message.getMessage_text().length() <= 0) {
            return null;
        }

        try  {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message_id);

            int updateSuccess = preparedStatement.executeUpdate();

            if (updateSuccess > 0) {
                return getMessageById(message_id);
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    /**
     * DELETE MESSAGE
     * @param message_id
     */
    public void deleteMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try  {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
}
