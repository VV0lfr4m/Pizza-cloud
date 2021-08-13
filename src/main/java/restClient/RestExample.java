package restClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.web.client.RestTemplate;
import pizza.model.Ingredient;

import java.net.URI;
import java.util.List;

@Slf4j
@SpringBootConfiguration
@SpringBootApplication
public class RestExample {

    public static void main(String[] args) {
        SpringApplication.run(RestExample.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Traverson traverson() {
        Traverson traverson = new Traverson(
                URI.create("http://localhost:9090"),
                MediaTypes.HAL_JSON);
        return traverson;
    }

    @Bean
    public CommandLineRunner fetchIngredients(PizzaClient pizzaClient) {
        return args -> {
            log.info("----------------------- GET -------------------------");
            log.info("GETTING INGREDIENT BY IDE");
            log.info("Ingredient:  " + pizzaClient.getIngredientById("YD"));
            log.info("GETTING ALL INGREDIENTS");
            List<Ingredient> ingredients = pizzaClient.getAllIngredients();
            log.info("All ingredients:");
            for (Ingredient ingredient : ingredients) {
                log.info("   - " + ingredient);
            }
        };
    }

    @Bean
    public CommandLineRunner traversonGetIngredients(PizzaClient pizzaClient) {
        return args -> {
            Iterable<Ingredient> ingredients = pizzaClient.getAllIngredientsWithTraverson();
            log.info("----------------------- GET INGREDIENTS WITH TRAVERSON -------------------------");
            for (Ingredient ingredient : ingredients) {
                log.info("   -  " + ingredient);
            }
        };
    }
}
