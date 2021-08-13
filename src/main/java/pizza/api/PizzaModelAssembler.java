package pizza.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import pizza.controllers.DesignPizzaController;
import pizza.model.Pizza;
import pizza.model.util.PizzaModel;

public class PizzaModelAssembler extends RepresentationModelAssemblerSupport<Pizza, PizzaModel> {

    public PizzaModelAssembler() {
        //1st param - determine the base path for any URLs in links it creates when creating a PizzaModel
        super(DesignPizzaController.class, PizzaModel.class);
    }

    //creates an exemplar of PizzaModel from given Pizza
    @Override
    protected PizzaModel instantiateModel(Pizza entity) {
        return new PizzaModel(entity);
    }

    //create model object from object and give it a self link with the URL being derived from an object's id property
    //under the covers, this method calls instantiateModel() method
    @Override
    public PizzaModel toModel(Pizza entity) {
        return createModelWithId(entity.getId(), entity);
    }

}
