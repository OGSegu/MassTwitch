package twitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FollowSender extends Checkable {

    private final String channelName;
    private final int amount;
    private final int threads;
    private final List<String> followers = new ArrayList<>();

    /**
     * Constructor of FollowSender
     *
     * @param channelName - name of the channel which will be followed.
     * @param amount      - amount of follows
     */
    public FollowSender(File in, String channelName, int amount, int threads) throws IOException {
        super(in);
        this.channelName = channelName;
        this.amount = amount;
        this.threads = threads;
    }

    /**
     * The method starts sending follows.
     */
    public void start() {
        try (Scanner sc = new Scanner(super.fileIn, "UTF-8")) {
            while (sc.hasNext()) {
                followers.add(sc.next());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File can not be found: " + e);
        }
        startExecution();
    }

    /**
     * Starts execution of Threads.
     */
    private void startExecution() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threads, threads, 3L, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        AtomicInteger subscribed = new AtomicInteger(0);
        AtomicInteger i = new AtomicInteger(-1);
        while (i.get() < followers.size()) {
            if (subscribed.get() >= amount) {
                executor.shutdown();
                executor.shutdownNow();
                break;
            }
            executor.execute(() -> {
                TwitchUser user = new TwitchUser(followers.get(i.incrementAndGet()));
                if (!user.isValid()) {
                    return;
                }
                if (!user.follow(channelName)) {
                    return;
                }
                System.out.println(subscribed.incrementAndGet() + " / " + amount);
            });
        }
        System.out.println("DONE");
    }
}
