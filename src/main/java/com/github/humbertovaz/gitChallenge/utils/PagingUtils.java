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
    private static String PAGE = "page";

    /**
     * This method has the logic regarding page content
     * @param pageCommits - size of the page
     * @param size - size of the page
     * @param page - desired page
     * @param resourceRequestedEventPublisher -ResourceRequestedEventPublisher object
     * @param uriBuilder -UriComponentsBuilder object
     * @param response - HttpServletResponse response object
     */
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

    /**
     * This method has the logic regarding page content
     * @param commitsList - list of CommitDTOs
     * @param start - start of paging
     * @param end - end of paging
     * @param pageable - Pageable object
     * @param listSize - size of desired list
     */
    public static Page<CommitDTO> listToPage(List<CommitDTO> commitsList, int start, int end, Pageable pageable, int listSize){
        return new PageImpl<CommitDTO>(commitsList.subList(start, end), pageable, listSize);
    }

    /**
     * This method has creates link header to be attached on http response
     * @param uri - Unified Resource Identifier
     * @param rel - relation of pages
     */
    public static String createLinkHeader(String uri, String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    /**
     * This method has creates location header
     * @param uriBuilder - Unified Resource Identifier
     * @param page - page number
     * @param size - size of page
     */
    public static String constructCurrentPageUri(UriComponentsBuilder uriBuilder, long page, long size) {
        return uriBuilder.replaceQueryParam(PAGE, page)
                .replaceQueryParam("size", size)
                .build()
                .encode()
                .toUriString();
    }
    /**
     * This method has creates next header
     * @param uriBuilder - Unified Resource Identifier
     * @param page - page number
     * @param size - size of page
     */
    public static String constructNextPageUri(UriComponentsBuilder uriBuilder, long page, long size) {
        return uriBuilder.replaceQueryParam(PAGE, page + 1)
                .replaceQueryParam("size", size)
                .build()
                .encode()
                .toUriString();
    }

    /**
     * This method verifies if the currente page number is less that the total number of pages
     * @param currentPageNumber - Unified Resource Identifier
     * @param totalPages - page number
     */
    public static boolean hasNextPage(int currentPageNumber, int totalPages){
        return currentPageNumber < totalPages;
    }

    /**
     * This method vreturns a sublist given a source list by page number and page size
     * @param sourceList - Unified Resource Identifier
     * @param page - page number
     * @param pageSize - page size
     */
    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("Invalid page: " + page + " or Invalid page size: " + pageSize + "\n");
        }

        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() <= fromIndex){
            return Collections.emptyList();
        }

        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }
}