package ru.slisarenko.springpractick.listner.entity;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


public class EntityEvent extends ApplicationEvent {

    @Getter
    private final AccessType accessType;

    public EntityEvent(final Object source, AccessType accessType) {
        super(source);
        this.accessType = accessType;
    }
}
