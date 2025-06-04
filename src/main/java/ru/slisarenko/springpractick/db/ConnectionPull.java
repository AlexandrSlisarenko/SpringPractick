package ru.slisarenko.springpractick.db;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ConnectionPull {
    private Integer size;
    private String userName;
    private List args;
    private Map<String, Object> properties;

    public void print() {
        log.info("Class = {}", this.getClass().getName());
        log.info("size: {}", size);
    }

    @PostConstruct
    private void init() {
        log.info("init connection pull");
    }

    @PreDestroy
    private void destroy() {
        log.info("Clean connection pull");
    }
}
