package pat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Class for chatbot Pat.
 */
public class Pat {
    private final TaskList taskList;
    private final Storage storage;

    /**
     * Constructor for <code>Pat</code>. Associates <code>TaskList</code> and <code>Storage</code> objects to the Pat
     * instance.
     *
     * @param dataPath Path leading to <code>.txt</code> file where list data is to be stored.
     */
    public Pat(Path dataPath) {
        taskList = new TaskList();
        storage = new Storage(taskList, dataPath);
    }

    /**
     * Starts the chatbot.
     */
    public void runChat() {
        Ui.greet();
        String line;
        Scanner in = new Scanner(System.in);
        do {
            line = Ui.readCommand(in);
            Parser.parseCommand(taskList, storage, line);
        } while (!Parser.saidBye(line));
        storage.writeListToFile();
        Ui.bye();
    }

    public static void main(String[] args) {
        Path dataPath = Paths.get("data", "list.txt");
        new Pat(dataPath).runChat();
    }
}
