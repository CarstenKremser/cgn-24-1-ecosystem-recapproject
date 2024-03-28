import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class ShopService {
    @NonNull private ProductRepo productRepo;
    @NonNull private OrderRepo orderRepo;
    @NonNull private IdService idService;

    public Order addOrder(List<String> productIds) throws ProductDoesNotExistException {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new ProductDoesNotExistException("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(idService.generateId().toString(), OrderStatus.PROCESSING, Instant.now(), products);

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
        if (orderRepo.getOrderById(id) != null) {
            Order updatedOrder = orderRepo.getOrderById(id).withStatus(newStatus);
            orderRepo.updateOrder(updatedOrder);
            return updatedOrder;
        }
        return null;
    }

    public Map<OrderStatus,Order> getOldestOrderPerStatus() {
        Map<OrderStatus,Order> result = new HashMap<>();
        for (OrderStatus orderStatus : OrderStatus.values()) {
            result.put(orderStatus,
                getAllOrdersWithStatus(orderStatus)
                    .stream()
                    .reduce(null,
                            (previous,current) -> (previous == null
                                    || current.orderTime().isBefore(previous.orderTime())
                                    ? current
                                    : previous)
                    )
            );
        }
        return result;
    }
}
