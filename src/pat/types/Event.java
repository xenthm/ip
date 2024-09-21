package types;

public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public Event(String[] eventFromToTriplet) {
        this(eventFromToTriplet[0], eventFromToTriplet[1], eventFromToTriplet[2]);
    }

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

    @Override
    public String getTask() {
        return "[E]" + super.getTask() + " (from: " + from + " to: " + to + ")";
    }
}
