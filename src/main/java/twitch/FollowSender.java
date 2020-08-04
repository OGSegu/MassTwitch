package twitch;

import twitch.io.FileCreator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FollowSender {
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
    public FollowSender(String channelName, int amount, int threads) {
        this.channelName = channelName;
        this.amount = amount;
        this.threads = threads;
    }

    /**
     * The method starts sending follows.
     */
    public void start() {
        try (Scanner sc = new Scanner(FileCreator.file, "UTF-8")) {
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
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 100, 4L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
        AtomicInteger subscribed = new AtomicInteger(0);
        AtomicInteger i = new AtomicInteger(-1);
        while (i.get() < followers.size()) {
            if (subscribed.get() >= amount) {
                executor.shutdown();
                executor.shutdownNow();
                break;
            }
            executor.execute(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.incrementAndGet();
                TwitchUser user = new TwitchUser(followers.get(i.get()));
                if (!user.isValid()) {
                    return;
                }
                if (!user.follow(channelName)) {
                    return;
                }
                subscribed.incrementAndGet();
                System.out.println(subscribed.get());
            });
        }
        System.out.println("DONE");
    }
}
