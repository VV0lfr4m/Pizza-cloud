package pizza.repositories;

import pizza.model.User;

public interface UserRepo {
    User save (User user);

    boolean delete(long id);

    User findById(long id);

    User findByUsername(String username);
}
