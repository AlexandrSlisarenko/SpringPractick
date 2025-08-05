package ru.slisarenko.springpractick.integration.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import ru.slisarenko.springpractick.db.entity.Role;
import ru.slisarenko.springpractick.db.entity.User;
import ru.slisarenko.springpractick.db.repositary.UserRepository;
import ru.slisarenko.springpractick.dto.PersonalInfo;
import ru.slisarenko.springpractick.dto.PersonalInfoProjection;
import ru.slisarenko.springpractick.dto.UserFilterByCompanyAndRole;
import ru.slisarenko.springpractick.dto.UserFilterByFIOAndBirthDate;
import ru.slisarenko.springpractick.integration.IntegrationTestBase;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;


    @Test
    void checkUpdate(){
        var ivan = userRepository.getReferenceById(4L);
        assertNull(ivan.getRole());
        var countUpdate = userRepository.updateRole(Role.USER_ROLE, 4L,3L);
        assertEquals(2, countUpdate);
        var samIvan = userRepository.getReferenceById(4L);
        assertSame(Role.USER_ROLE, samIvan.getRole());
    }

    @Test
    void checkFindFirstOrderByDesc(){
        var optionalUser = userRepository.findFirstByOrderByIdDesc();
        assertTrue(optionalUser.isPresent());
        optionalUser.ifPresent(user -> assertEquals(6L, user.getId()));
    }

    @Test
    void checkFindTop3ByOrderByIdDesc(){
        var topUsers = userRepository.findTop3ByOrderByIdDesc();
        assertFalse(topUsers.isEmpty());
        assertThat(topUsers).hasSize(3);
    }

    @Test
    void checkSort(){
        var typeSort = Sort.sort(User.class);
        var sortingParameter = typeSort.by(User::getBirthDate)
                .and(typeSort.by(User::getFirstname))
                .and(typeSort.by(User::getLastname));
        var users = userRepository.findBy(sortingParameter);
        assertFalse(users.isEmpty());
        users.forEach(user -> log.info(user.toString()));
    }

    @Test
    void checkPageable(){
        var typeSort = Sort.sort(User.class);
        var sortingParameter = typeSort.by(User::getId);
        var pageable = PageRequest.of(1,3, sortingParameter);
        var users = userRepository.findAllBy(pageable);
        assertFalse(users.isEmpty());
        assertThat(users).hasSize(3);
        users.forEach(user -> log.info(user.toString()));
    }

    @Test
    void checkSlice(){
        var typeSort = Sort.sort(User.class);
        var sortingParameter = typeSort.by(User::getId);
        var pageable = PageRequest.of(0,2, sortingParameter);
        var sliceUsers = userRepository.findUsersByRoleIsNull(pageable);
        assertThat(sliceUsers).hasSize(2);
        log.info("Slice № " +sliceUsers.getNumber());
        sliceUsers.forEach(user -> log.info(user.toString()));
        while (sliceUsers.hasNext()){
            sliceUsers = userRepository.findUsersByRoleIsNull(sliceUsers.nextPageable());
            log.info("Slice № " +sliceUsers.getNumber());
            sliceUsers.forEach(user -> log.info(user.toString()));

        }
    }

    @Test
    void checkPage(){
        var typeSort = Sort.sort(User.class);
        var sortingParameter = typeSort.by(User::getId);
        var pageable = PageRequest.of(0,2, sortingParameter);
        var pageUsers = userRepository.findUsersBy(pageable);
        assertEquals(3, pageUsers.getTotalPages());
        log.info("Page № " +pageUsers.getNumber());
        pageUsers.forEach(user -> log.info(user.toString()));
        while (pageUsers.hasNext()){
            pageUsers = userRepository.findUsersBy(pageUsers.nextPageable());
            log.info("Page № " +pageUsers.getNumber());
            pageUsers.forEach(user -> log.info(user.toString()));

        }
    }

    @Test
    void checkEntityGraph(){
        var typeSort = Sort.sort(User.class);
        var sortingParameter = typeSort.by(User::getId);
        var pageable = PageRequest.of(0,2, sortingParameter);
        var pageUsers = userRepository.findUsersByUsernameIsNotEmpty(pageable);
        assertEquals(3, pageUsers.getTotalPages());
        log.info("Page № " +pageUsers.getNumber());
        pageUsers.forEach(user -> log.info(user.getCompany().getName()));
        while (pageUsers.hasNext()){
            pageUsers = userRepository.findUsersByUsernameIsNotEmpty(pageUsers.nextPageable());
            log.info("Page № " +pageUsers.getNumber());
            pageUsers.forEach(user -> log.info(user.getCompany().getName()));

        }
    }

    @Test
    void checkProjectionPersonalInfo(){
        var personals = userRepository.findAllByCompanyId(1L);
        assertFalse(personals.isEmpty());
        personals.forEach(personal -> log.info(personal.toString()));
    }

    @Test
    void checkProjectionPersonalInfoGenetic(){
        var personals = userRepository.findAllByCompanyIdAndRoleIsNull(1L, PersonalInfo.class);
        assertFalse(personals.isEmpty());
        personals.forEach(personal -> log.info(personal.toString()));
    }

    @Test
    void checkFindUsersByCompanyId(){
        var personals = userRepository.findUsersByCompanyId(1L);
        assertFalse(personals.isEmpty());
        personals.forEach(personal -> log.info(((PersonalInfoProjection)personal).getFullName()));
    }

    @Test
    void checkUserFilterRepository(){
        var filter = new UserFilterByFIOAndBirthDate(" ", "ов", LocalDate.now());

        var users = userRepository. filterUserSearch(filter);
        assertFalse(users.isEmpty());
        assertThat(users).hasSize(2);
    }


    @Test
    @Commit
    void checkAuditing(){
        var user = userRepository.findById(5L).get();
        log.info(user.getBirthDate().toString());
        user.setBirthDate(user.getBirthDate().plusYears(1L));
        userRepository.flush();
        var userFromDB = userRepository.findById(user.getId()).get();


    }

    @Test
    void checkSearchUsersByCompanyAndRoleThroughJdbc(){
        var filter = new UserFilterByCompanyAndRole("mex", Role.USER_ROLE.name());

        var users = userRepository.searchUsersByCompanyAndRole(filter);
        assertThat(users).hasSize(1);
    }
}