package twitch;

import org.json.JSONException;
import org.json.JSONObject;
import twitch.exception.InvalidAccount;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class TwitchUser {
    private final String token;
    private String clientID;
    private String userID;
    private final boolean valid;

    /**
     * Constructor of Twitch.TwitchUser class
     * @param token
     * @param ignoreInvalid true - ignore invalid accounts
     */
    public TwitchUser(String token, boolean ignoreInvalid) throws InvalidAccount {
        this.token = token;
        this.valid = getTokenInformation(token);
        if (!ignoreInvalid && !this.valid) {
            throw new InvalidAccount("Invalid token : ", token);
        }
    }
    public TwitchUser(String token) throws InvalidAccount {
        this.token = token;
        this.valid = getTokenInformation(token);
        if (!this.valid) {
            throw new InvalidAccount("Invalid token : ", token);
        }

    }

    /**
     * The Method that receives user's clientID and userID
     * @param token
     * @return true - successful, false - failed
     */
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
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response).body());
        try {
            this.clientID = (String) jsonObject.get("client_id");
            this.userID = (String) jsonObject.get("user_id");
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public String getToken() {
        return token;
    }

    public String getClientID() {
        return clientID;
    }

    public String getUserID() {
        return userID;
    }


    @Override
    public String toString() {
        return "TwitchUser{" +
                "token='" + token + '\'' +
                ", clientID='" + clientID + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
