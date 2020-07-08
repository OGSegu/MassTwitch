import java.io.File;
import java.io.IOException;

public class FileCreator {
    public static File file;
    public static File resultFile;

    public static void create() {
        file = getDefaultFile();
        resultFile = getResultFile();
    }

    private static File getDefaultFile() {
        File file = new File("tokens.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Файл не может быть создан");
        }
        checkFile(file);
        return file;
    }

    private static File getResultFile() {
        File resultFile = new File("valid.txt");
        try {
            resultFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Файл не может быть создан");
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
