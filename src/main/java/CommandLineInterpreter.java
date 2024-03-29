import java.util.*;

public class CommandLineInterpreter {
    private final ShopService shopService;
    private final Map<String,String> aliasToOrderId = new HashMap<>();

    public CommandLineInterpreter(ShopService shopService) {
        this.shopService = shopService;
    }

    public void executeLine(String line) {
        if (line.isEmpty()) { return; }
        String[] splitLine = line.split(" ");
        getCommand(splitLine).ifPresent(shopServiceCommand -> executeCommand(shopServiceCommand, splitLine));
    }

    private Optional<ShopServiceCommand> getCommand(String[] splitLine) {
        if(splitLine.length == 0) { return Optional.empty(); }
        return Arrays.stream(ShopServiceCommand.values())
                .filter(shopServiceCommand -> shopServiceCommand.command.equals(splitLine[0]))
                .findFirst();
    }

    private void executeCommand(ShopServiceCommand command, String[] splitLine) {
        System.out.println("executeCommand " + command.command + " - " + Arrays.toString(splitLine));
        switch (command) {
            case ADD_ORDER -> executeCommandAddOrder(splitLine);
            case SET_STATUS -> executeCommandSetStatus(splitLine);
            case PRINT_ORDERS -> executeCommandPrintOrders(splitLine);
        }
    }

    private void executeCommandAddOrder(String[] splitLine) {
        if (splitLine.length < 3) {
            System.out.println("command has too few parameters: " + Arrays.toString(splitLine));
            return;
        }
        String orderAlias = splitLine[1];
        List<String> productIds = Arrays.stream(splitLine)
                .skip(2)
                .toList();
        try {
            Order newOrder = shopService.addOrder(productIds);
            aliasToOrderId.put(orderAlias, newOrder.id());
        } catch (ProductDoesNotExistException e) {
            System.out.println("cannot add Order - reason: " + e);
        }
    }

    private void executeCommandSetStatus(String[] splitLine) {
        if (splitLine.length != 3) {
            System.out.println("command has wrong number of parameters: " + Arrays.toString(splitLine));
            return;
        }
        String orderAlias = splitLine[1];
        String status = splitLine[2];
        OrderStatus newOrderStatus = OrderStatus.valueOf(status);
        Order newOrder = shopService.updateOrder(aliasToOrderId.get(orderAlias),newOrderStatus);
        aliasToOrderId.put(orderAlias, newOrder.id());
    }

    private void executeCommandPrintOrders(String[] splitLine) {
        if (splitLine.length != 1) {
            System.out.println("command has wrong number of parameters: " + Arrays.toString(splitLine));
            return;
        }
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println("Orders with Status " + status);
            System.out.println(shopService.getAllOrdersWithStatus(status));
        }
    }
}
