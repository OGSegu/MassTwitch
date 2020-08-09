package twitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Checker extends Checkable {

    private final StringBuilder sbResult = new StringBuilder();
    private final FileWriter fileWriter;

    /**
     * Constructor of Checker class
     *
     * @throws IOException - when creation of FileWriter is failed
     */
    public Checker(File in) throws IOException {
        super(in, "valid.txt");
        fileWriter = new FileWriter(super.getFileOut(), false);
    }

    /**
     * The method runs checker.
     *
     * @throws IOException - when creation of FileWriter is failed
     */
    public void start() throws IOException {
        try (Scanner sc = new Scanner(super.getFileIn(), "UTF-8")) {
            while (sc.hasNext()) {
                String token = sc.next();
                TwitchUser user = new TwitchUser(token);
                if (user.isValid()) {
                    output(token);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e);
        }
        fileWriter.flush();
    }

    /**
     * Method output result.
     *
     * @param token - token
     */
    private void output(String token) {
        System.out.println(token + " - VALID");
        sbResult.append(token)
                .append("\n");
        writeToFile();
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

}
