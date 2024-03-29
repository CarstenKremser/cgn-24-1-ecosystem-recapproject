import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CommandLineInterpreter {
    private final ShopService shopService;
    private final Map<String,String> aliasToOrderId = new HashMap<>();

    public CommandLineInterpreter(ShopService shopService) {
        this.shopService = shopService;
    }

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
        getCommand(splittedLine).ifPresent(shopServiceCommand -> executeCommand(shopServiceCommand, splittedLine));
    }

    private Optional<ShopServiceCommand> getCommand(String[] splittedLine) {
        if(splittedLine.length == 0) { return Optional.empty(); }
        return Arrays.stream(ShopServiceCommand.values())
                .filter(shopServiceCommand -> shopServiceCommand.command.equals(splittedLine[0]))
                .findFirst();
    }

    private void executeCommand(ShopServiceCommand command, String[] splittedLine) {
        System.out.println("executeCommand " + command.command + " - " + Arrays.toString(splittedLine));
        switch (command) {
            case ADD_ORDER -> executeCommandAddOrder(splittedLine);
            case SET_STATUS -> executeCommandSetStatus(splittedLine);
            case PRINT_ORDERS -> executeCommandPrintOrders(splittedLine);
        }
    }

    private void executeCommandAddOrder(String[] splittedLine) {
        String orderAlias = splittedLine[1];
        List<String> productIds = Arrays.stream(splittedLine)
                .skip(2)
                .toList();
        try {
            Order newOrder = shopService.addOrder(productIds);
            aliasToOrderId.put(orderAlias, newOrder.id());
        } catch (ProductDoesNotExistException e) {
            System.out.println("cannot add Order - reason: " + e);
        }
    }

    private void executeCommandSetStatus(String[] splittedLine) {

    }

    private void executeCommandPrintOrders(String[] splittedLine) {

    }
}
