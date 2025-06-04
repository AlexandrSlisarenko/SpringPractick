package ru.slisarenko.springpractick.db.repositary.security;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;

public class JdbcUserDetailRepositoryImpl extends MappingSqlQuery<UserDetails>
        implements JdbcUserDetailRepository {

    private static final String SQL_SELECT_USER_DATASOURCE = """ 
            SELECT userDB.username, passDB.password, userDB.role
            FROM data_jpa.users userDB
            LEFT OUTER JOIN data_jpa.passwords passDB ON userDB.id = passDB.user_id
            WHERE userDB.username = :username;
            """;


    public JdbcUserDetailRepositoryImpl(DataSource ds) {
        super(ds, SQL_SELECT_USER_DATASOURCE);
        this.declareParameter(new SqlParameter("username", Types.VARCHAR));
        this.compile();
    }

    @Override
    protected UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .authorities(rs.getString("role"))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(this.findObjectByNamedParam(Map.of("username", username)))
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found".formatted(username)));
    }
}
