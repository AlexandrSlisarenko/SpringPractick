package ru.slisarenko.springpractick.integration.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import ru.slisarenko.springpractick.db.entity.Company;
import ru.slisarenko.springpractick.db.repositary.CompanyRepository;
import ru.slisarenko.springpractick.integration.annotation.MyIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@MyIntegrationTest
@RequiredArgsConstructor
class CompanyRepositoryIT {

    private static final Long APPLE_ID = 6L;

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;
    private final CompanyRepository companyRepository;

    @Test
    void init() {
    }

    @Test
    void findById() {
        var companys = entityManager.createQuery("FROM Company", Company.class).getResultList();
        assertNotNull(companys);
        assertThat(companys).hasSize(5);
        assertThat(companys.get(1).getLocales()).hasSize(1);
        assertTrue(companys.stream().filter(company -> company.getId() == 11L).findFirst().get().getName().equals("Cinimex"));
    }

    @Test
    void deleteById() {
        var maybeCompany = companyRepository.findById(APPLE_ID);
        assertTrue(maybeCompany.isPresent());
        maybeCompany.ifPresent(companyRepository::delete);
        entityManager.flush();
        assertTrue(companyRepository.findById(APPLE_ID).isEmpty());
    }

    @Test
    void checkFindByQueries(){
        var maybeCinimexCompany = companyRepository.findByName("cinimex");
        var companys = companyRepository.findAllByNameContainingIgnoreCase("L");
    }
}