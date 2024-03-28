import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    static IdService testIdService = new IdService();

    @BeforeEach
    void setUpProductRepo() {
        testProducts = new Product[]{
                new Product("1","Product 1"),
                new Product("2","Product 2"),
                new Product("3","Product 3"),
                new Product("4","Product 4"),
                new Product("5","Product 5"),
                new Product("6","Product 6"),
                new Product("7","Product 7"),
                new Product("8","Product 8"),
                new Product("9","Product 9")
        };
        testProductRepo = new ProductRepo();
        for (Product product : testProducts) {
            testProductRepo.addProduct(product);
        }

    }

    Product[] testProducts;
    ProductRepo testProductRepo;

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = null;
        try {
            actual = shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e) {
            fail();
        }

        //THEN
        Order expected = new Order("-1", OrderStatus.PROCESSING, Instant.now(), List.of(new Product("1", "Apfel")));
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        List<String> productsIds = List.of("1", "2");

        //WHEN / THEN
        assertThrows(ProductDoesNotExistException.class,
                ()-> shopService.addOrder(productsIds));
    }

    @Test
    void getAllOrdersWithStatus_whenAdd1Order_expects1WithOrderStatusProcessing() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        List<String> productsIds = List.of("1");
        Order order = null;
        try {
            order = shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e) {
            fail();
        }

        //WHEN
        List<Order> actual = shopService.getAllOrdersWithStatus(OrderStatus.PROCESSING);

        //THEN
        assertEquals(1, actual.size());
        assertEquals(order, actual.get(0));
    }

    @Test
    void getAllOrdersWithStatus_whenAdd1Order_expectsNoWithOrderStatusCompleted() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        List<String> productsIds = List.of("1");
        try {
            shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e) {
            fail();
        }

        //WHEN
        List<Order> actual = shopService.getAllOrdersWithStatus(OrderStatus.COMPLETED);

        //THEN
        assertEquals(0, actual.size());
    }

    @Test
    void updateOrder_whenUpdatesExistingOrderWithStatusCompleted_expectOrderCompleted() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        List<String> productsIds = List.of("1");
        Order order = null;
        try {
            order = shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e) {
            fail();
        }

        //WHEN
        Order newOrder = shopService.updateOrder(order.id(), OrderStatus.COMPLETED);
        List<Order> actualOrders = shopService.getAllOrdersWithStatus(OrderStatus.COMPLETED);

        //THEN
        assertNotNull(actualOrders);
        assertEquals(1, actualOrders.size());
        Order actual = actualOrders.get(0);
        assertNotSame(order, actual); // nicht identische Objekte
        assertEquals(order.id(), actual.id());
        assertEquals(OrderStatus.COMPLETED, actual.status());
        assertEquals(order.products(), actual.products());
    }

    @Test
    void updateOrder_whenUpdatesNonexistingOrderWithStatusCompleted_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new IdService());
        List<String> productsIds = List.of("1");
        try {
            shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e) {
            fail();
        }

        //WHEN
        Order actual = shopService.updateOrder("unknownId", OrderStatus.COMPLETED);

        //THEN
        assertNull(actual);
    }

    @Test
    void getOldestOrderPerStatus() {
        ShopService shopService = new ShopService(testProductRepo, new OrderMapRepo(), testIdService);
        Order[] orders = new Order[9]; // 1..9
        try {
            orders[0] = shopService.addOrder(List.of("1"));
            orders[1] = shopService.addOrder(List.of("2"));
            orders[2] = shopService.addOrder(List.of("3"));
            orders[3] = shopService.addOrder(List.of("4"));
            orders[4] = shopService.addOrder(List.of("5"));
            orders[5] = shopService.addOrder(List.of("6"));
            orders[6] = shopService.addOrder(List.of("7"));
            orders[7] = shopService.addOrder(List.of("8"));
            orders[8] = shopService.addOrder(List.of("9"));
        } catch (ProductDoesNotExistException e) {
            fail();
        }
        Order oldestProcessing = orders[0];
        Order oldestCompleted = shopService.updateOrder(orders[2].id(), OrderStatus.COMPLETED);
        shopService.updateOrder(orders[4].id(), OrderStatus.COMPLETED);
        shopService.updateOrder(orders[5].id(), OrderStatus.COMPLETED);
        shopService.updateOrder(orders[8].id(), OrderStatus.COMPLETED);
        Order oldestInDelivery = shopService.updateOrder(orders[6].id(), OrderStatus.IN_DELIVERY);
        shopService.updateOrder(orders[7].id(), OrderStatus.IN_DELIVERY);

        Map<OrderStatus,Order> actual = shopService.getOldestOrderPerStatus();

        assertEquals(OrderStatus.values().length, actual.size());
        assertEquals(oldestProcessing, actual.get(OrderStatus.PROCESSING));
        assertEquals(oldestCompleted, actual.get(OrderStatus.COMPLETED));
        assertEquals(oldestInDelivery, actual.get(OrderStatus.IN_DELIVERY));
    }
}
