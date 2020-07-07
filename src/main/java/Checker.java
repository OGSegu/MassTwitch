import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Scanner;

public class Checker {
    private final File file;
    private final File resultFile;
    private final StringBuilder sbResult = new StringBuilder();

    public Checker(String fileURL) {
        file = getCustomFile(fileURL);
        resultFile = getResultFile();
    }

    public Checker() {
        file = getDefaultFile();
        resultFile = getResultFile();
    }

    private File getCustomFile(String fileURL) {
        File file = new File(fileURL);
        checkFile(file);
        return file;
    }

    private File getDefaultFile() {
        File file = new File("tokens.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Файл не может быть создан");
        }
        checkFile(file);
        return file;
    }

    private File getResultFile() {
        File resultFile = new File("valid.txt");
        try {
            resultFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Файл не может быть создан");
        }
        checkFile(resultFile);
        return resultFile;
    }

    private void checkFile(File file) {
        if (!file.exists() || !file.isFile() || !file.getAbsolutePath().endsWith(".txt")) {
            throw new IllegalArgumentException("Неверный файл.");
        }
    }

    public void start() {
        try (Scanner sc = new Scanner(file, "UTF-8")) {
            while (sc.hasNext()) {
                String token = sc.next();
                boolean result = getTokenInformation(token);
                output(token, result);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e);
        }
        writeToFile();
    }

    private boolean getTokenInformation(String token) {
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
        JSONObject jsonObject = new JSONObject(response.body());
        String clientId = (String) jsonObject.get("client_id");
        String userId = (String) jsonObject.get("user_id");
//        sendFollow(token, clientId, userId);
        return response.body().contains("client_id");
    }

    private void writeToFile() {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(resultFile);
            fileWriter.write(sbResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void output(String token, boolean result) {
        System.out.println( token + " / " + result);
        sbResult.append(token);
    }

//    public void sendFollow(String token, String clientId, String userId) {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.twitch.tv/helix/users/follows"))
//                .setHeader("Authorization" ," Bearer " + token)
//                .setHeader("Client-ID", clientId)
//                .setHeader("Content-Type", "application/json")
//                .build();
//        HttpResponse<String> response = null;
//        try {
//            response = client.send(request,
//                    HttpResponse.BodyHandlers.ofString());
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(response.body());
//    }
}
