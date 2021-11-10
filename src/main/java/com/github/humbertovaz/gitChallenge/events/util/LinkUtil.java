package com.github.humbertovaz.gitChallenge.events.util;

import org.springframework.web.util.UriComponentsBuilder;

public class LinkUtil {

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
}