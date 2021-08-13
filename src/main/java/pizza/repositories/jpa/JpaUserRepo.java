package pizza.repositories.jpa;

import org.springframework.data.repository.CrudRepository;
import pizza.model.User;

public interface JpaUserRepo extends CrudRepository<User, Long> {

    User findById(long id);

    User findByUsername(String username);
}
