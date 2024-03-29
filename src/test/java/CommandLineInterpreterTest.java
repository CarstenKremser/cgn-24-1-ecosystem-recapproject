import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineInterpreterTest {
    static IdService testIdService = new IdService();
    Product[] testProducts;
    OrderRepo testOrderRepo;
    ShopService testShopService;

    @BeforeEach
    void setUpShopService() {
        ProductRepo productRepo;
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
        productRepo = new ProductRepo();
        for (Product product : testProducts) {
            productRepo.addProduct(product);
        }
        testOrderRepo = new OrderMapRepo();
        testShopService = new ShopService(productRepo, testOrderRepo, testIdService);
    }

    @Test
    void executeLine_expectOneOrder_whenAddOrderWasSent() {
        CommandLineInterpreter cli = new CommandLineInterpreter(testShopService);

        cli.executeLine("addOrder A 1 2 3");
        List<Order> actual = testShopService.getAllOrdersWithStatus(OrderStatus.PROCESSING);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        List<String> actualOrderProductIds = actual.get(0).products().stream().map(product -> product.id()).toList();
        assertTrue(actualOrderProductIds.contains("1"));
        assertTrue(actualOrderProductIds.contains("2"));
        assertTrue(actualOrderProductIds.contains("3"));
    }

    @Test
    void executeLine_expectOneOrderWithStatusInDelivery_whenAddOrderAndSetStatusWasSent() {
        CommandLineInterpreter cli = new CommandLineInterpreter(testShopService);

        cli.executeLine("addOrder A 1 2 3");
        cli.executeLine("setStatus A IN_DELIVERY");
        List<Order> actualProcessing = testShopService.getAllOrdersWithStatus(OrderStatus.PROCESSING);
        List<Order> actual = testShopService.getAllOrdersWithStatus(OrderStatus.IN_DELIVERY);

        assertNotNull(actualProcessing);
        assertEquals(0, actualProcessing.size());
        assertNotNull(actual);
        assertEquals(1, actual.size());
        List<String> actualOrderProductIds = actual.get(0).products().stream().map(product -> product.id()).toList();
        assertTrue(actualOrderProductIds.contains("1"));
        assertTrue(actualOrderProductIds.contains("2"));
        assertTrue(actualOrderProductIds.contains("3"));
    }

}