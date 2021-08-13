package pizza.repositories.jpa$jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import pizza.model.Order;
import pizza.model.Pizza;

import java.util.*;

@Component
public class OrderRepoAdvImpl {

    @Autowired
    private OrderRepoAdv orderRepoAdv;
    private final SimpleJdbcInsert orderInserter;
    private final SimpleJdbcInsert orderPizzaInserter;

    @Autowired
    public OrderRepoAdvImpl(JdbcTemplate jdbc) {
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("pizza_order")
                .usingColumns(
                        "delivery_name", "delivery_street", "delivery_city",
                        "delivery_state", "delivery_zip", "cc_number",
                        "cc_expiration", "cc_cvv", "placed_at")
                .usingGeneratedKeyColumns("id");

        this.orderPizzaInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("pizza_order_pizzas")
                .usingColumns("pizza_order", "pizza");

    }

    public Order customSave(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderInfo(order);
        order.setId(orderId);

        List<Pizza> pizzas = order.getDesigns();
        pizzas.forEach(o -> savePizzaToOrder(o, orderId));

        return order;
    }

    public Optional<Order> customDeleteById(Long id) {
        Optional<Order> deleted = orderRepoAdv.findById(id);
        orderRepoAdv.deleteById(id);
        return deleted;
    }


    private void savePizzaToOrder(Pizza pizza, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("pizza", pizza.getId());
        values.put("pizza_order", orderId);
        orderPizzaInserter.execute(values);
    }

    private long saveOrderInfo(Order order) {
        MapSqlParameterSource values = new MapSqlParameterSource();

        values.addValue("delivery_name", order.getDeliveryName());
        values.addValue("delivery_street", order.getDeliveryStreet());
        values.addValue("delivery_city", order.getDeliveryCity());
        values.addValue("delivery_state", order.getDeliveryState());
        values.addValue("delivery_zip", order.getDeliveryZip());
        values.addValue("cc_number", order.getCcNumber());
        values.addValue("cc_expiration", order.getCcExpiration());
        values.addValue("cc_cvv", order.getCcCVV());
        values.addValue("placed_at", order.getPlacedAt());

        long orderId = orderInserter.executeAndReturnKey(values).longValue();

        return orderId;
    }
}
