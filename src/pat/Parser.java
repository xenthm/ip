import exceptions.EmptyDateException;
import exceptions.InvalidCommandException;
import types.Deadline;
import types.Event;
import types.Task;
import types.Todo;

public class Parser {
    private static String[] splitCommandArgs(String line) {
        return line.split(" ", 2);
    }

    public static boolean saidBye(String line) {
        return splitCommandArgs(line)[0].equals("bye");
    }

    public static void parseCommand(TaskList taskList, String line) {
        String[] commandArgsPair = splitCommandArgs(line);
        String command = commandArgsPair[0];
        switch (command) {
        case "list":
            taskList.printList();
            break;
        case "todo":
            handleTodo(taskList, commandArgsPair);
            break;
        case "deadline":
            handleDeadline(taskList, commandArgsPair);
            break;
        case "event":
            handleEvent(taskList, commandArgsPair);
            break;
        case "mark":
            markTask(taskList, commandArgsPair);
            break;
        case "unmark":
            unmarkTask(taskList, commandArgsPair);
            break;
        case "delete":
            deleteTask(taskList, commandArgsPair);
            break;
        default:
            Ui.sayln("Please give me a valid command!");
            Ui.sayln("[bye/list/todo/deadline/event/mark/unmark/delete]");
        }
    }

    public static String parseTodo(String[] commandArgsPair) throws InvalidCommandException, IndexOutOfBoundsException {
        String todo = commandArgsPair[1].trim();
        if (todo.isEmpty()) {
            throw new InvalidCommandException();
        }
        return todo;
    }

    private static void handleTodo(TaskList taskList, String[] commandArgsPair) {
        try {
            String todo = parseTodo(commandArgsPair);
            taskList.addToList(new Todo(todo));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid todo to add!");
            Ui.sayln("todo [task]");
        }
    }

    public static String[] parseDeadline(String[] commandArgsPair) throws InvalidCommandException, EmptyDateException,
            IndexOutOfBoundsException {
        String[] deadlineByPair = commandArgsPair[1].split("/by", 2);
        String deadline = deadlineByPair[0].trim();
        String by = deadlineByPair[1].trim();
        if (deadline.isEmpty()) {
            throw new InvalidCommandException();
        } else if (by.isEmpty()) {
            throw new EmptyDateException("By");
        }
        return new String[]{deadline, by};
    }

    private static void handleDeadline(TaskList taskList, String[] commandArgsPair) {
        try {
            String[] deadlineByPair = Parser.parseDeadline(commandArgsPair);
            taskList.addToList(new Deadline(deadlineByPair));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid deadline to add!");
            Ui.sayln("deadline [task] /by [deadline]");
        } catch (EmptyDateException e) {
            Ui.sayln("Please give me a valid due date/time");
            Ui.sayln("deadline [task] /by [deadline]");
        }
    }

    public static String[] parseEvent(String[] commandArgsPair) throws InvalidCommandException, EmptyDateException,
            IndexOutOfBoundsException {
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
        return new String[]{event, from, to};
    }

    private static void handleEvent(TaskList taskList, String[] commandArgsPair) {
        try {
            String[] eventFromToTriplet = Parser.parseEvent(commandArgsPair);
            taskList.addToList(new Event(eventFromToTriplet));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid event to add!");
            Ui.sayln("event [task] /from [start] /to [end]");
        } catch (EmptyDateException e) {
            Ui.sayln("Please give me a valid start and end date/time");
            Ui.sayln("event [task] /from [start] /to [end]");
        }
    }

    private static int parseTaskNumber(String[] commandArgsPair) throws NumberFormatException, InvalidCommandException,
            IndexOutOfBoundsException {
        if (commandArgsPair.length < 2) {   // No task number provided
            throw new InvalidCommandException();
        }
        return Integer.parseInt(commandArgsPair[1]);
    }

    private static void markTask(TaskList taskList, String[] commandArgsPair) {
        if (taskList.isEmpty()) {
            Ui.sayln("Your task list is empty, please add a task before marking!");
            return;
        }
        try {
            int taskNumber = parseTaskNumber(commandArgsPair);
            Task task = taskList.get(taskNumber - 1);
            task.markDone();
            Ui.sayln("Nice! I've marked this task as done:");
            Ui.sayln(task.getTask());
        } catch (NumberFormatException | InvalidCommandException e) {
            Ui.sayln("Please tell me a valid task number to mark!");
            Ui.sayln("Run `list` to see all available tasks.");
            Ui.sayln("mark [task number from list]");
        } catch (IndexOutOfBoundsException e) {
            Ui.sayln("Task does not exist!");
            Ui.say("Please provide a task number from 1 to ");
            Ui.sayln(String.valueOf(taskList.size()));
        }
    }

    private static void unmarkTask(TaskList taskList, String[] commandArgsPair) {
        if (taskList.isEmpty()) {
            Ui.sayln("Your task list is empty, please add a task before unmarking!");
            return;
        }
        try {
            int taskNumber = parseTaskNumber(commandArgsPair);
            Task task = taskList.get(taskNumber - 1);
            task.markNotDone();
            Ui.sayln("OK, I've marked this task as not done yet:");
            Ui.sayln(task.getTask());
        } catch (NumberFormatException | InvalidCommandException e) {
            Ui.sayln("Please tell me a valid task number to unmark!");
            Ui.sayln("Run `list` to see all available tasks.");
            Ui.sayln("unmark [task number from list]");
        } catch (IndexOutOfBoundsException e) {
            Ui.sayln("Task does not exist!");
            Ui.say("Please provide a task number from 1 to ");
            Ui.sayln(String.valueOf(taskList.size()));
        }
    }

    private static void deleteTask(TaskList taskList, String[] commandArgsPair) {
        if (taskList.isEmpty()) {
            Ui.sayln("Your task list is empty, please add a task before deleting!");
            return;
        }
        try {
            if (commandArgsPair.length < 2) {   // No task number provided
                throw new InvalidCommandException();
            }
            int taskNumber = parseTaskNumber(commandArgsPair);
            Task task = taskList.remove(taskNumber - 1);
            Ui.sayln("OK, I've removed this task:");
            Ui.sayln(task.getTask());
            int size = taskList.size();
            if (size > 0) {
                Ui.sayln("Now you have " + size + (size == 1 ? " task" : " tasks") + " in the list");
            } else {
                Ui.sayln("Your task list is empty. ");
            }
        } catch (NumberFormatException | InvalidCommandException e) {
            Ui.sayln("Please tell me a valid task number to delete!");
            Ui.sayln("Run `list` to see all available tasks.");
            Ui.sayln("delete [task number from list]");
        } catch (IndexOutOfBoundsException e) {
            Ui.sayln("Task does not exist!");
            Ui.say("Please provide a task number from 1 to ");
            Ui.sayln(String.valueOf(taskList.size()));
        }
    }
}
