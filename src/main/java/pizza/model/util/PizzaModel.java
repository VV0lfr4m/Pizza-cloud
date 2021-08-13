package pizza.model.util;

import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import pizza.api.IngredientModelAssembler;
import pizza.model.Pizza;

import java.util.Date;

@Getter
@Relation(collectionRelation = "pizzas", itemRelation = "pizza")
public class PizzaModel extends RepresentationModel<PizzaModel> {

    private static final IngredientModelAssembler
            ingredientAssembler = new IngredientModelAssembler();

    public PizzaModel(Pizza pizza) {
        this.name = pizza.getName();
        this.createdAt = pizza.getCreatedAt();
        this.ingredients =
                ingredientAssembler.toCollectionModel(pizza.getIngredients());
    }

    private String name;

    private Date createdAt;

    private CollectionModel<IngredientModel> ingredients;

}
