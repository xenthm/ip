package types;

/**
 * Class for <code>Event</code>. Includes a start and end time stored in <code>from </code> and <code>to</code>
 * respectively.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Constructor for <code>Event</code>. Sets <code>description</code>, <code>from</code>, <code>to</code>, and
     * <code>isDone</code> to false by default.
     *
     * @param description Description for <code>Event</code>.
     * @param from        Start time for the task given in <code>description</code>.
     * @param to          End time for the task given in <code>description</code>.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Constructor for <code>Event</code>. Sets <code>description</code>, <code>from</code>, <code>to</code>, and
     * <code>isDone</code> to false by default.
     *
     * @param eventFromToTriplet Array of <code>String</code> with the <code>description</code> as the first element,
     *                           start time as the second element, and end time as the third element.
     */
    public Event(String[] eventFromToTriplet) {
        this(eventFromToTriplet[0], eventFromToTriplet[1], eventFromToTriplet[2]);
    }

    /**
     * Constructor for <code>Event</code>. Allows <code>isDone</code> to be specified.
     *
     * @param description Description for <code>Event</code>.
     * @param from        Start time for the task given in <code>description</code>.
     * @param to          End time for the task given in <code>description</code>.
     * @param isDone      Boolean for whether the <code>Event</code> is done or not.
     */
    public Event(String description, String from, String to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    /**
     * Returns <code>String</code> form of <code>Event</code>.
     */
    @Override
    public String getTask() {
        return "[E]" + super.getTask() + " (from: " + from + " to: " + to + ")";
    }
}
