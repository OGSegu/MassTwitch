package twitch;

import twitch.io.FileCreator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Scanner;

public class FollowSender {
    private final String channelName;
    private final int amount;

    /**
     * Constructor of FollowSender
     * @param channelName - name of the channel which will be followed.
     * @param amount - amount of follows
     */
    public FollowSender(String channelName, int amount) {
        this.channelName = channelName;
        this.amount = amount;
    }

    /**
     * The method starts sending follows.
     */
    public void start() {
        try (Scanner sc = new Scanner(FileCreator.file, "UTF-8")) {
            int i = 0;
            while (i < amount) {
                TwitchUser user = new TwitchUser(sc.next());
                if (!user.isValid()) {
                    continue;
                }
                if (!user.follow(channelName)) {
                    continue;
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File can not be found: " + e);
        }
    }



}
