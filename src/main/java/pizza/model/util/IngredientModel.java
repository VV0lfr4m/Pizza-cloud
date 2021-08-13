package pizza.model.util;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import pizza.model.Ingredient;

@Getter
@Relation(collectionRelation = "ingredients", itemRelation = "ingredient")
public class IngredientModel extends RepresentationModel<IngredientModel> {

    public IngredientModel(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }

    private String id;

    private String name;

    private Ingredient.Type type;

    public Ingredient toIngredient() {
        return new Ingredient(id, name, type);
    }
}
