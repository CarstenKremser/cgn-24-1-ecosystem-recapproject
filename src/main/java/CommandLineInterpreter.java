import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CommandLineInterpreter {

    public void executeFile(String fileName) throws FileNotFoundException {
        System.out.println("CommandLineInterpreter.executeFile("+fileName+")");
        Scanner scanner = new Scanner(new File(fileName));
        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            executeLine(line);
        }
        scanner.close();
    }

    private void executeLine(String line) {
        if (line.isEmpty()) { return; }
        System.out.println(line);
    }
}
