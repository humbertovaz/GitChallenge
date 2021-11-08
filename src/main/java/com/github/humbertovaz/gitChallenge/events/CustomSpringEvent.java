package com.github.humbertovaz.gitChallenge.events;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.springframework.context.ApplicationEvent;

public class CustomSpringEvent extends ApplicationEvent {
    private PaginatedResultsRetrievedEvent<CommitDTO> message;

    public CustomSpringEvent(Object source, PaginatedResultsRetrievedEvent<CommitDTO> message) {
        super(source);
        this.message = message;
    }
    public PaginatedResultsRetrievedEvent<CommitDTO> getMessage() {
        return message;
    }
}
