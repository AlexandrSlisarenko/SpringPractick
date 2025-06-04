package ru.slisarenko.springpractick.db.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import ru.slisarenko.springpractick.db.entity.Company;
import ru.slisarenko.springpractick.db.repositary.CompanyRepository;
import ru.slisarenko.springpractick.dto.CompanyReadDto;
import ru.slisarenko.springpractick.listner.entity.EntityEvent;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    private final static Long COMPANY_ID = 1L;

    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyService companyService;




    @Test
    void findById() {
        doReturn(Optional.of(new Company(COMPANY_ID, "test", null)))
                .when(companyRepository).findById(COMPANY_ID);

        var actualResult = companyService.findById(COMPANY_ID);
        var expectedResult = new CompanyReadDto(COMPANY_ID);

        assertTrue(actualResult.isPresent());

        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));

        verify(eventPublisher).publishEvent(any(EntityEvent.class));
        verifyNoMoreInteractions(eventPublisher, companyRepository);
    }
}
