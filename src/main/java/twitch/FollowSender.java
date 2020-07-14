package twitch;

import twitch.exception.InvalidAccount;
import twitch.io.FileCreator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Scanner;

public class FollowSender {
    private final long channelID;

    public FollowSender(String channelName) {
        this.channelID = getChannelID(channelName);
    }

    public void start() {
        try (Scanner sc = new Scanner(FileCreator.file, "UTF-8")) {
            while (sc.hasNext()) {
                TwitchUser user = new TwitchUser(sc.next(), true);
                if (user.isValid()) {
                    follow(user);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File can not be found: " + e);
        } catch (InvalidAccount e) {
            System.out.println("Invalid token : " + e);
        }
    }


    private boolean follow(TwitchUser user) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("https://api.twitch.tv/kraken/users/" + user.getUserID() + "/follows/channels/" + channelID))
                .setHeader("Authorization", " OAuth " + user.getToken())
                .setHeader("Client-ID", user.getClientID())
                .setHeader("Accept", "application/vnd.twitchtv.v5+json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (!Objects.requireNonNull(response).body().contains("channel")) {
            System.out.println(user.getLogin() + " failed to follow. Followed: " + user.getFollowed());
            return false;
        }
        System.out.println(user.getLogin() + ": followed");
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
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response).body());
        JSONArray userInfo = jsonObject.getJSONArray("users");
        long channelId = 0;
        for (int i = 0; i < userInfo.length(); i++) {
            JSONObject info = userInfo.getJSONObject(i);
            channelId = info.getLong("_id");
        }
        return channelId;
    }
}
