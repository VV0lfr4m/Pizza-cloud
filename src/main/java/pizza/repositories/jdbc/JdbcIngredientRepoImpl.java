package pizza.repositories.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pizza.model.Ingredient;
import pizza.repositories.IngredientRepo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcIngredientRepoImpl implements IngredientRepo {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcIngredientRepoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbc.query(
                "SELECT id, name, type FROM ingredient",
                this::mapRowToIngredient);
    }

    private Ingredient mapRowToIngredient(ResultSet resultSet, int rowNum) throws SQLException {
        return new Ingredient(
                resultSet.getString("id"),
                resultSet.getString("name"),
                Ingredient.Type.valueOf(resultSet.getString("type")));
    }

    @Override
    public Optional<Ingredient> findOne(String id) {
        return Optional.ofNullable(jdbc.queryForObject(
                "SELECT id, name, type FROM ingredient WHERE id=?",
                this::mapRowToIngredient,
                id));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbc.update(
                "INSERT INTO ingredient (id, name, type) VALUES (?,?,?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());

        return ingredient;
    }

}
