package twitch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class Utils {
    public static final String TEST_TOKEN = "w4hit48w391fs1bcoxq7o6svldy11s";
    public static final String TEST_CLIENTID = "gp762nuuoqcoxypju8c569th9wz7q5";


    /**
     * Gets channelID by it's name
     *
     * @param channelName - channel name
     * @return - channelID
     */
    public static String getChannelID(String channelName) {
        HttpClient client = HttpClient.newHttpClient();
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
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response).body());
        JSONArray userInfo = jsonObject.getJSONArray("users");
        String channelId = null;
        for (int i = 0; i < userInfo.length(); i++) {
            JSONObject info = userInfo.getJSONObject(i);
            channelId = info.getString("_id");
        }
        if (channelId == null) {
            return "Not Found";
        }
        return channelId;
    }

    public static boolean channelExists(String channel) {
        return !getChannelID(channel).equals("Not Found");
    }
}
