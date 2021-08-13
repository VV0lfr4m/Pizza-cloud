package pizza.repositories.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pizza.model.Ingredient;
import pizza.model.Pizza;
import pizza.repositories.PizzaRepo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPizzaRepoImpl implements PizzaRepo {

    private JdbcTemplate jdbc;

    @Autowired
    private JdbcIngredientRepoImpl ingredientRepo;

    @Autowired
    public JdbcPizzaRepoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Pizza save(Pizza design) {
        long pizzaId = savePizzaInfo(design);
        design.setId(pizzaId);
        design.getIngredients().forEach(i -> saveIngredientToPizza(i, pizzaId));

        return design;
    }

    @Override
    public Optional<Pizza> findById(Long id) {
        return Optional.ofNullable(jdbc.queryForObject("SELECT id, name, created_at FROM pizza WHERE id=?",
                this::mapRowToPizza,
                id));
    }

    @Override
    public Page<Pizza> findAll(Pageable pageable) {
        String rowCountSql = "select count(*) from pizza";
        int count = jdbc.queryForObject(
                rowCountSql, Integer.class);

        String query = "SELECT * FROM pizza " +
                "LIMIT " + pageable.getPageSize() + " " +
                "OFFSET " + pageable.getOffset();

        List<Pizza> pizzas = jdbc.query(query, this::mapRowToPizza);

        return new PageImpl<>(pizzas, pageable, count);
    }

    private void saveIngredientToPizza(Ingredient ingredient, long pizzaId) {
        jdbc.update(
                "INSERT INTO pizza_ingredients (pizza_id, ingredient) VALUES (?,?)",
                pizzaId, ingredient.getId());
    }

    private long savePizzaInfo(Pizza design) {
        design.setCreatedAt(new Date());

        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(
                "INSERT INTO pizza (name, created_at) VALUES (?,?)",
                Types.VARCHAR, Types.TIMESTAMP
        );
// By default, returnGeneratedKeys = false so change it to true
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
        preparedStatementCreatorFactory.setGeneratedKeysColumnNames("id");

        PreparedStatementCreator psc =
                preparedStatementCreatorFactory.newPreparedStatementCreator(
                        Arrays.asList(
                                design.getName(),
                                new Timestamp(design.getCreatedAt().getTime())));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc,keyHolder);

        return keyHolder.getKey().longValue();
    }

    private List<Ingredient> getPizzaIngredients(Long pizza) {
        return jdbc.query("SELECT ingredient FROM pizza_ingredients WHERE pizza_id=?",
                this::mapRowToPizzaIngredient,
                pizza);
    }

    private Pizza mapRowToPizza(ResultSet resultSet, int rowNum) throws SQLException {

        Pizza pizza = new Pizza();
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        Date date = resultSet.getDate("created_at");
        List<Ingredient> ingredients = getPizzaIngredients(id);

        pizza.setId(id);
        pizza.setName(name);
        pizza.setCreatedAt(date);
        pizza.setIngredients(ingredients);

        return pizza;
    }
    private Ingredient mapRowToPizzaIngredient(ResultSet resultSet, int rowNum) throws SQLException {
        return ingredientRepo.findOne(resultSet.getString("ingredient")).get();
    }

}
