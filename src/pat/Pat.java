import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import types.Deadline;
import types.Event;
import types.Todo;

import exceptions.EmptyDateException;
import exceptions.InvalidCommandException;

public class Pat {
    TaskList tasklist;
    Storage storage;

    public Pat(Path dataPath) {
        tasklist = new TaskList();
        storage = new Storage(tasklist, dataPath);
    }

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
            String[] deadlineByPair = Parser.parseDeadline(commandArgsPair);
            tasklist.addToList(new Deadline(deadlineByPair));
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
            String[] eventFromToTriplet = Parser.parseEvent(commandArgsPair);
            tasklist.addToList(new Event(eventFromToTriplet));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid event to add!");
            Ui.sayln("event [task] /from [start] /to [end]");
        } catch (EmptyDateException e) {
            Ui.sayln("Please give me a valid start and end date/time");
            Ui.sayln("event [task] /from [start] /to [end]");
        }
    }

    public void runChat() {
        Ui.greet();
        String line;
        Scanner in = new Scanner(System.in);
        boolean saidBye = false;
        do {
            line = Ui.readCommand(in);
            String[] commandArgsPair = Parser.splitCommandArgs(line);
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
        storage.writeListToFile();
        Ui.bye();
    }

    public static void main(String[] args) {
        Path dataPath = Paths.get("data", "list.txt");
        new Pat(dataPath).runChat();
    }
}
