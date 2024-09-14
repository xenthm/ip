import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;

import types.Deadline;
import types.Event;
import types.Task;
import types.Todo;

import exceptions.EmptyDateException;
import exceptions.InvalidCommandException;

public class Pat {
    // ANSI escape codes to change format chat
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_PINK = "\033[95m";
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_ERASE_LINE = "\033[2K";
    public static final String ANSI_MOVE_TO_START_OF_PREVIOUS_LINE = "\033[F";
    public static final String ANSI_MOVE_TO_START_OF_NEXT_LINE = "\033[E";
    public static final String INDENT = "    ";

    // data path
    private static final Path dataPath = Paths.get("data", "list.txt");
    private static File dataFile;

    private static final ArrayList<Task> todoList = new ArrayList<>();

    private static void say(String message) {
        System.out.print(ANSI_PINK + message + ANSI_RESET);
    }

    private static void sayln(String message) {
        System.out.println(ANSI_PINK + message + ANSI_RESET);
    }

    private static void sayRedln(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    private static void greet() {
        sayRedln("(Type \"bye\" to end the chat)");
        sayln("Hello! This is Pat!");
        sayln("What can I do for you?");
    }

    private static void bye() {
        sayln("Bye! Remember, this is Pat!");
    }

    private static void addToList(Task task) {
        todoList.add(task);
        sayln("Added this task: ");
        sayln(INDENT + task.getTask());
        int size = todoList.size();
        sayln("Now you have " + size + (size == 1 ? " task" : " tasks") + " in the list");
    }

    private static void printList() {
        if (todoList.isEmpty()) {
            sayln("Your todo list is empty!");
            return;
        }
        sayln("Here's your todo list!");
        for (int i = 0; i < todoList.size(); i++) {
            sayln(INDENT + (i + 1) + "." + todoList.get(i).getTask());
        }
    }

    // todo: combine the following 2 methods into 1 by defining another parameter that takes in "mark" or "unmark"
    private static void markTask(String[] commandArgsPair) {
        if (todoList.isEmpty()) {
            sayln("Your todo list is empty, please add a task before marking!");
            return;
        }
        try {
            int taskNumber = Integer.parseInt(commandArgsPair[1]);
            Task task = todoList.get(taskNumber - 1);
            task.markDone();
            sayln("Nice! I've marked this task as done:");
            sayln(task.getTask());
        } catch (NumberFormatException e) {
            sayln("Please tell me a valid task number to mark!");
            sayln("Run `list` to see all available tasks.");
            sayln("mark [task number from list]");
        } catch (IndexOutOfBoundsException e) {
            sayln("Task does not exist!");
            say("Please provide a task number from 1 to ");
            sayln(String.valueOf(todoList.size()));
        }
    }

    private static void unmarkTask(String[] commandArgsPair) {
        if (todoList.isEmpty()) {
            sayln("Your todo list is empty, please add a task before unmarking!");
            return;
        }
        try {
            int taskNumber = Integer.parseInt(commandArgsPair[1]);
            Task task = todoList.get(taskNumber - 1);
            task.markNotDone();
            sayln("OK, I've marked this task as not done yet:");
            sayln(task.getTask());
        } catch (NumberFormatException e) {
            sayln("Please tell me a valid task number to unmark!");
            sayln("Run `list` to see all available tasks.");
            sayln("mark [task number from list]");
        } catch (IndexOutOfBoundsException e) {
            sayln("Task does not exist!");
            say("Please provide a task number from 1 to ");
            sayln(String.valueOf(todoList.size()));
        }
    }

    private static void handleTodo(String[] commandArgsPair) {
        try {
            String todo = commandArgsPair[1].trim();
            if (todo.isEmpty()) {
                throw new InvalidCommandException();
            }
            addToList(new Todo(todo));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            sayln("Please give me a valid todo to add!");
            sayln("todo [task]");
        }
    }

    private static void handleDeadline(String[] commandArgsPair) {
        try {
            String[] deadlineByPair = commandArgsPair[1].split("/by", 2);
            String deadline = deadlineByPair[0].trim();
            String by = deadlineByPair[1].trim();
            if (deadline.isEmpty()) {
                throw new InvalidCommandException();
            } else if (by.isEmpty()) {
                throw new EmptyDateException("By");
            }
            addToList(new Deadline(deadline, by));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            sayln("Please give me a valid deadline to add!");
            sayln("deadline [task] /by [deadline]");
        } catch (EmptyDateException e) {
            sayln("Please give me a valid due date/time");
            sayln("deadline [task] /by [deadline]");
        }
    }

    private static void handleEvent(String[] commandArgsPair) {
        try {
            String[] eventArgsPair = commandArgsPair[1].split("/from", 2);
            String event = eventArgsPair[0].trim();
            String[] fromToPair = eventArgsPair[1].split("/to", 2);
            String from = fromToPair[0].trim();
            String to = fromToPair[1].trim();
            if (event.isEmpty()) {
                throw new InvalidCommandException();
            } else if (from.isEmpty()) {
                throw new EmptyDateException("Empty from");
            } else if (to.isEmpty()) {
                throw new EmptyDateException("Empty to");
            }
            addToList(new Event(event, from, to));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            sayln("Please give me a valid event to add!");
            sayln("event [task] /from [start] /to [end]");
        } catch (EmptyDateException e) {
            sayln("Please give me a valid start and end date/time");
            sayln("event [task] /from [start] /to [end]");
        }
    }

    private static void runChat() {
        String line;
        Scanner in = new Scanner(System.in);
        boolean saidBye = false;
        do {
            System.out.print("Type your command: ");
            line = in.nextLine().trim();
            if (line.isEmpty()) {
                System.out.print(ANSI_MOVE_TO_START_OF_PREVIOUS_LINE);
                continue;
            }
            System.out.print(ANSI_MOVE_TO_START_OF_PREVIOUS_LINE + ANSI_ERASE_LINE + line
                    + ANSI_MOVE_TO_START_OF_NEXT_LINE);

            String[] commandArgsPair = line.split(" ", 2);
            String command = commandArgsPair[0];
            switch (command) {
            case "bye":
                saidBye = true;
                break;
            case "list":
                printList();
                break;
            case "todo":
                handleTodo(commandArgsPair);
                break;
            case "deadline":
                handleDeadline(commandArgsPair);
                break;
            case "event":
                handleEvent(commandArgsPair);
                break;
            case "mark":
                markTask(commandArgsPair);
                break;
            case "unmark":
                unmarkTask(commandArgsPair);
                break;
            default:
                sayln("Please give me a valid command!");
                sayln("[bye/list/todo/deadline/event/mark/unmark]");
            }
        } while (!saidBye);
    }

    private static void checkDataFile() throws IOException, RuntimeException {
        dataFile = dataPath.toFile();

        // Checks if data directory exists
        if (dataFile.getParentFile().mkdirs()) {
            sayRedln("Directory \"" + dataFile.getParentFile() + "\" created.");
        }

        // Checks if file exists
        if (dataFile.createNewFile()) {
            sayRedln("File \"" + dataFile.getName() + "\" created.");
        }
    }

    private static void appendTask(StringBuilder result, Task task) {
        Object taskClass = task.getClass();
        if (taskClass.equals(Todo.class)) {
            result.append("T|")
                    .append(task.getDescription())
                    .append("|")
                    .append(task.isDone() ? "y" : "n")
                    .append(System.lineSeparator());
        } else if (taskClass.equals(Deadline.class)) {
            result.append("D|")
                    .append(task.getDescription())
                    .append("|")
                    .append(((Deadline) task).getBy())
                    .append("|")
                    .append(task.isDone() ? "y" : "n")
                    .append(System.lineSeparator());

        } else if (taskClass.equals(Event.class)) {
            result.append("E|")
                    .append(task.getDescription())
                    .append("|")
                    .append(((Event) task).getFrom())
                    .append("|")
                    .append(((Event) task).getTo())
                    .append("|")
                    .append(task.isDone() ? "y" : "n")
                    .append(System.lineSeparator());

        } else {
            System.out.println("invalid task");
        }
    }

    private static String listToFile() {
        StringBuilder result = new StringBuilder();
        for (Task task : todoList) {
            appendTask(result, task);
        }
        return result.toString();
    }

    private static void writeListToFile() {
        FileWriter fw = null;
        try {
            checkDataFile();
            fw = new FileWriter(dataFile);
            fw.write(listToFile());
        } catch (IOException e) {
            sayRedln("Cannot write to file: " + e.getMessage());
        } catch (RuntimeException e) {
            sayRedln("Other error writing to file: " + e.getMessage());
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    sayRedln("Cannot close file: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        greet();
        runChat();
        writeListToFile();
        bye();
    }
}
