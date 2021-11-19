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
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {


    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final SingleRoleExtractor roleExtractor = SingleRoleExtractor.INSTANCE;
    
    enum SingleRoleExtractor {
        INSTANCE;
        private final RoleExtractor instance = new RoleExtractor();;
        public  RoleExtractor getInstance(){
            return instance;
        }

        private class RoleExtractor implements ResultSetExtractor<List<User>> {
            private final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer,User> users =  new LinkedHashMap<>();
                User user = null;
                while (rs.next()) {
                    if (user!=null){
                        int userId = rs.getInt("id");
                        if (user.getId() != userId){
                            user = users.get(userId);
                        }
                    }
                    if (user == null) {
                        user = ROW_MAPPER.mapRow(rs,0);
                        users.put(user.getId(),user);
                    }
                    String roleName = rs.getString("roles");
                    if (roleName!=null){
                        Role role = Role.valueOf(roleName);
                        user.getRoles().add(role);
                    }
                }
                return new ArrayList<>(users.values());
            }
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

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            if (!user.isNew()) {
                jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
            }
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                    user.getRoles(),
                    user.getRoles().size(),
                    (ps, role) -> {
                        ps.setInt(1, user.getId());
                        ps.setString(2, role.name());
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
                """, roleExtractor.getInstance(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("""
                SELECT users.*,roles.role as roles FROM users
                left outer join user_roles roles on users.id=roles.user_id
                WHERE email=?
                """, roleExtractor.getInstance(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                SELECT users.*,roles.role as roles FROM users
                left outer join user_roles roles on users.id=roles.user_id
                ORDER BY name, email
                """, roleExtractor.getInstance());
    }
}
