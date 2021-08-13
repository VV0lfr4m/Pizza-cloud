package pizza.repositories;

import pizza.model.Order;

import java.util.List;

public interface OrderRepo {

    Order save(Order order);

    List<Order> getByUserId(long id);

    Order delete(long id);
}
