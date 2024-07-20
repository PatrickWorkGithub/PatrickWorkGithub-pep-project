package Service;

import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public Message postMessage(Message message){
        return messageDAO.postMessage(message);
    }
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);
    }
    public Message deleteMessage(int message_id){
        return messageDAO.deleteMessage(message_id);
    }
    public Message patchMessage(int message_id, String message_text){
        return messageDAO.patchMessage(message_id,message_text);
    }
    public List<Message> getAccountMessages(int account_id){
        return messageDAO.getAccountMessages(account_id);
    }
}
