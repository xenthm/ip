import java.util.Scanner;

public class Pat {
    // ANSI escape code to change Pat's messages to pink
    public static final String PINK = "\033[95m";
    public static final String RESET = "\033[0m";

    private static void say(String msg) {
        System.out.println(PINK + msg + RESET);
    }

    private static void greet() {
        say("""
                Hello! This is Pat!
                What can I do for you?
                """);
    }

    private static void bye() {
        say("Bye! Hope to see you again!");
    }

    public static void main(String[] args) {
        greet();
        bye();
    }
}
