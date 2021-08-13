package pizza.repositories.jpa;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pizza.model.Order;
import pizza.model.User;

import java.util.Date;
import java.util.List;

public interface JpaOrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByDeliveryZip(String zip);

    List<Order> readOrderByDeliveryZipAndPlacedAtBetween(String zip, Date startDate, Date endDate);

    List<Order> findByDeliveryState(String state);

    List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
