package pizza.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import pizza.model.Order;
import pizza.model.User;

@Service
@Data
public class OrderService {

    private User user;

    public Order getDeliveryOrder() {
        Order order = new Order();

        if (user != null) {
            order.setDeliveryName(user.getFullName());
            order.setDeliveryState(user.getState());
            order.setDeliveryCity(user.getCity());
            order.setDeliveryZip(user.getZip());
            order.setDeliveryStreet(user.getStreet());
            order.setCcNumber("341591934824265");
            order.setCcExpiration("11/22");
            order.setCcCVV("123");
        }

        return order;
    }
}
