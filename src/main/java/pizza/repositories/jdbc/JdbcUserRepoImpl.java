package pizza.repositories.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pizza.model.User;
import pizza.repositories.UserRepo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcUserRepoImpl implements UserRepo {

    JdbcTemplate jdbcTemplate;
    SimpleJdbcInsert userInserter;
    SimpleJdbcInsert userAuthInserter;

    public JdbcUserRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        userInserter =
                new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("user")
                .usingColumns("username", "password", "enabled")
                .usingGeneratedKeyColumns("id");
        userAuthInserter =
                new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("user_authority")
                .usingColumns("id", "username", "authority");
    }

    @Override
    public User save(User user) {
        long userId = saveUserInfo(user);
        user.setId(userId);

        boolean isUserSaved = saveUserAuthority(user);

        if (isUserSaved) {
            return user;
        }
        else {
            throw new NullPointerException("couldn't save user= " + user.toString());
        }
    }

    private boolean saveUserAuthority(User user) {

        Map<String, Object> values = new HashMap<>();
        values.put("id", user.getId());
        values.put("username", user.getUsername());
        values.put("authority", user.getAuthority());

        return userAuthInserter.execute(values) > 0;
    }

    private Long saveUserInfo(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("enabled", user.isEnabled());

        long userId = userInserter.executeAndReturnKey(values).longValue();

        return userId;
    }

    @Override
    public boolean delete(long id) {
        boolean userInfoRes =
                jdbcTemplate.update("DELETE FROM user_info WHERE id=?", id) > 0;

        boolean userAuthRes =
                jdbcTemplate.update("DELETE FROM user_authority WHERE id=?", id) > 0;

        return userInfoRes && userAuthRes;
    }

    @Override
    public User findById(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, username, password, enabled FROM user_info WHERE id=?",
                this::mapRowInfoToUser, id);
    }

    @Override
    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT id, username, password, enabled FROM user_info WHERE username=?",
                this::mapRowInfoToUser, username);
    }

    private User mapRowInfoToUser(ResultSet resultSet, int rowNum) throws SQLException {
        long userId = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String fullName = resultSet.getString("full_name");
        String street = resultSet.getString("street");
        String city = resultSet.getString("city");
        String state = resultSet.getString("state");
        String zip = resultSet.getString("zip");
        String phoneNumber = resultSet.getString("phone_number");
        String authority =
                jdbcTemplate.queryForObject(
                        "SELECT authority FROM user_authority WHERE id=? OR username=?",
                        this::mapRowUserAuthToString,
                        userId);
        boolean enabled = resultSet.getBoolean("enabled");

        User user= new User(
                username, password, fullName,
                street, city, state, zip,
                phoneNumber);

        user.setEnabled(enabled);
        user.setAuthority(authority);
        user.setId(userId);

        return user;
    }
    private String mapRowUserAuthToString(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getString("authority");
    }
}
