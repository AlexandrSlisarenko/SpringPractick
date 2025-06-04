package ru.slisarenko.springpractick.db.repositary.filter;


import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.slisarenko.springpractick.db.entity.User;
import ru.slisarenko.springpractick.db.querydsl.QPredicates;
import ru.slisarenko.springpractick.dto.PersonalInfo;
import ru.slisarenko.springpractick.dto.UserFilterByCompanyAndRole;
import ru.slisarenko.springpractick.dto.UserFilterByFIOAndBirthDate;
import ru.slisarenko.springpractick.dto.UserFilterParams;


import java.util.Collections;
import java.util.List;

import static ru.slisarenko.springpractick.db.entity.QUser.user;

@RequiredArgsConstructor
public class UserFilterRepositoryImpl implements UserFilterRepository {

    private static final String SELECT_USERS_BY_COMPANY_AND_ROLE = """
                SELECT u.firstname, u.lastname, u.birth_date
                FROM data_jpa.users u 
                LEFT JOIN data_jpa.company c ON c.id = u.company_id
                WHERE c.name LIKE ? AND u.role LIKE ?
            """;

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> filterUserSearch(UserFilterParams filterFIOAndBirthDate) {
        if (filterFIOAndBirthDate.getClass().equals(UserFilterByFIOAndBirthDate.class)) {
            var filter = (UserFilterByFIOAndBirthDate) filterFIOAndBirthDate;

            var predicate = QPredicates.builder()
                    .and(filter.getFirstName(), user.firstname::containsIgnoreCase)
                    .and(filter.getLastName(), user.lastname::containsIgnoreCase)
                    .and(filter.getBirthDate(), user.birthDate::before)
                    .build();

            return new JPAQuery<User>(entityManager)
                    .select(user)
                    .from(user)
                    .where(predicate)
                    .fetch();
        }

        return Collections.emptyList();
    }

    @Override
    public List<PersonalInfo> searchUsersByCompanyAndRole(UserFilterParams filterCompanyAndRole) {
        if (filterCompanyAndRole.getClass().equals(UserFilterByCompanyAndRole.class)) {
            var filtr = (UserFilterByCompanyAndRole) filterCompanyAndRole;
            return jdbcTemplate.query(SELECT_USERS_BY_COMPANY_AND_ROLE,
                    (rs, rowNum) -> new PersonalInfo(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getDate("birth_date").toLocalDate()
                    ), "%" + filtr.getCompanyName() + "%", "%" + filtr.getRole() + "%");
        }
        return Collections.emptyList();
    }
}
