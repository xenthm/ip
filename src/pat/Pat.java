import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import types.Deadline;
import types.Event;
import types.Task;
import types.Todo;

import exceptions.EmptyDateException;
import exceptions.InvalidCommandException;

public class Pat {
    TaskList tasklist;

    // data path
    private final Path dataPath = Paths.get("data", "list.txt");

    private void handleTodo(String[] commandArgsPair) {
        try {
            String todo = commandArgsPair[1].trim();
            if (todo.isEmpty()) {
                throw new InvalidCommandException();
            }
            tasklist.addToList(new Todo(todo));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid todo to add!");
            Ui.sayln("todo [task]");
        }
    }

    private void handleDeadline(String[] commandArgsPair) {
        try {
            String[] deadlineByPair = commandArgsPair[1].split("/by", 2);
            String deadline = deadlineByPair[0].trim();
            String by = deadlineByPair[1].trim();
            if (deadline.isEmpty()) {
                throw new InvalidCommandException();
            } else if (by.isEmpty()) {
                throw new EmptyDateException("By");
            }
            tasklist.addToList(new Deadline(deadline, by));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid deadline to add!");
            Ui.sayln("deadline [task] /by [deadline]");
        } catch (EmptyDateException e) {
            Ui.sayln("Please give me a valid due date/time");
            Ui.sayln("deadline [task] /by [deadline]");
        }
    }

    private void handleEvent(String[] commandArgsPair) {
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
            tasklist.addToList(new Event(event, from, to));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid event to add!");
            Ui.sayln("event [task] /from [start] /to [end]");
        } catch (EmptyDateException e) {
            Ui.sayln("Please give me a valid start and end date/time");
            Ui.sayln("event [task] /from [start] /to [end]");
        }
    }

    private void checkDataFile(File dataFile) throws IOException, RuntimeException {
        // Checks if data directory exists
        if (dataFile.getParentFile().mkdirs()) {
            Ui.sayRedln("Directory \"" + dataFile.getParentFile() + "\" created.");
        }

        // Checks if file exists
        if (dataFile.createNewFile()) {
            Ui.sayRedln("File \"" + dataFile.getName() + "\" created.");
        }
    }

    private void appendTask(StringBuilder result, Task task) {
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

    private String listToFile() {
        StringBuilder result = new StringBuilder();
        for (Task task : tasklist) {
            appendTask(result, task);
        }
        return result.toString();
    }

    private void writeListToFile() {
        FileWriter fw = null;
        File dataFile = dataPath.toFile();
        try {
            checkDataFile(dataFile);
            fw = new FileWriter(dataFile);
            fw.write(listToFile());
        } catch (IOException e) {
            Ui.sayRedln("Cannot write to file: " + e.getMessage());
        } catch (RuntimeException e) {
            Ui.sayRedln("Other error writing to file: " + e.getMessage());
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    Ui.sayRedln("Cannot close file: " + e.getMessage());
                }
            }
        }
    }

    private void readList() {
        File dataFile = dataPath.toFile();
        if (dataFile.exists()) {
            try {
                Scanner s = new Scanner(dataFile);
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    if (line.equals(System.lineSeparator())) {
                        break;
                    }
                    String[] taskParams = line.split("\\|");
                    char taskType = taskParams[0].charAt(0);
                    String description = taskParams[1];
                    boolean isDone;
                    switch (taskType) {
                    case 'T':
                        isDone = taskParams[2].equals("y");
                        tasklist.add(new Todo(description, isDone));
                        break;
                    case 'D':
                        String by = taskParams[2];
                        isDone = taskParams[3].equals("y");
                        tasklist.add(new Deadline(description, by, isDone));
                        break;
                    case 'E':
                        String from = taskParams[2];
                        String to = taskParams[3];
                        isDone = taskParams[4].equals("y");
                        tasklist.add(new Event(description, from, to, isDone));
                        break;
                    default:
                        System.out.println("Invalid task type in data file");
                        break;
                    }
                }
                Ui.sayRedln("Previous list data restored!");
            } catch (FileNotFoundException e) {
                Ui.sayRedln("File not found: " + e.getMessage());
            }
        }
    }

    public void runChat() {
        String line;
        Scanner in = new Scanner(System.in);
        boolean saidBye = false;
        do {
            System.out.print("Type your command: ");
            line = in.nextLine().trim();
            if (line.isEmpty()) {
                System.out.print(Ui.ANSI_MOVE_TO_START_OF_PREVIOUS_LINE);
                continue;
            }
            System.out.print(Ui.ANSI_MOVE_TO_START_OF_PREVIOUS_LINE + Ui.ANSI_ERASE_LINE + line
                    + Ui.ANSI_MOVE_TO_START_OF_NEXT_LINE);

            String[] commandArgsPair = line.split(" ", 2);
            String command = commandArgsPair[0];
            switch (command) {
            case "bye":
                saidBye = true;
                break;
            case "list":
                tasklist.printList();
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
                tasklist.markTask(commandArgsPair);
                break;
            case "unmark":
                tasklist.unmarkTask(commandArgsPair);
                break;
            case "delete":
                tasklist.deleteTask(commandArgsPair);
                break;
            default:
                Ui.sayln("Please give me a valid command!");
                Ui.sayln("[bye/list/todo/deadline/event/mark/unmark/delete]");
            }
        } while (!saidBye);
    }

    public Pat() {
        tasklist = new TaskList();
        readList();
        Ui.greet();
        runChat();
        writeListToFile();
        Ui.bye();
    }

    public static void main(String[] args) {
        Pat pat = new Pat();
    }
}
