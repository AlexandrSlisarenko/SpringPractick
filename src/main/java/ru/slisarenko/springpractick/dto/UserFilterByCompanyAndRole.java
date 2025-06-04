package ru.slisarenko.springpractick.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserFilterByCompanyAndRole implements UserFilterParams{
        private String companyName;
        private String role;
}

