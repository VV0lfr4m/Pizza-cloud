package pizza.api;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import pizza.controllers.DesignPizzaController;
import pizza.model.Ingredient;
import pizza.model.util.IngredientModel;

public class IngredientModelAssembler extends RepresentationModelAssemblerSupport<Ingredient, IngredientModel> {

    public IngredientModelAssembler() {
        super(DesignPizzaController.class, IngredientModel.class);
    }

    @Override
    protected IngredientModel instantiateModel(Ingredient entity) {
        return new IngredientModel(entity);
    }

    @Override
    public IngredientModel toModel(Ingredient entity) {
        IngredientModel ingredientModel = new IngredientModel(entity);
        Link link = WebMvcLinkBuilder
                .linkTo(DesignPizzaController.class)
                .slash("recent")
                .slash("ingredient")
                .slash(entity.getId())
                .withSelfRel();//or may be withRel(some rel name)

        ingredientModel.add(link);

        return ingredientModel;
    }
}
