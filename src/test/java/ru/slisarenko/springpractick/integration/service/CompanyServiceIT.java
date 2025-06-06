package ru.slisarenko.springpractick.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import ru.slisarenko.springpractick.service.CompanyService;
import ru.slisarenko.springpractick.dto.CompanyReadDto;
import ru.slisarenko.springpractick.integration.annotation.MyIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MyIntegrationTest
@RequiredArgsConstructor
public class CompanyServiceIT {

    private final static Long COMPANY_ID = 1L;
    private final CompanyService companyService;

    @Test
    public void test() {
        var actualResult = companyService.findById(COMPANY_ID);
        var expectedResult = new CompanyReadDto(COMPANY_ID);

        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }
}
