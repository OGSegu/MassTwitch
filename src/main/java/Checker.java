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
    private final File file;
    private final File resultFile;
    private final StringBuilder sbResult = new StringBuilder();
    private final FileWriter fileWriter;


    public Checker() throws IOException {
        file = getDefaultFile();
        resultFile = getResultFile();
        fileWriter = new FileWriter(resultFile);
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

    public void start() throws IOException {
        try (Scanner sc = new Scanner(file, "UTF-8")) {
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
        JSONObject jsonObject = new JSONObject(response.body());
        String clientId = (String) jsonObject.get("client_id");
        String userId = (String) jsonObject.get("user_id");
        boolean result = response.body().contains("client_id");
        output(token, clientId, userId, result);
    }

    private void writeToFile() {
        try {
            fileWriter.write(sbResult.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void output(String token, String clientId, String userId, boolean result) {
        System.out.println( token + " / " + result);
        sbResult.append(token)
                .append(":")
                .append(clientId)
                .append(":")
                .append(userId)
                .append("\n");
        writeToFile();
    }

}
