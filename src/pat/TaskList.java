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

    public void printAllWithKeyword(String keyword) {
        boolean atLeastOneFound = false;
        Ui.sayln("Here are the matching tasks in your list:");
        for (int i = 0; i < size(); i++) {
            Task task = this.get(i);
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                atLeastOneFound = true;
                Ui.sayln(Ui.INDENT + (i + 1) + "." + this.get(i).getTask());
            }
        }
        if (!atLeastOneFound) {
            Ui.say(Ui.ANSI_MOVE_TO_START_OF_PREVIOUS_LINE + Ui.ANSI_ERASE_LINE);
            Ui.sayln("No tasks with keyword \"" + keyword + "\" found!");
        }
    }
}
