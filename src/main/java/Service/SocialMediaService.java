package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;

import Model.Account;
import Model.Message;


public class SocialMediaService implements SocialMedia {
    private final AccountDAO accountDAO;
    private final MessageDAO messageDAO;

    public SocialMediaService(AccountDAO accountDAO, MessageDAO messageDAO) {
        this.accountDAO = accountDAO;
        this.messageDAO = messageDAO;
    }


    @Override
    public Account registerAccount(String username, String password) {
        if (username.isBlank() || password.length() < 4 || accountDAO.userExistsByUsername(username)) {
            return null; // Registration unsuccessful
        }
        Account account = new Account(username, password);
        return accountDAO.registerAccount(account);
    }

    @Override
    public Account loginAccount(String username, String password) {
        return accountDAO.loginAccount(username, password);
    }
    @Override
    public boolean userExists(int userId) {
        try {
            // Assuming you have a DAO method that checks if a user exists by ID
            return accountDAO.userExistsById(userId);
        } catch (Exception e) {
            // Handle any exceptions or errors here
            return false; // Return false in case of an error
        }
    }

    @Override
    public Message createMessage(int postedBy, String messageText, long timePostedEpoch) {
        if (messageText == "" || messageText.length() >= 255 || !this.userExists(postedBy)) {
            return null; // Message creation unsuccessful
        }

        Message message = new Message(postedBy, messageText, timePostedEpoch);
        return messageDAO.createMessage(message);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    @Override
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }

    @Override
    public Message updateMessage(int messageId, String newMessageText) {
        if (newMessageText == "" || newMessageText.length() >= 255) {
            return null; // Message update unsuccessful
        }

        return messageDAO.updateMessage(messageId, newMessageText);
    }

    @Override
    public boolean deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }
}