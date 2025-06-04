package ru.slisarenko.springpractick.listner.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EntityListner {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @EventListener
    public void acceptEvent(EntityEvent event) {
        log.info("Entity: " + event.getSource().getClass().getName() + " " + event.getAccessType().name());
    }
}
