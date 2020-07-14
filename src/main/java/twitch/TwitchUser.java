package twitch;

import org.json.JSONArray;
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
    private final boolean valid;

    private String clientID;
    private String userID;

    private int followed;


    /**
     * Constructor of Twitch.TwitchUser class
     *
     * @param token
     * @param ignoreInvalid true - ignore invalid accounts
     */
    public TwitchUser(String token, boolean ignoreInvalid) throws InvalidAccount {
        this.token = token;
        this.valid = getTokenInformation(token);
        if (!ignoreInvalid && !this.valid) {
            throw new InvalidAccount("Invalid token : ", token);
        }
        this.followed = getFollowedAmount();
        System.out.println(toString());
        clean(5);
    }

    public TwitchUser(String token) throws InvalidAccount {
        this.token = token;
        this.valid = getTokenInformation(token);
        if (!this.valid) {
            throw new InvalidAccount("Invalid token : ", token);
        }
        this.followed = getFollowedAmount();
        System.out.println(toString());
        clean(5);
    }

    /**
     * The Method that receives user's clientID and userID
     *
     * @param token
     * @return true - successful, false - failed
     */
    private boolean getTokenInformation(String token) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://id.twitch.tv/oauth2/validate"))
                .setHeader("Authorization", " Bearer " + token)
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

    /**
     * Method gets amount of followings.
     *
     * @return - amount of followings
     */
    public int getFollowedAmount() {
        if (!valid) return -1;
        JSONObject jsonObject = getFollowedJSON();
        int result;
        try {
            result = jsonObject.getInt("total");
        } catch (JSONException e) {
            System.out.println("Could not find key\"total\"");
            return -1;
        }
        return result;
    }

    private JSONObject getFollowedJSON() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitch.tv/helix/users/follows?from_id=" + userID))
                .setHeader("Authorization", " Bearer " + token)
                .setHeader("Client-ID", clientID)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new JSONObject(Objects.requireNonNull(response).body());
    }

    public boolean canFollow() {
        return followed < 2000;
    }

    public void cleanAll() {
        if (!valid) return;
        try {
            JSONObject jsonObject = getFollowedJSON();
            int total = jsonObject.getInt("total");
            if (total == 0) return;
            JSONArray data = jsonObject.getJSONArray("data");
            int i = 0;
            for (int k = 0; k < total; k++) {
                if (i == data.length() - 1) {
                    data = getFollowedJSON().getJSONArray("data");
                    i = 0;
                }
                String userID = data.getJSONObject(i).getString("to_id");
                if (unfollow(userID)) {
                    System.out.println(k + "/" + total);
                } else {
                    System.out.println("failed to unfollowed");
                    break;
                }
                i++;
            }
        } catch (JSONException e) {
            System.out.println("Can't parse JSON");
        }
    }

    private void clean(int amount) {
        if (!valid) return;
        try {
            JSONObject jsonObject = getFollowedJSON();
            int total = jsonObject.getInt("total");
            if (total == 0) return;
            JSONArray data = jsonObject.getJSONArray("data");
            int i = 0;
            for (int k = 0; k < amount; k++) {
                if (i == data.length() - 1) {
                    data = getFollowedJSON().getJSONArray("data");
                    i = 0;
                }
                String userID = data.getJSONObject(i).getString("to_id");
                if (unfollow(userID)) {
                    System.out.println(k + "/" + total);
                } else {
                    System.out.println("Failed to unfollowed");
                    break;
                }
                i++;
            }
        } catch (JSONException e) {
            System.out.println("Can't parse JSON");
        }
    }

    private boolean unfollow(String channelID) {
        if (!valid) return false;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("https://api.twitch.tv/kraken/users/" + userID + "/follows/channels/" + channelID))
                .setHeader("Authorization", " OAuth " + token)
                .setHeader("Client-ID", clientID)
                .setHeader("Accept", "application/vnd.twitchtv.v5+json")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(response).body().isEmpty();
    }

    public void setFollowed(int followed) {
        this.followed = followed;
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

    public int getFollowed() {
        return followed;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        return "TwitchUser{" +
                "valid='" + valid + '\'' +
                ", token=" + token +
                ", clientID='" + clientID + '\'' +
                ", userID='" + userID + '\'' +
                ", followed=" + followed +
                '}';
    }
}
