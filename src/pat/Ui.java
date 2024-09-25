package pat;

import java.util.Scanner;

/**
 * Utility class for interactions with the user. Its key cosmetic feature is the usage of ANSI escape codes to change
 * text colours and move the cursor around.
 */
public class Ui {
    // ANSI escape codes to change chat format
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_PINK = "\033[95m";
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_ERASE_LINE = "\033[2K";
    public static final String ANSI_MOVE_TO_START_OF_PREVIOUS_LINE = "\033[F";
    public static final String ANSI_MOVE_TO_START_OF_NEXT_LINE = "\033[E";
    public static final String INDENT = "    ";

    /**
     * Prints the <code>message</code> in pink without a newline.
     */
    public static void say(String message) {
        System.out.print(ANSI_PINK + message + ANSI_RESET);
    }

    /**
     * Prints the <code>message</code> in pink with a newline.
     */
    public static void sayln(String message) {
        System.out.println(ANSI_PINK + message + ANSI_RESET);
    }

    /**
     * Prints the <code>message</code> in red with a newline.
     */
    public static void sayRedln(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    /**
     * Prints instructions and greeting message.
     */
    public static void greet() {
        sayRedln("(Type \"bye\" to end the chat)");
        sayln("Hello! This is Pat!");
        sayln("What can I do for you?");
    }

    /**
     * Prints goodbye message.
     */
    public static void bye() {
        sayln("Bye! Remember, this is Pat!");
    }

    /**
     * Returns line typed by the user after requesting for user input. Overrides the request text with the user input
     * to function as a chat log for user inputs.
     *
     * @param scanner <code>Scanner</code> object for reading user input.
     */
    public static String readCommand(Scanner scanner) {
        String line;
        System.out.print("Type your command: ");
        line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            System.out.print(ANSI_MOVE_TO_START_OF_PREVIOUS_LINE);
        } else {
            System.out.print(ANSI_MOVE_TO_START_OF_PREVIOUS_LINE + ANSI_ERASE_LINE + line
                    + ANSI_MOVE_TO_START_OF_NEXT_LINE);
        }
        return line;
    }
}
