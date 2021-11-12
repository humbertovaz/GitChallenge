package com.github.humbertovaz.gitChallenge.utils;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import com.github.humbertovaz.gitChallenge.events.PaginatedResultsRetrievedEvent;
import com.github.humbertovaz.gitChallenge.events.ResourceRequestedEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

public class PagingUtils {

    public static List<CommitDTO> pageContent(PageImpl<CommitDTO> pageCommits, int size, int page,
                                              ResourceRequestedEventPublisher resourceRequestedEventPublisher,
                                              UriComponentsBuilder uriBuilder, HttpServletResponse response){
        int totalPages = pageCommits.getTotalPages();
        if (page > totalPages) {
            throw new RuntimeException("You've asked a page that doesn't exist");
        }
        resourceRequestedEventPublisher.publishCustomEvent(new PaginatedResultsRetrievedEvent<>(
                CommitDTO.class, uriBuilder, response, page, totalPages, size));

        return pageCommits.getContent();
    }

    public static Page<CommitDTO> listToPage(List<CommitDTO> fooList, int start, int end, Pageable pageable, int listSize){
        return new PageImpl<CommitDTO>(fooList.subList(start, end), pageable, listSize);
    }

    private static String PAGE = "page";
    public static String createLinkHeader(String uri, String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    public static String constructCurrentPageUri(UriComponentsBuilder uriBuilder, long page, long size) {
        return uriBuilder.replaceQueryParam(PAGE, page)
                .replaceQueryParam("size", size)
                .build()
                .encode()
                .toUriString();
    }

    public static String constructNextPageUri(UriComponentsBuilder uriBuilder, long page, long size) {
        return uriBuilder.replaceQueryParam(PAGE, page + 1)
                .replaceQueryParam("size", size)
                .build()
                .encode()
                .toUriString();
    }


    public static boolean hasNextPage(int currentPageNumber, int totalPages){
        return currentPageNumber < totalPages;
    }

    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("Invalid page: " + page + " or Invalid page size: " + pageSize + "\n");
        }

        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() <= fromIndex){
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }
}