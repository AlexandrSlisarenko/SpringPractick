package ru.slisarenko.springpractick.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.slisarenko.springpractick.SpringPractice;

import java.util.Optional;

@EnableJpaAuditing
@EnableEnversRepositories(basePackageClasses = SpringPractice.class)
@Configuration
public class AuditConfiguration {

    @Bean
    AuditorAware<String> auditorAware() {
        return () -> Optional.of("aslisarenko");
    }
}
