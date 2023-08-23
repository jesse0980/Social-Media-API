package DAO;

import java.util.List;

import Model.Message;

public interface MessageDAO {
    Message createMessage(Message message);
    List<Message> getAllMessages();
    Message getMessageById(int messageId);
    List<Message> getMessagesByUserId(int userId);
    Message updateMessage(int messageId, String newMessageText);
    boolean deleteMessage(int messageId);
}