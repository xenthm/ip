import java.util.Scanner;
import java.util.ArrayList;

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
                What can I do for you?""");
    }

    private static void bye() {
        say("Bye! Remember, this is Pat!");
    }

    private static void runChat() {
        String line;
        Scanner in = new Scanner(System.in);
        boolean saidBye = false;
        do {
            System.out.print("Type your message: ");
            line = in.nextLine();
            System.out.print("\033[F\033[1G\033[2K" + line + "\033[E");
            if (line.equals("bye")) {
                saidBye = true;
            } else {
                say(line);
            }
        } while (!saidBye);
    }

    public static void main(String[] args) {
        System.out.println("(Type \"bye\" to end the chat)");
        greet();
        runChat();
        bye();
    }
}
