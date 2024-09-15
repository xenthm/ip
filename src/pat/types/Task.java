package types;

public class Task {
    protected String description;
    protected boolean isDone;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

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
