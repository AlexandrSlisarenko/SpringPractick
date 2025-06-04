package ru.slisarenko.springpractick;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ru.slisarenko.springpractick.config.JpaConfiguration;
import ru.slisarenko.springpractick.db.ConnectionPull;
import ru.slisarenko.springpractick.db.service.CompanyService;

@Slf4j
@ConfigurationPropertiesScan("ru.slisarenko.springpractick.config")
@SpringBootApplication
public class SpringPractice {

    public static void main(String[] args) {
        var context = SpringApplication.run(SpringPractice.class, args);
        /*log.info("Application started");
        log.info(context.getBean(JpaConfiguration.class).toString());
        log.info(context.getApplicationName() + " поехали!!!");
        var connectionPull = context.getBean(ConnectionPull.class);
        connectionPull.print();

        var companyService = context.getBean(CompanyService.class);
        log.info(companyService.findById(1L).toString());
        companyService.getCompanys().forEach(company -> log.info(company.companyId().toString()));*/
    }
}


