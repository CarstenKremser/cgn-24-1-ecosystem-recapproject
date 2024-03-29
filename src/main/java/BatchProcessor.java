import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BatchProcessor {
    private final CommandLineInterpreter commandLineInterpreter;

    public BatchProcessor(CommandLineInterpreter commandLineInterpreter) {
        this.commandLineInterpreter = commandLineInterpreter;
    }

    public void executeFile(String fileName) throws FileNotFoundException {
        System.out.println("BatchProcessor.executeFile(" + fileName + ")");
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            commandLineInterpreter.executeLine(line);
        }
        scanner.close();
    }
}