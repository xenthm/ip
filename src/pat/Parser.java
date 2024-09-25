package pat;

import pat.exceptions.EmptyDateException;
import pat.exceptions.InvalidCommandException;
import pat.types.Deadline;
import pat.types.Event;
import pat.types.Task;
import pat.types.Todo;

/**
 * Utility class to parse commands given to the Pat chatbot. Also contains the logic for the various commands.
 */
public class Parser {
    /**
     * Splits the user input into command and arguments.
     *
     * @param line User input from <code>Scanner</code>.
     * @return <code>String</code> array with the command as the first element and its arguments in the second element.
     */
    private static String[] splitCommandArgs(String line) {
        return line.split(" ", 2);
    }

    /**
     * Returns true if the user has typed the "bye" command.
     */
    public static boolean saidBye(String line) {
        return splitCommandArgs(line)[0].equals("bye");
    }

    /**
     * Extracts the command from the user input and performs the corresponding action. Saves the state of the
     * <code>Task</code> list after every command that modifies it.
     *
     * @param taskList <code>TaskList</code> from Pat.
     * @param storage  <code>Storage</code> object for Pat.
     * @param line     User input from <code>Scanner</code>.
     */
    public static void parseCommand(TaskList taskList, Storage storage, String line) {
        String[] commandArgsPair = splitCommandArgs(line);
        String command = commandArgsPair[0];
        switch (command) {
        case "list":
            taskList.printList();
            break;
        case "todo":
            handleTodo(taskList, commandArgsPair);
            storage.writeListToFile();
            break;
        case "deadline":
            handleDeadline(taskList, commandArgsPair);
            storage.writeListToFile();
            break;
        case "event":
            handleEvent(taskList, commandArgsPair);
            storage.writeListToFile();
            break;
        case "mark":
            markTask(taskList, commandArgsPair);
            storage.writeListToFile();
            break;
        case "unmark":
            unmarkTask(taskList, commandArgsPair);
            storage.writeListToFile();
            break;
        case "delete":
            deleteTask(taskList, commandArgsPair);
            storage.writeListToFile();
            break;
        case "find":
            findTask(taskList, commandArgsPair);
            break;
        case "bye", "":
            break;
        default:
            Ui.sayln("Please give me a valid command!");
            Ui.sayln("[bye/list/todo/deadline/event/mark/unmark/delete/find]");
        }
    }

    /**
     * Returns the argument for a one-argument command (eg. adding a <code>Todo</code>) from parsed user input.
     *
     * @param commandArgsPair <code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     * @return The argument given by the user, if it exists.
     * @throws InvalidCommandException   If the user just typed a bunch of spaces after the command.
     * @throws IndexOutOfBoundsException If no argument was provided by the user.
     */
    public static String parseOneArg(String[] commandArgsPair) throws InvalidCommandException,
            IndexOutOfBoundsException {
        String arg = commandArgsPair[1].trim();
        if (arg.isEmpty()) {
            throw new InvalidCommandException();
        }
        return arg;
    }

    /**
     * Adds a new <code>Todo</code> to the <code>Task</code> list based on the parsed user input.
     *
     * @param taskList        <code>TaskList</code> from Pat.
     * @param commandArgsPair code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     */
    private static void handleTodo(TaskList taskList, String[] commandArgsPair) {
        try {
            String todo = parseOneArg(commandArgsPair);
            taskList.addToList(new Todo(todo));
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid todo to add!");
            Ui.sayln("todo [task]");
        }
    }

    /**
     * Further splits the parsed user input for the add <code>Deadline</code> command.
     *
     * @param commandArgsPair <code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     * @return A <code>String</code> array with the <code>Deadline</code> description in the first element and the
     * deadline in the second element.
     * @throws InvalidCommandException If no command was provided.
     * @throws EmptyDateException      If no <code>by</code> argument was provided.
     */
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

    /**
     * Adds a new <code>Deadline</code> to the <code>Task</code> list based on the parsed user input.
     *
     * @param taskList        <code>TaskList</code> from Pat.
     * @param commandArgsPair code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     */
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

    /**
     * Further splits the parsed user input for the add <code>Event</code> command.
     *
     * @param commandArgsPair <code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     * @return A <code>String</code> array with the <code>Event</code> description in the first element, the
     * start time in the second element, and the end time in the third element.
     * @throws InvalidCommandException If no command was provided.
     * @throws EmptyDateException      If no <code>from</code> or <code>to</code> argument was provided.
     */
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

    /**
     * Adds a new <code>Event</code> to the <code>Task</code> list based on the parsed user input.
     *
     * @param taskList        <code>TaskList</code> from Pat.
     * @param commandArgsPair code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     */
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

    /**
     * Returns the task number for the commands that require it (eg. mark, unmark, delete) from parsed user input.
     *
     * @param commandArgsPair <code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     * @return An <code>Integer</code> representing the task number of the <code>Task</code> in the list.
     * @throws InvalidCommandException If no argument was provided.
     * @throws NumberFormatException   If the argument is not a parsable integer.
     */
    private static int parseTaskNumber(String[] commandArgsPair) throws NumberFormatException, InvalidCommandException,
            IndexOutOfBoundsException {
        if (commandArgsPair.length < 2) {   // No task number provided
            throw new InvalidCommandException();
        }
        return Integer.parseInt(commandArgsPair[1].trim());
    }

    /**
     * Marks <code>Task</code> with given task number as done.
     *
     * @param taskList        <code>TaskList</code> from Pat.
     * @param commandArgsPair code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     */
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

    /**
     * Unmarks <code>Task</code> with given task number as not done.
     *
     * @param taskList        <code>TaskList</code> from Pat.
     * @param commandArgsPair code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     */
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

    /**
     * Deletes <code>Task</code> with given task number.
     *
     * @param taskList        <code>TaskList</code> from Pat.
     * @param commandArgsPair code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     */
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

    /**
     * Finds all <code>Task</code> that contain the given keyword from user input.
     *
     * @param taskList        <code>TaskList</code> from Pat.
     * @param commandArgsPair code>String</code> array with the command in the first element and potentially an
     *                        argument in the second element.
     */
    private static void findTask(TaskList taskList, String[] commandArgsPair) {
        if (taskList.isEmpty()) {
            Ui.sayln("Your task list is empty, please add a task before finding!");
            return;
        }
        try {
            String keyword = parseOneArg(commandArgsPair);
            taskList.printAllWithKeyword(keyword);
        } catch (InvalidCommandException | IndexOutOfBoundsException e) {
            Ui.sayln("Please give me a valid keyword to find!");
            Ui.sayln("find [keyword]");
        }
    }
}
