public class Ui {
    // ANSI escape codes to change chat format
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_PINK = "\033[95m";
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_ERASE_LINE = "\033[2K";
    public static final String ANSI_MOVE_TO_START_OF_PREVIOUS_LINE = "\033[F";
    public static final String ANSI_MOVE_TO_START_OF_NEXT_LINE = "\033[E";
    public static final String INDENT = "    ";

    public static void say(String message) {
        System.out.print(ANSI_PINK + message + ANSI_RESET);
    }

    public static void sayln(String message) {
        System.out.println(ANSI_PINK + message + ANSI_RESET);
    }

    public static void sayRedln(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    public static void greet() {
        sayRedln("(Type \"bye\" to end the chat)");
        sayln("Hello! This is Pat!");
        sayln("What can I do for you?");
    }

    public static void bye() {
        sayln("Bye! Remember, this is Pat!");
    }
}
