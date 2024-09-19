package types;

public class Deadline extends Task {
    private String by;

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public Deadline(String[] deadlineByPair) {
        this(deadlineByPair[0], deadlineByPair[1]);
    }

    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public String getTask() {
        return "[D]" + super.getTask() + " (by: " + by + ")";
    }
}
