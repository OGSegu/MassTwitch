import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Checker {
    private final File file;
    private final File resultFile;

    public Checker(String fileURL) {
        file = getCustomFile(fileURL);
        resultFile = getResultFile();
    }

    public Checker() {
        file = getDefaultFile();
        resultFile = getResultFile();
    }

    private File getCustomFile(String fileURL) {
        File file = new File(fileURL);
        checkFile(file);
        return file;
    }

    private File getDefaultFile() {
        File file = new File("tokens.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Файл не может быть создан");
        }
        checkFile(file);
        return file;
    }

    private File getResultFile() {
        File resultFile = new File("valid.txt");
        try {
            resultFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Файл не может быть создан");
        }
        checkFile(resultFile);
        return resultFile;
    }

    private void checkFile(File file) {
        if (!file.exists() || !file.isFile() || !file.getAbsolutePath().endsWith(".txt")) {
            throw new IllegalArgumentException("Неверный файл.");
        }
    }




    public void start() {
        try (Scanner sc = new Scanner(file, "UTF-8")) {
            while (sc.hasNext()) {
                String token = sc.next();
                boolean result = checkToken(token);
                System.out.println( token + " / " + result);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e);
        }
    }

    private boolean checkToken(String token) {
        boolean result = false;

        if (result) {
            writeToFile(token);
        }
        return result;
    }

    private void writeToFile(String token) {
        try (FileWriter fileWriter = new FileWriter(resultFile)) {
            fileWriter.write(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
