package twitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Checker extends Checkable {

    /**
     * action
     * 0 - checker
     * 1 - cleaner
     */

    private final FileWriter fileWriter;
    private final Queue<String> tokensList = new ArrayBlockingQueue<>(70000);

    /**
     * Constructor of Checker class
     *
     * @throws IOException - when creation of FileWriter is failed
     */
    public Checker(File in) throws IOException {
        super(in, "valid.txt");
        fileWriter = new FileWriter(super.fileOut, false);
    }

    /**
     * The method runs checker
     *
     * @throws IOException - when creation of FileWriter is failed
     */
    public void start() throws IOException {
        try (Scanner sc = new Scanner(super.fileIn, "UTF-8")) {
            while (sc.hasNext()) {
                tokensList.add(sc.next());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e);
        }
        fileWriter.flush();
        startExecution();
    }

    /**
     * The method starts execution of checker
     */
    private void startExecution() {
        AtomicInteger i = new AtomicInteger(0);
        int amount = tokensList.size();
        AtomicInteger validAmount = new AtomicInteger();
        while (!super.executor.isTerminated()) {
            super.executor.execute(() -> {
                if (i.get() >= amount - 1 || tokensList.peek() == null) {
                    super.executor.shutdown();
                    try {
                        if (!super.executor.awaitTermination(5, TimeUnit.SECONDS)) {
                            super.executor.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        super.executor.shutdownNow();
                    }
                } else {
                    String token = tokensList.poll();
                    TwitchUser user = new TwitchUser(token);
                    if (!user.isValid()) {
                        System.out.println(String.format("%s - !INVALID!", user.getToken()));
                        return;
                    }
                    if (user.getFollowed() > 1990) {
                        user.clean(30);
                        System.out.println(String.format("%s WAS CLEANED", user.getLogin()));
                    }
                    writeToFile(user.getToken());
                    validAmount.getAndIncrement();
                }
            });
        }
        outputResult(validAmount.get(), amount);
    }

    /**
     * Method writes to file
     */
    private void writeToFile(String output) {
        try {
            fileWriter.write(output + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method outputs result in format - valid / amount
     *
     * @param valid  - amount of valid tokens
     * @param amount - amount of tokens
     */
    private void outputResult(int valid, int amount) {
        System.out.println(String.format("RESULT - %d/%d", valid, amount));
    }
}
