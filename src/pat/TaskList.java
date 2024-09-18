import exceptions.InvalidCommandException;
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

    // todo: combine the following 2 methods into 1 by defining another parameter that takes in "mark" or "unmark"
    public void markTask(String[] commandArgsPair) {
        if (isEmpty()) {
            Ui.sayln("Your task list is empty, please add a task before marking!");
            return;
        }
        try {
            if (commandArgsPair.length < 2) {   // No task number provided
                throw new InvalidCommandException();
            }
            int taskNumber = Integer.parseInt(commandArgsPair[1]);
            Task task = get(taskNumber - 1);
            task.markDone();
            Ui.sayln("Nice! I've marked this task as done:");
            Ui.sayln(task.getTask());
        } catch (NumberFormatException | InvalidCommandException e) {
            Ui.sayln("Please tell me a valid task number to mark!");
            Ui.sayln("Run `list` to see all available tasks.");
            Ui.sayln("mark [task number from list]");
        } catch (IndexOutOfBoundsException e) {
            Ui.sayln("Task does not exist!");
            Ui.say("Please provide a task number from 1 to ");
            Ui.sayln(String.valueOf(size()));
        }
    }

    public void unmarkTask(String[] commandArgsPair) {
        if (isEmpty()) {
            Ui.sayln("Your task list is empty, please add a task before unmarking!");
            return;
        }
        try {
            if (commandArgsPair.length < 2) {   // No task number provided
                throw new InvalidCommandException();
            }
            int taskNumber = Integer.parseInt(commandArgsPair[1]);
            Task task = get(taskNumber - 1);
            task.markNotDone();
            Ui.sayln("OK, I've marked this task as not done yet:");
            Ui.sayln(task.getTask());
        } catch (NumberFormatException | InvalidCommandException e) {
            Ui.sayln("Please tell me a valid task number to unmark!");
            Ui.sayln("Run `list` to see all available tasks.");
            Ui.sayln("unmark [task number from list]");
        } catch (IndexOutOfBoundsException e) {
            Ui.sayln("Task does not exist!");
            Ui.say("Please provide a task number from 1 to ");
            Ui.sayln(String.valueOf(size()));
        }
    }

    public void deleteTask(String[] commandArgsPair) {
        if (isEmpty()) {
            Ui.sayln("Your task list is empty, please add a task before deleting!");
            return;
        }
        try {
            if (commandArgsPair.length < 2) {   // No task number provided
                throw new InvalidCommandException();
            }
            int taskNumber = Integer.parseInt(commandArgsPair[1]);
            Task task = remove(taskNumber - 1);
            Ui.sayln("OK, I've removed this task:");
            Ui.sayln(task.getTask());
            int size = size();
            if (size > 0) {
                Ui.sayln("Now you have " + size + (size == 1 ? " task" : " tasks") + " in the list");
            } else {
                Ui.sayln("Your task list is empty. ");
            }
        } catch (NumberFormatException | InvalidCommandException e) {
            Ui.sayln("Please tell me a valid task number to delete!");
            Ui.sayln("Run `list` to see all available tasks.");
            Ui.sayln("delete [task number from list]");
        } catch (IndexOutOfBoundsException e) {
            Ui.sayln("Task does not exist!");
            Ui.say("Please provide a task number from 1 to ");
            Ui.sayln(String.valueOf(size()));
        }
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
