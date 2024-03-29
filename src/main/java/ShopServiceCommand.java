public enum ShopServiceCommand {
    ADD_ORDER("addOrder"),
    SET_STATUS("setStatus"),
    PRINT_ORDERS("printOrders");

    public final String command;

    ShopServiceCommand(String command) {
        this.command = command;
    }
}
