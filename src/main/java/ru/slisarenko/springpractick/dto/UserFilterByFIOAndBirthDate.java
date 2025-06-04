package ru.slisarenko.springpractick.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserFilterByFIOAndBirthDate implements UserFilterParams{
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
}

