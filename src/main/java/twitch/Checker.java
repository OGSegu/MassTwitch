package twitch;

import twitch.exception.InvalidAccount;
import twitch.io.FileCreator;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Checker {
    private final StringBuilder sbResult = new StringBuilder();
    private final FileWriter fileWriter;

    /**
     * Конструктор класса Twitch_Checker.Checker
     * @throws IOException
     */
    public Checker() throws IOException {
        fileWriter = new FileWriter(FileCreator.resultFile, false);
    }

    /**
     * Метод запускающий чекер.
     * @throws IOException
     */
    public void start() throws IOException {
        try (Scanner sc = new Scanner(FileCreator.file, "UTF-8")) {
            while (sc.hasNext()) {
                String token = sc.next();
                TwitchUser user = new TwitchUser(token, true);
                System.out.println(user.toString());
                output(token);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e);
        } catch (InvalidAccount ignored) {
        }
        fileWriter.flush();
    }



//    private int checkFollowers(String token, String clientId) {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.twitch.tv/kraken/channel"))
//                .setHeader("Authorization" ," OAuth " + token)
//                .setHeader("Client-ID", clientId)
//                .setHeader("Accept", "application/vnd.twitchtv.v5+json")
//                .build();
//        HttpResponse<String> response = null;
//        try {
//            response = client.send(request,
//                    HttpResponse.BodyHandlers.ofString());
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        JSONObject jsonObject = new JSONObject(response.body());
//        System.out.println(jsonObject.toString());
//        //System.out.println(jsonObject.getInt("followers"));
//        return 12;
//    }

    /**
     * Метод записывает результат в файл
     */
    private void writeToFile() {
        try {
            fileWriter.write(sbResult.toString());
            fileWriter.flush();
            sbResult.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод преобразовывающий информацию от Токена в вид token:clientId:userId
     * @param token
     */
    private void output(String token) {
        sbResult.append(token)
                .append("\n");
        writeToFile();
    }

}
