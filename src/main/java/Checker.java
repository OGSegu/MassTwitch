import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Checker {
    private final StringBuilder sbResult = new StringBuilder();
    private final FileWriter fileWriter;


    public Checker() throws IOException {
        fileWriter = new FileWriter(FileCreator.resultFile, false);
    }


    public void start() throws IOException {
        try (Scanner sc = new Scanner(FileCreator.file, "UTF-8")) {
            while (sc.hasNext()) {
                String token = sc.next();
                getTokenInformation(token);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e);
        }
        fileWriter.flush();
    }


    private void getTokenInformation(String token) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://id.twitch.tv/oauth2/validate"))
                .setHeader("Authorization" ," Bearer " + token)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (!response.body().contains("client_id")) {
            System.out.println(token + " : " + "Invalid");
            return;
        }
        JSONObject jsonObject = new JSONObject(response.body());
        String clientId = (String) jsonObject.get("client_id");
        String userId = (String) jsonObject.get("user_id");
        System.out.println(token + " : " + "Valid");
        output(token, clientId, userId);
        checkFollowers(token, clientId);
    }

    private int checkFollowers(String token, String clientId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitch.tv/kraken/channel"))
                .setHeader("Authorization" ," OAuth " + token)
                .setHeader("Client-ID", clientId)
                .setHeader("Accept", "application/vnd.twitchtv.v5+json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(response.body());
        System.out.println(jsonObject.toString());
        //System.out.println(jsonObject.getInt("followers"));
        return 12;
    }

    private void writeToFile() {
        try {
            fileWriter.write(sbResult.toString());
            fileWriter.flush();
            sbResult.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void output(String token, String clientId, String userId) {
        sbResult.append(token)
                .append(":")
                .append(clientId)
                .append(":")
                .append(userId)
                .append("\n");
        writeToFile();
    }

}
