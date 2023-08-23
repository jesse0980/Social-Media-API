package Service;

import java.util.List;


import Model.Account;
import Model.Message;

public interface SocialMedia{
    Account registerAccount(String username, String password);
    Account loginAccount(String username, String password);
    Message createMessage(int postedBy, String messageText, long timePostedEpoch);
    List<Message> getAllMessages();
    Message getMessageById(int messageId);
    List<Message> getMessagesByUserId(int userId);
    boolean userExists(int userId);
    Message updateMessage(int messageId, String newMessageText);
    boolean deleteMessage(int messageId);
}