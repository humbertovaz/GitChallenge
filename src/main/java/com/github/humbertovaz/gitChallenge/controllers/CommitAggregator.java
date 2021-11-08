package com.github.humbertovaz.gitChallenge.controllers;


import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import com.github.humbertovaz.gitChallenge.events.CustomSpringEventPublisher;
import com.github.humbertovaz.gitChallenge.events.PaginatedResultsRetrievedEvent;
import com.github.humbertovaz.gitChallenge.utils.CommitProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
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
    private final CustomSpringEventPublisher customSpringEventPublisher;

    public CommitAggregator(CommitProcessor commitProcessor, CustomSpringEventPublisher customSpringEventPublisher) {
        this.commitProcessor = commitProcessor;
        this.customSpringEventPublisher = customSpringEventPublisher;
    }

    @GetMapping(path="/commits")
    public List<CommitDTO> commits() {
        return this.commitProcessor.processCommits();
    }

    @GetMapping(path="/commitsPaginated",params = { "page", "size" })
    public List<CommitDTO> commitsPaginated(
                                     @RequestParam(defaultValue = "0", name = "page") int page,
                                     @RequestParam(defaultValue = "10", name= "size") int size, UriComponentsBuilder uriBuilder,
                                     HttpServletResponse response) {
        try{
            Pageable paging = PageRequest.of(page, size);
            Page<CommitDTO> pageCommits = commitProcessor.processCommits(size, page, paging);
            if (page > pageCommits.getTotalPages()) {
                throw new RuntimeException("You've asked a page that doesn't exist");
            }
            customSpringEventPublisher.publishCustomEvent(new PaginatedResultsRetrievedEvent<>(
                    CommitDTO.class, uriBuilder, response, page, pageCommits.getTotalPages(), size));

            return pageCommits.getContent();
        }catch (Exception e){
            logger.error(e);
        }

        return null;
    }
}