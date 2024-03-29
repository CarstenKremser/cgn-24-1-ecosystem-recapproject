import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;
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
        String[] splittedLine = line.split(" ");
        getCommand(splittedLine).ifPresent(shopServiceCommand -> {executeCommand(shopServiceCommand, splittedLine);});
    }

    private Optional<ShopServiceCommand> getCommand(String[] splittedLine) {
        if(splittedLine.length == 0) { return Optional.empty(); }
        return Arrays.stream(ShopServiceCommand.values())
                .filter(shopServiceCommand -> shopServiceCommand.command.equals(splittedLine[0]))
                .findFirst();
    }

    private void executeCommand(ShopServiceCommand command, String[] splittedLine) {
        System.out.println("executeCommand " + command.command + " - " + Arrays.toString(splittedLine));

    }
}
