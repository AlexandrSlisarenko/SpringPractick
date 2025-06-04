package ru.slisarenko.springpractick.config.sequrity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class CustomConfigurer extends AbstractHttpConfigurer<CustomConfigurer, HttpSecurity> {

    

    @Override
    public void init(HttpSecurity http) throws Exception {
        // initialization code
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
    }
}
