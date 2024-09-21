import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Pat {
    private final TaskList taskList;
    private final Storage storage;

    public Pat(Path dataPath) {
        taskList = new TaskList();
        storage = new Storage(taskList, dataPath);
    }

    public void runChat() {
        Ui.greet();
        String line;
        Scanner in = new Scanner(System.in);
        do {
            line = Ui.readCommand(in);
            Parser.parseCommand(taskList, line);
        } while (!Parser.saidBye(line));
        storage.writeListToFile();
        Ui.bye();
    }

    public static void main(String[] args) {
        Path dataPath = Paths.get("data", "list.txt");
        new Pat(dataPath).runChat();
    }
}
