import types.Task;

import java.util.ArrayList;

public class TaskList extends ArrayList<Task> {
    public void addToList(Task task) {
        add(task);
        Ui.sayln("Added this task: ");
        Ui.sayln(Ui.INDENT + task.getTask());
        int size = size();
        Ui.sayln("Now you have " + size + (size == 1 ? " task" : " tasks") + " in the list");
    }

    public void printList() {
        if (isEmpty()) {
            Ui.sayln("Your todo list is empty!");
            return;
        }
        Ui.sayln("Here's your todo list!");
        for (int i = 0; i < size(); i++) {
            Ui.sayln(Ui.INDENT + (i + 1) + "." + get(i).getTask());
        }
    }
}
