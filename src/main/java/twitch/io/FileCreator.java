package twitch.io;

import java.io.File;
import java.io.IOException;

public class FileCreator {
    public static File file;
    public static File resultFile;

    /**
     * Creates default files
     */
    public static void create() {
        file = getDefaultFile();
        resultFile = getResultFile();
    }

    /**
     * Creates custom files
     * @param tokensFile - name of token file
     */
    public static void create(String tokensFile) {
        file = getCustomFile(tokensFile);
        resultFile = getResultFile();
    }

    /**
     * Gets/creates default token file.
     * @return - default file
     */
    private static File getDefaultFile() {
        File file = new File("tokens.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("File can not be created");
        }
        checkFile(file);
        return file;
    }
    /**
     * Gets custom token file.
     * @param tokensFile - File with tokens
     * @return - default file
     */
    private static File getCustomFile(String tokensFile) {
        File file = new File(tokensFile);
        checkFile(file);
        return file;
    }

    /**
     * Gets/creates result file (valid.txt)
     * @return - result file
     */
    private static File getResultFile() {
        File resultFile = new File("valid.txt");
        try {
            resultFile.createNewFile();
        } catch (IOException e) {
            System.out.println("File can not be found");
        }
        checkFile(resultFile);
        return resultFile;
    }

    /**
     * Check if file exists, .txt and not directory
     * @param file - this file
     */
    private static void checkFile(File file) {
        if (!file.exists() || !file.isFile() || !file.getAbsolutePath().endsWith(".txt")) {
            throw new IllegalArgumentException("Неверный файл.");
        }
    }
}
