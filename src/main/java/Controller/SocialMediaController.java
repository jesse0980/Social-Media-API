package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import Util.ConnectionUtil;

public class SocialMediaController {
    private final SocialMediaService socialMediaService;

    public SocialMediaController() {
        this.socialMediaService = new SocialMediaService(
            new AccountDAOImpl(ConnectionUtil.getConnection()),
            new MessageDAOImpl(ConnectionUtil.getConnection())
        );
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Register account endpoint
        app.post("/register", this::registerAccountHandler);

        // Login account endpoint
        app.post("/login", this::loginAccountHandler);

        // Create message endpoint
        app.post("/messages", this::createMessageHandler);

        // Get all messages endpoint
        app.get("/messages", this::getAllMessagesHandler);

        // Get message by ID endpoint
        app.get("/messages/{message_id}", this::getMessageByIdHandler);

        // Get messages by user ID endpoint
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserIdHandler);

        // Update message endpoint
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        // Delete message endpoint
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        return app;
    }

    private void registerAccountHandler(Context context) {
        String requestBody = context.body();
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            Account account = objectMapper.readValue(requestBody, Account.class);
            // Check if the username is not blank and the password length is at least 4 characters
            // Check if an acco1unt with the given username already exists
    
            Account registeredAccount = socialMediaService.registerAccount(account.getUsername(), account.getPassword());
            if (registeredAccount != null) {
                context.status(200);
                context.json(registeredAccount);
            } else {
                context.status(400).result("");
            }
        } catch (IOException e) {
            context.status(400).result("");
        }
    }

    private void loginAccountHandler(Context context) {
        String requestBody = context.body();
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            Account account = objectMapper.readValue(requestBody, Account.class);
            // Check if the username is not blank and the password length is at least 4 characters
            // Check if an account with the given username already exists
    
            Account registeredAccount = socialMediaService.loginAccount(account.getUsername(), account.getPassword());
            if (registeredAccount != null) {
                context.status(200);
                context.json(registeredAccount);
            } else {
                context.status(401).result("");
            }
        } catch (IOException e) {
            context.status(401).result("");
        }
    }

    private void createMessageHandler(Context context) {
        try {
            CreateMessageRequest messageRequest = context.bodyAsClass(CreateMessageRequest.class);
            int postedBy = messageRequest.posted_by;
            String messageText = messageRequest.message_text;
            long timePostedEpoch = messageRequest.time_posted_epoch;
            
            Message message = socialMediaService.createMessage(postedBy, messageText, timePostedEpoch);
            if (message != null) {
                System.out.println(messageText.length());
                context.status(200);
                context.json(message);
            } else {
                context.status(400).result("");
            }
        } catch (Exception e) {
            context.status(400).result("");
        }
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = socialMediaService.getAllMessages();
        context.json(messages);
    }

    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = socialMediaService.getMessageById(messageId);
        if (message != null) {
            context.json(message);
        } else {
            context.status(200);
        }
    }

    private void getMessagesByUserIdHandler(Context context) {
        int userId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = socialMediaService.getMessagesByUserId(userId);
        context.json(messages);
    }

    private void updateMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = context.body();
        String messageText = "";
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(requestBody);
            
        // Retrieve the message_text field value
         messageText = jsonNode.get("message_text").asText();
    
        // Now you have the message text
        System.out.println("Message Text: " + messageText);


        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(messageText);
        Message updatedMessage = socialMediaService.updateMessage(messageId, messageText);
        if (updatedMessage != null) {
            context.status(200);
            context.json(updatedMessage);
        } else {
            context.status(400).result("");
        }
    }

    private void deleteMessageHandler(Context context) {
        
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message m = socialMediaService.getMessageById(messageId);
        if (m != null) {
            context.status(200);
            context.json(m);
        } else {
            context.status(200);
        }
    }
}
