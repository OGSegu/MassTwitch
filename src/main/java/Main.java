public class Main {
    public static void main(String[] args) {
        Checker checker;
        if (args.length == 0) {
            checker = new Checker();
        }
        else if (args.length == 1) {
            checker = new Checker(args[0]);
        }
        else {
            throw new IllegalArgumentException("Пример: checker 'url'");
        }
    }
}
