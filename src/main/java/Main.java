import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import Controller.SocialMediaController;
import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Service.SocialMediaService;
import Util.ConnectionUtil;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        AccountDAOImpl accountDAO = new AccountDAOImpl(ConnectionUtil.getConnection());
        MessageDAOImpl messageDAO = new MessageDAOImpl(ConnectionUtil.getConnection());

        // Initialize Services
        HttpClient webClient = HttpClient.newHttpClient();

        // Initialize Controllers
        SocialMediaController socialMediaController = new SocialMediaController();
        Javalin app = socialMediaController.startAPI();
        app.start(8080);
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/register"))
        .POST(HttpRequest.BodyPublishers.ofString("{" +
                "\"username\": \"user\", " +
                "\"password\": \"password\" }"))
        .header("Content-Type", "application/json")
        .build();
try {
    HttpResponse response = webClient.send(request, HttpResponse.BodyHandlers.ofString());

} catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (InterruptedException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}

    }
}
