import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", OrderStatus.PROCESSING, List.of(new Product("1", "Apfel")));
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        assertNull(actual);
    }

    @Test
    void getAllOrdersWithStatus_whenAdd1Order_expects1WithOrderStatusProcessing() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        Order order = shopService.addOrder(productsIds);

        //WHEN
        List<Order> actual = shopService.getAllOrdersWithStatus(OrderStatus.PROCESSING);

        //THEN
        assertEquals(1, actual.size());
        assertEquals(order, actual.get(0));
    }

    @Test
    void getAllOrdersWithStatus_whenAdd1Order_expectsNoWithOrderStatusCompleted() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        Order order = shopService.addOrder(productsIds);

        //WHEN
        List<Order> actual = shopService.getAllOrdersWithStatus(OrderStatus.COMPLETED);

        //THEN
        assertEquals(0, actual.size());
    }
}
