package pat.types;

/**
 * Class for <code>Todo</code>. Behaves like a <code>Task</code>, but with an overridden <code>getTask</code> method.
 */
public class Todo extends Task {
    /**
     * Constructor for <code>Todo</code>. Sets <code>description</code> and <code>isDone</code> to false by default.
     *
     * @param description Description for <code>Todo</code>.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Constructor for <code>Todo</code>. Allows <code>isDone</code> to be specified.
     *
     * @param description Description for <code>Todo</code>.
     * @param isDone      Boolean for whether the <code>Todo</code> is done or not.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Returns <code>String</code> form of <code>Todo</code>.
     */
    @Override
    public String getTask() {
        return "[T]" + super.getTask();
    }
}
