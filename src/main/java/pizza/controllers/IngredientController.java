package pizza.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pizza.model.Ingredient;
import pizza.repositories.jpa.JpaIngredientRepo;


//REST ingredient controller
@RestController
@RequestMapping(path = "/api/ingredients", produces = "application/hal+json")
public class IngredientController {

    private JpaIngredientRepo ingredientRepo;

    public IngredientController(JpaIngredientRepo ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @GetMapping()
    public Iterable<Ingredient> getAll() {

        return ingredientRepo.findAll();
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable("id") String ingredientId) {

        return ingredientRepo.findById(ingredientId).orElseThrow(IllegalArgumentException::new);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putIngredient(@RequestBody Ingredient ingredient) {
        ingredientRepo.save(ingredient);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable("id") String ingredientId) {
        ingredientRepo.deleteById(ingredientId);
    }

    @PostMapping()
    public HttpStatus createIngredient(@RequestBody Ingredient ingredient) {
        ingredientRepo.save(ingredient);
        return HttpStatus.CREATED;
    }
}
