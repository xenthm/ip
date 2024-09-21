import types.Deadline;
import types.Event;
import types.Task;
import types.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Storage {
    TaskList taskList;
    Path dataPath;
    File dataFile;

    public Storage(TaskList tasklist, Path dataPath) {
        this.taskList = tasklist;
        this.dataPath = dataPath;
        dataFile = dataPath.toFile();
        readList();
    }

    public void checkDataFile() throws IOException, SecurityException {
        // Checks if data directory exists
        if (dataFile.getParentFile().mkdirs()) {
            Ui.sayRedln("Directory \"" + dataFile.getParentFile() + "\" created.");
        }

        // Checks if file exists
        if (dataFile.createNewFile()) {
            Ui.sayRedln("File \"" + dataFile.getName() + "\" created.");
        }
    }

    public void appendTask(StringBuilder result, Task task) {
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

    public String listToFile() {
        StringBuilder result = new StringBuilder();
        for (Task task : taskList) {
            appendTask(result, task);
        }
        return result.toString();
    }

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

    public void readList() {
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
