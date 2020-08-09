package twitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Checker extends Checkable {

    private final StringBuilder sbResult = new StringBuilder();
    private final FileWriter fileWriter;
    private final List<String> tokensList = new ArrayList<>();

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
        while (!super.executor.isShutdown()) {
            super.executor.execute(() -> {
                if (i.get() >= amount - 1) {
                    super.executor.shutdown();
                    try {
                        super.executor.awaitTermination(3, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    TwitchUser user = new TwitchUser(tokensList.get(i.incrementAndGet()));
                    if (!user.isValid()) {
                        System.out.println(String.format("%s - !INVALID!", user.getToken()));
                        return;
                    }
                    System.out.println(String.format("%s - VALID", user.getToken()));
                    validAmount.getAndIncrement();
                    outputToken(user.getToken());
                }
            });
        }
        outputResult(validAmount.get(), amount);
    }

    /**
     * Method output result
     *
     * @param token - token
     */
    private void outputToken(String token) {
        sbResult.append(token)
                .append("\n");
        writeToFile();
    }

    /**
     * Method writes to file
     */
    private void writeToFile() {
        try {
            fileWriter.write(sbResult.toString());
            fileWriter.flush();
            sbResult.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method outputs result in format - valid / amount
     * @param valid - amount of valid tokens
     * @param amount - amount of tokens
     */
    private void outputResult(int valid, int amount) {
        System.out.println(String.format("RESULT - %d/%d", valid, amount));
    }
}
