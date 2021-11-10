package com.github.humbertovaz.gitChallenge.events;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

public class ResourceRequestedEvent extends ApplicationEvent {
    private PaginatedResultsRetrievedEvent<CommitDTO> paginatedResultsRetrievedEvent;
    // TODO: Review these fields and its associated methods
    private HttpServletResponse response;
    private long idOfCurrentResource;

    public ResourceRequestedEvent(Object source, PaginatedResultsRetrievedEvent<CommitDTO> paginatedResultsRetrievedEvent) {
        super(source);
        this.paginatedResultsRetrievedEvent = paginatedResultsRetrievedEvent;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
    public long getIdOfCurrentResource() {
        return idOfCurrentResource;
    }

    public PaginatedResultsRetrievedEvent<CommitDTO> getPaginatedResultsRetrievedEvent() {
        return paginatedResultsRetrievedEvent;
    }
}
