package com.github.humbertovaz.gitChallenge.events;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import com.github.humbertovaz.gitChallenge.events.util.LinkUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.StringJoiner;


@Component
public class ResourceRequestedEventListener implements ApplicationListener<ResourceRequestedEvent> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void onApplicationEvent(ResourceRequestedEvent resourceRequestedEvent){
        logger.info("Received spring custom event - " + resourceRequestedEvent.getPaginatedResultsRetrievedEvent());
        PaginatedResultsRetrievedEvent paginatedResultsRetrievedEvent = resourceRequestedEvent.getPaginatedResultsRetrievedEvent();

        long currentPage = resourceRequestedEvent.getPaginatedResultsRetrievedEvent().getPage();

        addLinkHeaderOnResourceRequest(paginatedResultsRetrievedEvent, currentPage);
    }
    void addLinkHeaderOnResourceRequest
            (PaginatedResultsRetrievedEvent<CommitDTO> paginatedResultsRetrievedEvent, long currentPage){
        HttpServletResponse response = paginatedResultsRetrievedEvent.getResponse();
        int page = paginatedResultsRetrievedEvent.getPage();
        int size = paginatedResultsRetrievedEvent.getPageSize();
        int totalPages = paginatedResultsRetrievedEvent.getTotalPages();

        StringJoiner linkHeader = new StringJoiner(", ");

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("page", String.valueOf(page));
        headers.add("size", String.valueOf(size));
        UriComponentsBuilder uriComponentsBuilder = paginatedResultsRetrievedEvent.getUriBuilder().path("commits").queryParams(headers);
        if (LinkUtil.hasNextPage(page, totalPages)) {
            String uriForNextPage = LinkUtil.constructNextPageUri(uriComponentsBuilder, page, size);
            linkHeader.add(LinkUtil.createLinkHeader(uriForNextPage, "next"));
            response.setHeader("next",linkHeader.toString());
        }
        response.setHeader("location", LinkUtil.constructCurrentPageUri(uriComponentsBuilder, currentPage, size));
    }
}