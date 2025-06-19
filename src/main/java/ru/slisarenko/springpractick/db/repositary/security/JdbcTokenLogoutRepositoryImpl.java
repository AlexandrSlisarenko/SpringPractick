package ru.slisarenko.springpractick.db.repositary.security;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
public class JdbcTokenLogoutRepositoryImpl implements JdbcTokenLogoutRepository {
    private static final String INSERT_TIME_DEACTIVATED_TOKEN = """
            INSERT INTO data_jpa.deactivated_token(id, c_keep_until) VALUES (?, ?);
            """;

    private static final String SELECT_DEACTIVATED_TOKEN_ID = """
            SELECT EXISTS (SELECT token.id FROM data_jpa.deactivated_token token WHERE token.id = ?);
            """;

    private static final String SELECT_USER_PASSWORD = """
            SELECT password
            FROM data_jpa.passwords pass
            WHERE pass.user_id = (
                SELECT user_id
                FROM data_jpa.users us
                WHERE us.username = ?
            );
            """;

    private final JdbcTemplate jdbcTemplate;



    @Override
    public Boolean isDeactivatedToken(UUID id) {
        return jdbcTemplate.queryForObject(SELECT_DEACTIVATED_TOKEN_ID, Boolean.class, id);
    }

    @Override
    public void insertDeactivatedToken(UUID id, Date c_keep_until) {
        jdbcTemplate.update(INSERT_TIME_DEACTIVATED_TOKEN, id, c_keep_until);
    }

    @Override
    public String getPassword(String username) {
        return  jdbcTemplate.queryForObject(SELECT_USER_PASSWORD, String.class, username);
    }
}
