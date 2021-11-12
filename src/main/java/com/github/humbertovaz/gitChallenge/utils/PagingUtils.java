package com.github.humbertovaz.gitChallenge.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

public class PagingUtils {

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