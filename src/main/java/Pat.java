import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.ArrayList;

public class Pat {
    private static ArrayList<Task> todoList = new ArrayList<>();

    // ANSI escape code to change Pat's messages to pink
    public static final String PINK = "\033[95m";
    public static final String RESET = "\033[0m";

    private static void say(String message) {
        System.out.println(PINK + message + RESET);
    }

    private static void greet() {
        say("""
                Hello! This is Pat!
                What can I do for you?""");
    }

    private static void bye() {
        say("Bye! Remember, this is Pat!");
    }

    private static void addToTodo(String task) {
        todoList.add(new Task(task));
        say("Added: " + task);
    }

    private static void printList() {
        say("Here's your todo list!");
        for (int i = 0; i < todoList.size(); i++) {
            say((i + 1) + "." + todoList.get(i).getTask());
        }
    }

    private static void runChat() {
        String line;
        Scanner in = new Scanner(System.in);
        boolean saidBye = false;
        do {
            System.out.print("Type your message: ");
            line = in.nextLine();
            if (line.trim().isEmpty()) {
                System.out.print("\033[F");
                continue;
            }
            System.out.print("\033[F\033[1G\033[2K" + line + "\033[E");

            String[] splitLine = line.split(" ", 2);
            switch (splitLine[0]) {
            case "bye":
                saidBye = true;
                break;
            case "list":
                printList();
                break;
            case "mark":
                try {
                    Task task = todoList.get(Integer.parseInt(splitLine[1]) - 1);
                    task.markDone();
                    say("Nice! I've marked this task as done:");
                    say(task.getTask());
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    say("Please tell me a valid task to mark!");
                }
                break;
            case "unmark":
                try {
                    Task task = todoList.get(Integer.parseInt(splitLine[1]) - 1);
                    task.markNotDone();
                    say("OK, I've marked this task as not done yet:");
                    say(task.getTask());
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    say("Please tell me a valid task to unmark!");
                }
                break;
            default:
                addToTodo(line);
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
