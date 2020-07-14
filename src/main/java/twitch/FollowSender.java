package twitch;

import twitch.io.FileCreator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class FollowSender {
    private final long channelID;

    public FollowSender(String channelName) {
        this.channelID = getChannelID(channelName);
    }

    public void start() {
        try (Scanner sc = new Scanner(FileCreator.resultFile, "UTF-8")) {
            while (sc.hasNext()) {
                String[] info = sc.next().split(":");
                follow(info[0], info[1], info[2]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e);
        }
    }


    private boolean follow(String token, String clientId, String userId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("https://api.twitch.tv/kraken/users/" + userId + "/follows/channels/" + channelID))
                .setHeader("Authorization", " OAuth " + token)
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
        if (response.body().contains("Unprocessable Entity")) {
            System.out.println(response.body());
            return false;
        }
        System.out.println(userId + ": followed");
        return true;
    }

    private long getChannelID(String channelName) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitch.tv/kraken/users?login=" + channelName))
                .setHeader("Client-ID", "kimne78kx3ncx6brgo4mv6wki5h1ko")
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
        JSONArray userInfo = jsonObject.getJSONArray("users");
        long channelId = 0;
        for (int i = 0; i < userInfo.length(); i++) {
            JSONObject info = userInfo.getJSONObject(i);
            channelId = info.getLong("_id");
        }
        return channelId;
    }
}
