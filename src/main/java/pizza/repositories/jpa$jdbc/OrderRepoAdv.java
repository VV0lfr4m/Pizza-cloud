package pizza.repositories.jpa$jdbc;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizza.model.Order;
import pizza.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepoAdv extends JpaRepository<Order, Long> {

    Order customSave(Order order);

    Optional<Order> customDeleteById(Long orderId) throws EmptyResultDataAccessException;

    Optional<Order> findById(Long orderId);

    List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
