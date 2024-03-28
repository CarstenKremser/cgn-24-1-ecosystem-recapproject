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
        Order actual = null;
        try {
            actual = shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e) {
            fail();
        }

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

        //WHEN / THEN
        assertThrows(ProductDoesNotExistException.class,
                ()-> shopService.addOrder(productsIds));
    }

    @Test
    void getAllOrdersWithStatus_whenAdd1Order_expects1WithOrderStatusProcessing() {
        //GIVEN
        ShopService shopService = new ShopService();
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
        ShopService shopService = new ShopService();
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
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        Order order = null;
        try {
            order = shopService.addOrder(productsIds);
        } catch (ProductDoesNotExistException e) {
            fail();
        }

        //WHEN
        Order actual = shopService.updateOrder(order.id(), OrderStatus.COMPLETED);

        //THEN
        assertNotSame(order, actual); // nicht identische Objekte
        assertEquals(order.id(), actual.id());
        assertEquals(OrderStatus.COMPLETED, actual.status());
        assertEquals(order.products(), actual.products());
    }

    @Test
    void updateOrder_whenUpdatesNonexistingOrderWithStatusCompleted_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService();
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
}
