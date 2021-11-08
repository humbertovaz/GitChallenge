package com.github.humbertovaz.gitChallenge.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CustomSpringEventListener implements ApplicationListener<CustomSpringEvent> {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void onApplicationEvent(CustomSpringEvent event) {
        logger.info("Received spring custom event - " + event.getMessage());
    }
}