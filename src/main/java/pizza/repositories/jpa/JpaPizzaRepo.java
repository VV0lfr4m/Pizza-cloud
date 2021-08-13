package pizza.repositories.jpa;

import org.springframework.data.repository.CrudRepository;
import pizza.model.Pizza;

public interface JpaPizzaRepo extends CrudRepository<Pizza, Long> {
}
