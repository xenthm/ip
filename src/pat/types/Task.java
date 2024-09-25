package pat.types;

/**
 * Super class for different <code>Task</code> types. Contains a description and status for whether it is marked as
 * done or not.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructor for <code>Task</code>. Sets <code>description</code> and <code>isDone</code> to false by default.
     *
     * @param description Description for <code>Task</code>.
     */
    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructor for <code>Task</code>. Allows <code>isDone</code> to be specified.
     *
     * @param description Description for <code>Task</code>.
     * @param isDone      Boolean for whether the <code>Task</code> is done or not.
     */
    protected Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns <code>String</code> form of <code>Task</code>.
     */
    public String getTask() {
        return (isDone ? "[X]" : "[ ]") + " " + description;
    }

    public void markDone() {
        isDone = true;
    }

    public void markNotDone() {
        isDone = false;
    }
}
