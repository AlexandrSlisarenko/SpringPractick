package ru.slisarenko.springpractick.db.repositary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.history.RevisionRepository;
import ru.slisarenko.springpractick.db.entity.Role;
import ru.slisarenko.springpractick.db.entity.User;
import ru.slisarenko.springpractick.db.repositary.filter.UserFilterRepository;
import ru.slisarenko.springpractick.db.repositary.security.JdbcUserDetailRepository;
import ru.slisarenko.springpractick.dto.PersonalInfo;
import ru.slisarenko.springpractick.dto.PersonalInfoProjection;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        UserFilterRepository,
        RevisionRepository<User, Long, Integer>{

    @Modifying(clearAutomatically = true)
    @Query("update User u " +
            "set u.role = :role " +
            "where u.id in (:ids)")
    int updateRole(Role role,Long... ids);

    Optional<User> findFirstByOrderByIdDesc();

    List<User> findTop3ByOrderByIdDesc();

    List<User> findBy(Sort role);

    List<User> findAllBy(Pageable pageable);

    Slice<User> findUsersByRoleIsNull(Pageable pageable);

    @Query(value = "select u from User u ",
           countQuery = "select count(distinct u.username) from User u")
    Page<User> findUsersBy(Pageable pageable);

    @EntityGraph(attributePaths = {"company", "company.locales"})
    @Query(value = "select u from User u ",
            countQuery = "select count(distinct u.username) from User u")
    Page<User> findUsersByUsernameIsNotEmpty(Pageable pageable);

    List<PersonalInfo> findAllByCompanyId(Long companyId);
    <T> List<T> findAllByCompanyIdAndRoleIsNull(Long companyId, Class<T> clazz);

    @Query(nativeQuery = true,
           value = "SELECT u.firstname, u.lastname, u.birth_date birthDate " +
                   "FROM data_jpa.users u WHERE u.company_id = :companyId")
    List<PersonalInfoProjection> findUsersByCompanyId(Long companyId);

    Optional<User> findById(Long id);


}
