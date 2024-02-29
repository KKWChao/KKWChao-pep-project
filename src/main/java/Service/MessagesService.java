package Service;

import java.util.List;

import DAO.MessagesDAO;
import Model.Message;

public class MessagesService {
    private MessagesDAO messageDAO;

    public MessagesService() {
        this.messageDAO = new MessagesDAO();
    }

    public MessagesService(MessagesDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * GET ALL MESSAGES
     * @return List of all messages
     */
    public List<Message> getAllMessages() {
        try {
            return messageDAO.getAllMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GET MESSAGES BY ID
     * @param message_id
     * @return message 
     */
    public Message getMessageById(int message_id) {
        try {
            return messageDAO.getMessageById(message_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * GET MESSAGE BY ACCOUNT ID
     * @param account_id
     * @return List of all message from specified user
     */
    public List<Message> getMessagesByUserId(int account_id) {
        try {
            return messageDAO.getAllMessageByUserId(account_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * CREATE MESSAGE
     * @param messsage
     * @return created message
     */
    public Message createMessage(Message message) {
        try {
            return messageDAO.createMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            return messageDAO.updateMessage(message_id, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DELETE MESSAGE
     * @param message_id
     * @return deleted message
     */
    public Message deleteMessage(int message_id) {
        try {
            Message deletedMessage = messageDAO.getMessageById(message_id);
            messageDAO.deleteMessage(message_id);
            return deletedMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
