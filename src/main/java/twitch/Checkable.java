package twitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class Checkable {

    final ThreadPoolExecutor executor;

    final File fileIn;
    final File fileOut;

    Checkable(String fileNameIn, String fileNameOut, int threads) {
        this.fileIn = createFile(fileNameIn, false);
        this.fileOut = createFile(fileNameOut, true);
        executor = new ThreadPoolExecutor(0,
                threads,
                4L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    Checkable(File fileIn, String fileNameOut, int threads) {
        this.fileIn = fileIn;
        this.fileOut = createFile(fileNameOut, true);
        executor = new ThreadPoolExecutor(0,
                threads,
                4L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    Checkable(File fileIn, int threads) {
        this.fileIn = fileIn;
        this.fileOut = new File("");
        executor = new ThreadPoolExecutor(0,
                threads,
                4L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private static File createFile(String fileName, boolean createFile) {
        File resultFile = new File(fileName);
        if (invalidFile(resultFile)) {
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

    public static boolean invalidFile(File file) {
        return (!file.exists() || !file.isFile() || !file.getAbsolutePath().endsWith(".txt"));
    }

}
