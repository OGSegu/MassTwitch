import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Checker {
    private final File file;

    public Checker(String fileURL) {
        file = getCustomFile(fileURL);
    }

    public Checker() {
        file = getDefaultFile();
    }

    private File getCustomFile(String fileURL) {
        File file = new File(fileURL);
        checkFile(file);
        return file;
    }

    private File getDefaultFile() {
        File file = new File("tokens.txt");
        checkFile(file);
        return file;
    }

    private void checkFile(File file) {
        if (!file.exists() || !file.isDirectory() || !file.getAbsolutePath().endsWith(".txt")) {
            throw new IllegalArgumentException("Неверный файл.");
        }
    }

    public void check() {
        try (Scanner sc = new Scanner(file, "UTF-8")) {

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e);
        }
    }
}
