package twitch;

import twitch.io.FileCreator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FollowSender {
    private final String channelName;
    private final int amount;
    private final List<String> followers = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructor of FollowSender
     *
     * @param channelName - name of the channel which will be followed.
     * @param amount      - amount of follows
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
            while (sc.hasNext()) {
                followers.add(sc.next());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File can not be found: " + e);
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 200, 4L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
        AtomicInteger subscribed = new AtomicInteger(0);
        AtomicInteger i = new AtomicInteger(-1);
        long startTime = 0;
        boolean getTime = false;
        while (i.get() < followers.size()) {
            if (!getTime && subscribed.get() > 1) {
                startTime = System.currentTimeMillis();
                getTime = true;
            }
            executor.execute(() -> {
                if (subscribed.get() >= amount) {
                    executor.shutdown();
                    try {
                        if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                            executor.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        executor.shutdownNow();
                    }
                    return;
                }
                executor.remove(this::start);
                i.getAndIncrement();
                TwitchUser user = new TwitchUser(followers.get(i.get()));
                if (!user.isValid()) {
                    return;
                }
                if (!user.follow(channelName)) {
                    return;
                }
                subscribed.getAndIncrement();
            });

            if (subscribed.get() >= amount) {
                break;
            }
        }
        System.out.println("DONE");
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime-startTime) + "ms");
    }
}
