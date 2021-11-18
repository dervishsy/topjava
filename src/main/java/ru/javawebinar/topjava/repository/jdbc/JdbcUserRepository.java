package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private class RoleExtractor implements ResultSetExtractor<List<User>> {

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<User> users = new ArrayList<>();
            User user = null;
            boolean newUser = true;
            while (rs.next()) {
                if ((user != null) && (user.getId() != rs.getInt("id"))) {
                    newUser = true;
                }
                if (newUser) {
                    user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("calories_per_day"),
                            rs.getBoolean("enabled"),
                            rs.getDate("registered"),
                            null
                    );
                    newUser = false;
                    users.add(user);
                }
                String roleName = rs.getString("roles");
                try {
                    Role role = Role.valueOf(roleName);
                    user.getRoles().add(role);
                } catch (IllegalArgumentException | NullPointerException ignored) {}
            }
            return users;
        }
    }

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }

        if ((user.getRoles() != null) && (user.getRoles().size() > 0)) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                    user.getRoles(),
                    user.getRoles().size(),
                    (ps, role) -> {
                        ps.setInt(1, user.getId());
                        ps.setString(2, role.toString());
                    });
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("""
                SELECT users.*,roles.role as roles FROM users
                left outer join user_roles roles on users.id=roles.user_id
                WHERE id=?
                """, new RoleExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("""
                SELECT users.*,roles.role as roles FROM users
                left outer join user_roles roles on users.id=roles.user_id
                WHERE email=?
                """, new RoleExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                SELECT users.*,roles.role as roles FROM users
                left outer join user_roles roles on users.id=roles.user_id
                ORDER BY name, email
                """, new RoleExtractor());
    }
}
