package pat;

import pat.types.Deadline;
import pat.types.Event;
import pat.types.Task;
import pat.types.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Helper class to handle file storage needs for the Pat chatbot. Contains a reference to the <code>TaskList</code>
 * in <code>Pat</code>.
 */
public class Storage {
    private TaskList taskList;
    private Path dataPath;
    private File dataFile;

    /**
     * Constructor for <code>Storage</code>. Resolves given <code>Path</code> to a <code>File</code> and tries to
     * restore the task <code>Task</code> list from the file.
     *
     * @param taskList <code>TaskList</code> from Pat.
     * @param dataPath <code>Path</code> to <code>.txt</code> file.
     */
    public Storage(TaskList taskList, Path dataPath) {
        this.taskList = taskList;
        this.dataPath = dataPath;
        dataFile = dataPath.toFile();
        readList();
    }

    /**
     * Checks if the <code>.txt</code> file and its parent directory as defined in the <code>Pat</code> class
     * exists, creating them if they don't.
     *
     * @throws IOException       If unable to create file.
     * @throws SecurityException If the user doesn't have the required permissions for data file management.
     */
    private void checkDataFile() throws IOException, SecurityException {
        // Checks if data directory exists
        if (dataFile.getParentFile().mkdirs()) {
            Ui.sayRedln("Directory \"" + dataFile.getParentFile() + "\" created.");
        }

        // Checks if file exists
        if (dataFile.createNewFile()) {
            Ui.sayRedln("File \"" + dataFile.getName() + "\" created.");
        }
    }

    /**
     * Appends <code>Task</code> to a <code>StringBuilder</code> object <code>result</code> based on its subclass
     * (<code>Todo</code>, <code>Deadline</code>, or <code>Event</code>).
     *
     * @param result <code>StringBuilder</code> object to append to.
     * @param task   <code>Task</code> to append.
     */
    private void appendTask(StringBuilder result, Task task) {
        Object taskClass = task.getClass();
        if (taskClass.equals(Todo.class)) {
            result.append("T|")
                    .append(task.getDescription())
                    .append("|")
                    .append(task.isDone() ? "y" : "n")
                    .append(System.lineSeparator());
        } else if (taskClass.equals(Deadline.class)) {
            result.append("D|")
                    .append(task.getDescription())
                    .append("|")
                    .append(((Deadline) task).getBy())
                    .append("|")
                    .append(task.isDone() ? "y" : "n")
                    .append(System.lineSeparator());

        } else if (taskClass.equals(Event.class)) {
            result.append("E|")
                    .append(task.getDescription())
                    .append("|")
                    .append(((Event) task).getFrom())
                    .append("|")
                    .append(((Event) task).getTo())
                    .append("|")
                    .append(task.isDone() ? "y" : "n")
                    .append(System.lineSeparator());

        } else {
            System.out.println("invalid task");
        }
    }

    /**
     * Builds and returns a <code>StringBuilder</code> with the formatted entries from the <code>Task</code> list.
     */
    private String listToFile() {
        StringBuilder result = new StringBuilder();
        for (Task task : taskList) {
            appendTask(result, task);
        }
        return result.toString();
    }

    /**
     * Writes the contents of the <code>Task</code> list to <code>dataFile</code>.
     */
    public void writeListToFile() {
        FileWriter fw = null;
        try {
            checkDataFile();
            fw = new FileWriter(dataFile);
            fw.write(listToFile());
        } catch (IOException e) {
            Ui.sayRedln("Cannot write to file: " + e.getMessage());
        } catch (SecurityException e) {
            Ui.sayRedln("Other error writing to file: " + e.getMessage());
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    Ui.sayRedln("Cannot close file: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Reads the contents of the <code>dataFile</code> if it exists and adds them to the <code>Task</code> list in
     * the same order.
     */
    private void readList() {
        if (dataFile.exists()) {
            try {
                Scanner s = new Scanner(dataFile);
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    if (line.equals(System.lineSeparator())) {
                        break;
                    }
                    String[] taskParams = line.split("\\|");
                    char taskType = taskParams[0].charAt(0);
                    String description = taskParams[1];
                    boolean isDone;
                    switch (taskType) {
                    case 'T':
                        isDone = taskParams[2].equals("y");
                        taskList.add(new Todo(description, isDone));
                        break;
                    case 'D':
                        String by = taskParams[2];
                        isDone = taskParams[3].equals("y");
                        taskList.add(new Deadline(description, by, isDone));
                        break;
                    case 'E':
                        String from = taskParams[2];
                        String to = taskParams[3];
                        isDone = taskParams[4].equals("y");
                        taskList.add(new Event(description, from, to, isDone));
                        break;
                    default:
                        System.out.println("Invalid task type in data file");
                        break;
                    }
                }
                Ui.sayRedln("Previous list data restored!");
            } catch (FileNotFoundException e) {
                Ui.sayRedln("File not found: " + e.getMessage());
            }
        }
    }
}
