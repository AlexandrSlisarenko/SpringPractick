package ru.slisarenko.springpractick.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;


@ConfigurationProperties(prefix = "db")
public record JpaConfiguration(String url,
                               String user,
                               String password,
                               Integer port,
                               List<Pool> pools,
                               Map<String, Object> schemes) {

    public static record Pool(Integer size,
                              Integer timeout) {

    }

}

