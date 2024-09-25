package pat.types;

/**
 * Class for <code>Deadline</code>. Includes a deadline stored in the field <code>by</code>.
 */
public class Deadline extends Task {
    private String by;

    /**
     * Constructor for <code>Deadline</code>. Sets <code>description</code>, <code>by</code>, and <code>isDone</code>
     * to false by default.
     *
     * @param description Description for <code>Deadline</code>.
     * @param by          Deadline for the task given in <code>description</code>.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Constructor for <code>Deadline</code>. Using a <code>String</code> array, sets <code>description</code>,
     * <code>by</code>, and <code>isDone</code> to false by default.
     *
     * @param deadlineByPair Array of <code>String</code> with the <code>description</code> as the first element and
     *                       <code>by</code> time as the second element.
     */
    public Deadline(String[] deadlineByPair) {
        this(deadlineByPair[0], deadlineByPair[1]);
    }

    /**
     * Constructor for <code>Deadline</code>. Allows <code>isDone</code> to be specified.
     *
     * @param description Description for <code>Deadline</code>.
     * @param by          Deadline for the task given in <code>description</code>.
     * @param isDone      Boolean for whether the <code>Deadline</code> is done or not.
     */
    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    /**
     * Returns <code>String</code> form of <code>Deadline</code>.
     */
    @Override
    public String getTask() {
        return "[D]" + super.getTask() + " (by: " + by + ")";
    }
}
