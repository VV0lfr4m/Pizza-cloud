package pizza.repositories.jpa;

import org.springframework.data.repository.CrudRepository;
import pizza.model.Ingredient;

public interface JpaIngredientRepo extends CrudRepository<Ingredient, String> {
}
