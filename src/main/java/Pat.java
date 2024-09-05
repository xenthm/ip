import java.util.Scanner;
import java.util.ArrayList;

public class Pat {
    // ANSI escape codes to change format chat
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_PINK = "\033[95m";
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_ERASE_LINE = "\033[2K";
    public static final String ANSI_MOVE_TO_START_OF_PREVIOUS_LINE = "\033[F";
    public static final String ANSI_MOVE_TO_START_OF_NEXT_LINE = "\033[E";
    public static final String INDENT = "    ";

    private static final ArrayList<Task> todoList = new ArrayList<>();

    private static void say(String message) {
        System.out.println(ANSI_PINK + message + ANSI_RESET);
    }

    private static void greet() {
        System.out.println(ANSI_RED + "(Type \"bye\" to end the chat)" + ANSI_RESET);
        say("Hello! This is Pat!");
        say("What can I do for you?");
    }

    private static void bye() {
        say("Bye! Remember, this is Pat!");
    }

    private static void addToTodo(Task task) {
        todoList.add(task);
        say("Added this task: ");
        say(INDENT + task.getTask());
        int size = todoList.size();
        say("Now you have " + size + (size == 1 ? " task" : " tasks") + " in the list");
    }

    private static void printList() {
        if (todoList.isEmpty()) {
            say("Your todo list is empty!");
            return;
        }
        say("Here's your todo list!");
        for (int i = 0; i < todoList.size(); i++) {
            say(INDENT + (i + 1) + "." + todoList.get(i).getTask());
        }
    }

    private static void markTask(String arg) {
        try {
            Task task = todoList.get(Integer.parseInt(arg) - 1);
            task.markDone();
            say("Nice! I've marked this task as done:");
            say(task.getTask());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            say("Please tell me a valid task to mark!");
            say("mark [task number from list]");
        }
    }

    private static void unmarkTask(String arg) {
        try {
            Task task = todoList.get(Integer.parseInt(arg) - 1);
            task.markNotDone();
            say("OK, I've marked this task as not done yet:");
            say(task.getTask());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            say("Please tell me a valid task to unmark!");
            say("unmark [task number from list]");
        }
    }

    private static void handleTodo(String arg) {
        try {
            String todo = arg.trim();
            if (todo.isEmpty()) {
                throw new IllegalArgumentException("Empty todo");
            }
            addToTodo(new Todo(todo));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            say("Please give me a valid todo to add!");
            say("todo [task]");
        }
    }

    private static void handleDeadline(String arg) {
        try {
            String[] splitLineBy = arg.split(" /by ", 2);
            String deadline = splitLineBy[0].trim();
            String by = splitLineBy[1].trim();
            if (deadline.isEmpty()) {
                throw new IllegalArgumentException("Empty deadline");
            } else if (by.isEmpty()) {
                throw new IllegalArgumentException("Empty by");
            }
            addToTodo(new Deadline(deadline, by));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            say("Please give me a valid deadline to add!");
            say("deadline [task] /by [deadline]");
        }
    }

    private static void handleEvent(String arg) {
        try {
            String[] splitLineFrom = arg.split(" /from ", 2);
            String event = splitLineFrom[0].trim();
            String[] splitLineTo = splitLineFrom[1].split(" /to ", 2);
            String from = splitLineTo[0].trim();
            String to = splitLineTo[1].trim();
            if (event.isEmpty()) {
                throw new IllegalArgumentException("Empty event");
            } else if (from.isEmpty()) {
                throw new IllegalArgumentException("Empty from");
            } else if (to.isEmpty()) {
                throw new IllegalArgumentException("Empty to");
            }
            addToTodo(new Event(event, from, to));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            say("Please give me a valid event to add!");
            say("event [task] /from [start] /to [end]");
        }
    }

    private static void runChat() {
        String line;
        Scanner in = new Scanner(System.in);
        boolean saidBye = false;
        do {
            System.out.print("Type your message: ");
            line = in.nextLine().trim();
            if (line.isEmpty()) {
                System.out.print(ANSI_MOVE_TO_START_OF_PREVIOUS_LINE);
                continue;
            }
            System.out.print(ANSI_MOVE_TO_START_OF_PREVIOUS_LINE + ANSI_ERASE_LINE + line + ANSI_MOVE_TO_START_OF_NEXT_LINE);

            String[] splitLine = line.split(" ", 2);
            String command = splitLine[0];
            switch (command) {
            case "bye":
                saidBye = true;
                break;
            case "list":
                printList();
                break;
            case "mark":
                markTask(splitLine[1]);
                break;
            case "unmark":
                unmarkTask(splitLine[1]);
                break;
            case "todo":
                handleTodo(splitLine[1]);
                break;
            case "deadline":
                handleDeadline(splitLine[1]);
                break;
            case "event":
                handleEvent(splitLine[1]);
                break;
            default:
                say("Please give me a valid command!");
                say("[command] [arguments]");
            }
        } while (!saidBye);
    }

    public static void main(String[] args) {
        greet();
        runChat();
        bye();
    }
}
