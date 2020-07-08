import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FollowSender {
    private final long channelID;

    public FollowSender(String channelName) {
        this.channelID = getChannelID(channelName);
    }

    public void start(String token, String clientId, String userId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitch.tv/kraken/users/" + userId + "/follows/channels/" + channelID))
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
        System.out.println(response.body());
    }

    private long getChannelID(String channelName) {
        return 123;
    }
}
