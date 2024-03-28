import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) throws ProductDoesNotExistException {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new ProductDoesNotExistException("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), OrderStatus.PROCESSING, products);

        return orderRepo.addOrder(newOrder);
    }

    List<Order> getAllOrdersWithStatus(OrderStatus orderStatus) {
        return orderRepo
                .getOrders()
                .stream()
                .filter(order -> order.status().equals(orderStatus))
                .toList();
    }

    public Order updateOrder(String id, OrderStatus newStatus) {
        return (orderRepo.getOrderById(id) != null)
            ? orderRepo.getOrderById(id).withStatus(newStatus)
            : null;
    }
}
