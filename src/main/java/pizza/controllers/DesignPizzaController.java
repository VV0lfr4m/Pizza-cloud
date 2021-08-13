package pizza.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pizza.api.CollectionModelResponseEntity;
import pizza.api.PizzaModelAssembler;
import pizza.model.Ingredient;

import java.util.*;
import java.util.stream.Collectors;

import pizza.model.Ingredient.*;
import pizza.model.Order;
import pizza.model.Pizza;
import pizza.model.User;
import pizza.model.util.PizzaModel;
import pizza.repositories.IngredientRepo;
import pizza.repositories.PizzaRepo;
import pizza.repositories.jpa.JpaIngredientRepo;
import pizza.services.OrderService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignPizzaController {

    private final JpaIngredientRepo ingredientRepo;
    private final PizzaRepo pizzaRepo;
    private final OrderService orderService;


    @Autowired
    public DesignPizzaController(JpaIngredientRepo ingredientRepo, PizzaRepo pizzaRepo, OrderService orderService) {
        this.ingredientRepo = ingredientRepo;
        this.pizzaRepo = pizzaRepo;
        this.orderService = orderService;
    }


    @GetMapping
    public String showDesignForm(@AuthenticationPrincipal User user, Model model) {
        log.info("Showing design form");

        model.addAttribute("design", getDesign());
        model.addAttribute("user", user);
        // TODO: 04.08.2021 ERROR IS HERE
        //model.addAttribute("order", getOrder());

        return "design";
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(ingredients::add);

        Type[] types = Ingredient.Type.values();
        for (Type type :
                types) {
            model.addAttribute(type.toString().toLowerCase(), filterIngredientByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "order")
    public Order getOrder() {
        return new Order();
    }

    @ModelAttribute(name = "design")
    public Pizza getDesign() {
        return new Pizza();
    }


    @PostMapping
    public String processDesign(@Valid @ModelAttribute("design") Pizza design, Errors errors,
                                @ModelAttribute Order order) {

        if (errors.hasErrors()) {
            return "design";
        }
        log.info("Processing design: " + design);
        Pizza savedPizza = pizzaRepo.save(design);
        order.addDesign(savedPizza);
        return "redirect:/orders/current";
    }

    //REST API
    @GetMapping(path = "/api/recent")
    @ResponseBody
    public CollectionModelResponseEntity recentPizzas() {
        PageRequest page =
                PageRequest.of(0, 12, Sort.by("createdAt").descending());
        List<Pizza> pizzas = pizzaRepo.findAll(page).getContent();

        CollectionModel<PizzaModel> pizzaModels =
                new PizzaModelAssembler().toCollectionModel(pizzas);

        CollectionModel<PizzaModel> recentModels = CollectionModel.of(pizzaModels);

        recentModels.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                DesignPizzaController.class).recentPizzas()).withRel("recents"));

        return new CollectionModelResponseEntity(recentModels, HttpStatus.OK);
    }


    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Pizza> pizzaById(@PathVariable("id") Long id) {
        Optional<Pizza> optPizza = pizzaRepo.findById(id);
        return optPizza.isPresent()
                ? new ResponseEntity<>(optPizza.get(), HttpStatus.OK)
                : new ResponseEntity<>(optPizza.orElse(new Pizza()), HttpStatus.NOT_FOUND);
    }


    @PostMapping(path = "/api",consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Pizza postPizza(@RequestBody Pizza pizza) {
        return pizzaRepo.save(pizza);
    }



    private List<Ingredient> filterIngredientByType(List<Ingredient> ingredients, Type type) {

        return ingredients.stream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());
    }

    //may be non static
    @Component
    public static class IngredientConverter implements Converter<String, Ingredient> {

        private final IngredientRepo ingredientRepo;

        @Autowired
        public IngredientConverter(IngredientRepo ingredientRepo) {

            this.ingredientRepo = ingredientRepo;
        }

        @Override
        public Ingredient convert(String source) {

            List<Ingredient> ingredients = new ArrayList<>();

            ingredientRepo.findAll().forEach(ingredients::add);

            for (Ingredient ingredient : ingredients) {

                if (ingredient.getId().equals(source))

                    return ingredient;
            }

            return null;
        }

    }
}
