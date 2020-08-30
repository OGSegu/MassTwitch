package twitch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

public class Utils {

    public static final HttpClient client = HttpClient.newHttpClient();
    public static final String TEST_TOKEN = "w4hit48w391fs1bcoxq7o6svldy11s";
    public static final String TEST_CLIENTID = "gp762nuuoqcoxypju8c569th9wz7q5";


    /**
     * Gets channelID by it's name
     *
     * @param channelName - channel name
     * @return - channelID
     */
    public static String getChannelID(String channelName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitch.tv/kraken/users?login=" + channelName))
                .setHeader("Client-ID", TEST_CLIENTID)
                .setHeader("Accept", "application/vnd.twitchtv.v5+json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (response == null) return "";
        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray userInfo = jsonObject.getJSONArray("users");
        if (userInfo.isEmpty()) return "Unknown";
        JSONObject info = userInfo.getJSONObject(0);
        return Optional
                .ofNullable(info.getString("_id"))
                .orElse("Unknown");
    }

    /**
     *
     * @param channel - channel name
     * @return - true - channel exists, false - not exists
     */
    public static boolean channelExists(String channel) {
        return !getChannelID(channel).equals("Unknown");
    }
}
