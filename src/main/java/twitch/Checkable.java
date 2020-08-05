package twitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class Checkable {

    final File fileIn;
    final File fileOut;

    Checkable(String fileNameIn, String fileNameOut) {
        this.fileIn = createFile(fileNameIn, false);
        this.fileOut = createFile(fileNameOut, true);
    }

    Checkable(File fileIn, String fileNameOut) {
        this.fileIn = fileIn;
        this.fileOut = createFile(fileNameOut, true);
    }

    Checkable(File fileIn) {
        this.fileIn = fileIn;
        this.fileOut = new File("");
    }

    private static File createFile(String fileName, boolean createFile) {
        File resultFile = new File(fileName);
        if (validFile(resultFile)) {
            try {
                if (!createFile) {
                    throw new FileNotFoundException();
                }
                resultFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultFile;
    }

    public static boolean validFile(File file) {
        return (!file.exists() || !file.isFile() || !file.getAbsolutePath().endsWith(".txt"));
    }

    File getFileIn() {
        return fileIn;
    }

    File getFileOut() {
        return fileOut;
    }

}
