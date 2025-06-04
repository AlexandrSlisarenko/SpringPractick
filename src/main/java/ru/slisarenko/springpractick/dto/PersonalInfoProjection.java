package ru.slisarenko.springpractick.dto;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface PersonalInfoProjection {

    String getFirstName();

    String getLastName();

    LocalDate getBirthDate();

    @Value("#{target.firstname + ' ' + target.lastname}")
    String getFullName();
}
