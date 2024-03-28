import java.util.List;

public class Main {

    public static void main(String[] args) {
        OrderRepo orderRepo = new OrderMapRepo();
        ProductRepo productRepo = new ProductRepo();
        ShopService shopService = new ShopService(productRepo,orderRepo);

        Product[] products = new Product[]{
                new Product("a", "Apfel"),
                new Product("b", "Birne"),
                new Product("c", "Chili"),
                new Product("d", "Dattel"),
                new Product("e", "Erdnuss")
        };
        for (Product product:products) {
            productRepo.addProduct(product);
        }
        try {
            shopService.addOrder(List.of("a","b"));
            shopService.addOrder(List.of("b","c"));
            shopService.addOrder(List.of("c","d"));
            shopService.addOrder(List.of("d","e"));
            shopService.addOrder(List.of("a","e","b","d","c"));
        } catch (ProductDoesNotExistException e) {
            System.out.println("Exception: " + e);
        }
        System.out.println(shopService.getAllOrdersWithStatus(OrderStatus.PROCESSING));

    }
}
