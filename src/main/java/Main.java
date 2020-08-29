import twitch.Checkable;
import twitch.Utils;
import twitch.Checker;
import twitch.FollowSender;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static Checker checker;
    private static FollowSender followSender;
    private static Scanner scanner = new Scanner(System.in);

    private static File tokenFile;
    private static String channel;
    private static int amount;

    public static void main(String[] args) throws IOException {
        checkArguments(args);
    }


    private static void checkArguments(String[] args) throws IOException {
        if (args.length > 1) {
            throw new IllegalArgumentException("Wrong arguments");
        }
        switch (args[0].toLowerCase()) {
            case "follow":
                tokenFile = chooseTokenFile();
                channel = chooseChannel();
                amount = chooseAmount();
                followSender = new FollowSender(tokenFile, channel, amount);
                followSender.start();
                break;
            case "check":
                tokenFile = chooseTokenFile();
                checker = new Checker(tokenFile, 0);
                checker.start();
                break;
            case "clean":
                tokenFile = chooseTokenFile();
                checker = new Checker(tokenFile, 1);
                checker.start();
                break;
        }
    }

    private static File chooseTokenFile() {
        System.out.print("Token file: ");
        File file = new File(scanner.nextLine());
        if (Checkable.validFile(file)) {
            System.out.println("! WRONG FILE ! TRY AGAIN");
            chooseTokenFile();
        }
        return file;
    }

    private static String chooseChannel() {
        System.out.print("Channel to follow: ");
        String channel = scanner.nextLine();
        if (!Utils.channelExists(channel)) {
            System.out.println("! WRONG CHANNEL ! TRY AGAIN");
            chooseChannel();
        }
        return channel;
    }

    private static int chooseAmount() {
        System.out.print("Amount of followers (1 < amount < 10000): ");
        int amount = scanner.nextInt();
        if (amount > 10000 || amount < 1) {
            System.out.println("! WRONG AMOUNT ! TRY AGAIN");
            chooseAmount();
        }
        return amount;
    }
}
