import exceptions.EmptyDateException;
import exceptions.InvalidCommandException;

public class Parser {
    public static String[] splitCommandArgs(String line) {
        return line.split(" ", 2);
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
}
