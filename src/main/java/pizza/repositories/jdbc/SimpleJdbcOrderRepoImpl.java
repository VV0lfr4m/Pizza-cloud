package pizza.repositories.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pizza.model.Order;
import pizza.model.Pizza;
import pizza.repositories.OrderRepo;

import java.util.*;

@Repository
public class SimpleJdbcOrderRepoImpl implements OrderRepo {

    private final SimpleJdbcInsert orderInserter;
    private final SimpleJdbcInsert orderPizzaInserter;

    @Autowired
    public SimpleJdbcOrderRepoImpl(JdbcTemplate jdbc) {
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

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderInfo(order);
        order.setId(orderId);

        List<Pizza> pizzas = order.getDesigns();
        pizzas.forEach(o -> savePizzaToOrder(o, orderId));

        return order;
    }

    @Override
    public List<Order> getByUserId(long id) {
        return null;
    }

    @Override
    public Order delete(long id) {
        return null;
    }

    private void savePizzaToOrder(Pizza pizza, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("pizza", pizza.getId());
        values.put("pizza_order", orderId);
        orderPizzaInserter.execute(values);
    }

    private long saveOrderInfo(Order order) {
        MapSqlParameterSource values = new MapSqlParameterSource();
        //first value is a column name in db
        //IN POSTGRESQL SHOULDN'T NAME COLUMNS WITH CAMELCASE
        values.addValue("delivery_name", order.getDeliveryName());
        values.addValue("delivery_street", order.getDeliveryStreet());
        values.addValue("delivery_city", order.getDeliveryCity());
        values.addValue("delivery_state", order.getDeliveryState());
        values.addValue("delivery_zip", order.getDeliveryZip());
        values.addValue("cc_number", order.getCcNumber());
        values.addValue("cc_expiration", order.getCcExpiration());
        values.addValue("cc_cvv", order.getCcCVV());
        //objectMapper would otherwise convert the Date into a long, which is incompatible
        //with the placedAt field in the Pizza_Order table.
        values.addValue("placed_at", order.getPlacedAt());

        long orderId = orderInserter.executeAndReturnKey(values).longValue();

        return orderId;
    }
}
