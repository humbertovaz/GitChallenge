package com.github.humbertovaz.gitChallenge.events;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CustomSpringEventPublisher {
    private static final Logger logger = LogManager.getLogger();
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishCustomEvent(final PaginatedResultsRetrievedEvent<CommitDTO> paginatedResult) {
        logger.info("Publishing custom event. ");
        CustomSpringEvent customSpringEvent = new CustomSpringEvent(this, paginatedResult);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }
}