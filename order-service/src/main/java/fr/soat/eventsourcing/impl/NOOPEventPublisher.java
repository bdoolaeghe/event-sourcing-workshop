package fr.soat.eventsourcing.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class NOOPEventPublisher implements ApplicationEventPublisher {
    @Override
    public void publishEvent(Object o) {
        log.info("publishing " + o);
    }
}
