package pizza.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pizza.model.Pizza;

import java.util.Optional;

public interface PizzaRepo {

    Pizza save(Pizza design);

    Optional<Pizza> findById(Long id);

    Page<Pizza> findAll(Pageable pageable);

}
