package twitch;

import twitch.exception.InvalidAccount;
import twitch.io.FileCreator;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Checker {
    private final StringBuilder sbResult = new StringBuilder();
    private final FileWriter fileWriter;

    /**
     * Constructor of Checker class
     * @throws IOException - when creation of FileWriter is failed
     */
    public Checker() throws IOException {
        fileWriter = new FileWriter(FileCreator.resultFile, false);
    }

    /**
     * The Method runs checker.
     * @throws IOException - when creation of FileWriter is failed
     */
    public void start() throws IOException {
        try (Scanner sc = new Scanner(FileCreator.file, "UTF-8")) {
            while (sc.hasNext()) {
                String token = sc.next();
                TwitchUser user = new TwitchUser(token, true);
                System.out.println(user.toString());
                output(token);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e);
        } catch (InvalidAccount e) {
            System.out.println("Invalid token: " + e);
        }
        fileWriter.flush();
    }


    /**
     * Method writes to file.
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
     * Method adds result to StringBuilder.
     * @param token
     */
    private void output(String token) {
        sbResult.append(token)
                .append("\n");
        writeToFile();
    }

}
