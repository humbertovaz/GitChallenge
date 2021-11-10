package com.github.humbertovaz.gitChallenge.controllers;


import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import com.github.humbertovaz.gitChallenge.events.ResourceRequestedEventPublisher;
import com.github.humbertovaz.gitChallenge.events.PaginatedResultsRetrievedEvent;
import com.github.humbertovaz.gitChallenge.services.CommitProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
public class CommitAggregator {

    private static final Logger logger = LogManager.getLogger();
    private final CommitProcessor commitProcessor;
    private final ResourceRequestedEventPublisher resourceRequestedEventPublisher;

    public CommitAggregator(CommitProcessor commitProcessor, ResourceRequestedEventPublisher resourceRequestedEventPublisher) {
        this.commitProcessor = commitProcessor;
        this.resourceRequestedEventPublisher = resourceRequestedEventPublisher;
    }


    @GetMapping(path="/commits",params = { "page", "size" })
    public List<CommitDTO> commitsPaginated(
                                     @RequestParam(defaultValue = "1", name = "page") int page,
                                     @RequestParam(defaultValue = "10", name= "size") int size, UriComponentsBuilder uriBuilder,
                                     HttpServletResponse response) {
        try {
            Pageable paging = PageRequest.of(page, size);
            PageImpl<CommitDTO> pageCommits = (PageImpl<CommitDTO>) commitProcessor.processCommits(size, page, paging);
            int totalPages = pageCommits.getTotalPages();
            if (page > totalPages) {
                throw new RuntimeException("You've asked a page that doesn't exist");
            }
            resourceRequestedEventPublisher.publishCustomEvent(new PaginatedResultsRetrievedEvent<>(
                    CommitDTO.class, uriBuilder, response, page, totalPages, size));

            return pageCommits.getContent();
        }catch (Exception e){
            logger.error(e);
        }

        return null;
    }
}