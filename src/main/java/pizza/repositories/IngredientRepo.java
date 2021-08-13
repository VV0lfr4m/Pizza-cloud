package pizza.repositories;

import pizza.model.Ingredient;

import java.util.Optional;

public interface IngredientRepo {

    Iterable<Ingredient> findAll ();
    Optional<Ingredient> findOne (String id);
    Ingredient save (Ingredient ingredient);
}
