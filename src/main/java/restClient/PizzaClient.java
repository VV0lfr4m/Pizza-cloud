package restClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pizza.model.Ingredient;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

@Slf4j
@Service
public class PizzaClient {

    private RestTemplate restTemplate;

    private Traverson traverson;

    public PizzaClient(RestTemplate restTemplate, Traverson traverson) {
        this.restTemplate = restTemplate;
        this.traverson = traverson;
    }

    public List<Ingredient> getAllIngredients() {

        return restTemplate.exchange(
                "http://localhost:9090/ingredients",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Ingredient>>(){})
                .getBody();

    }
    public Ingredient getIngredientById(String ingredientId) {

        return restTemplate.getForObject(
                "http://localhost:9090/ingredients/{id}",
                Ingredient.class,
                ingredientId);
    }

    public Ingredient getIngredientByIdInMap(String ingredientId) {
        Map<String, String> urlVars = new HashMap<>();
        urlVars.put("id", ingredientId);

        return restTemplate.getForObject(
                "http://localhost:9090/ingredients/{id}",
                Ingredient.class,
                urlVars);
    }

    public Ingredient getIngredientByIdWithUrl(String ingredientId) {
        Map<String, String> urlVars = new HashMap<>();
        urlVars.put("id", ingredientId);

        URI url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:9090/ingredients/{id}")
                .build(urlVars);

        return restTemplate.getForObject(url, Ingredient.class);
    }



    public Ingredient getIngredientByIdFromEntity(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity =
                restTemplate.getForEntity(
                        "http://localhost:9090/ingredients/{id}",
                        Ingredient.class,
                        ingredientId);

        log.info("Fetched time: " +
                responseEntity.getHeaders().getDate());

        return responseEntity.getBody();
    }



    public void updateIngredient(Ingredient ingredient) {
        restTemplate.put(
                "http://localhost:9090/ingredients/{id}",
                ingredient,
                ingredient.getId());
    }

    public void updateIngredientWithResponseBody(ResponseEntity<Ingredient> ingredientEnt) {
        ResponseEntity<Ingredient> response = restTemplate.exchange(
                "http://localhost:9090/ingredients/{id}",
                HttpMethod.PUT,
                ingredientEnt,
                Ingredient.class);

        assert response.getStatusCode() == HttpStatus.NO_CONTENT;

        assertNotNull(response.getBody());
    }

    public void deleteIngredient(String ingredientId) {
        restTemplate.delete(
                "http://localhost:9090/ingredients/{id}",
                ingredientId);
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        HttpStatus httpStatus = restTemplate.postForObject(
                "http://localhost:9090/ingredients",
                ingredient,
                HttpStatus.class);

        log.info(httpStatus.getReasonPhrase());

        return ingredient;
    }

    public URI createIngredientWithLocation(Ingredient ingredient) {
        URI location = restTemplate.postForLocation(
                "http://localhost:9090/ingredients",
                ingredient);

        log.info(location.getPath());

        return location;
    }

    public Ingredient createIngredientWithEntity(Ingredient ingredient) {
        ResponseEntity<Ingredient> entity = restTemplate.postForEntity(
                "http://localhost:9090/ingredients",
                ingredient,
                Ingredient.class);

        log.info("New resource created at " + entity.getHeaders().getLocation());

        return entity.getBody();
    }

    //TRAVERSON for fetching rest with links

    public Iterable<Ingredient> getAllIngredientsWithTraverson() {
        ParameterizedTypeReference<CollectionModel<Ingredient>> ingredientType =
                new ParameterizedTypeReference<CollectionModel<Ingredient>>() {};

        CollectionModel<Ingredient> ingredientRes =
                traverson
                        .follow("ingredients")
                        .toObject(ingredientType);

        Collection<Ingredient> ingredients = ingredientRes.getContent();

        return ingredients;
    }
}
