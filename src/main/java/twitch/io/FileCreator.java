package twitch.io;

import java.io.File;
import java.io.IOException;

public class FileCreator {
    public static File file;
    public static File resultFile;

    public static void create() {
        file = getDefaultFile();
        resultFile = getResultFile();
    }
    public static void create(String tokens) {
        file = getCustomFile(tokens);
        resultFile = getResultFile();
    }

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

    private static File getCustomFile(String tokens) {
        File file = new File(tokens);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("File can not be created");
        }
        checkFile(file);
        return file;
    }

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

    private static void checkFile(File file) {
        if (!file.exists() || !file.isFile() || !file.getAbsolutePath().endsWith(".txt")) {
            throw new IllegalArgumentException("Неверный файл.");
        }
    }
}
