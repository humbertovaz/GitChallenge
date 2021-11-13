package com.github.humbertovaz.gitChallenge.services;


import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import com.github.humbertovaz.gitChallenge.utils.PagingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;


@ConfigurationProperties("remotecommitloader")
public class RemoteCommitLoader {
    private static final Logger logger = LogManager.getLogger();
    String url;
    int timeout;
    private List<CommitDTO> commits;
    TimeUnit time = TimeUnit.MILLISECONDS;

    /**
     * This getter is responsible to expose the externalized endpoint.url string on application.properties
     */
    public String getUrl() {
        return url;
    }

    /**
     * This setter is responsible to set the externalized remotecommitloader.url string on application.properties
     * @param url is a String that is going to be used on external API calls
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * This getter is responsible to expose the externalized remotecommitloader.timeout integer on application.properties
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * This setter is responsible to set the externalized remotecommitloader.timeout string on application.properties
     * @param timeout is a String that is going to be used on external API calls
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * This method is responsible to create a GET request, send it and retrieve a StringBuffer with the result
     */
    private StringBuffer sendGET() throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout((int) time.toMinutes(timeout));
        int responseCode = con.getResponseCode();
        logger.info("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response;
        } else {
            logger.error("GET request not worked");
            return null;
        }

    }

    /**
     * This method is responsible for getting commits from remote repository
     */
    List<CommitDTO> getCommitsFromRepository() throws IOException, JSONException {
        StringBuffer sb = sendGET();
        return RemoteCommitParser.jsonToCommitDTO(sb.toString());
    }

    /**
     * This method is responsible for processing commits from remote repository based on size, and page number
     * @param size - size of the page
     * @param page - page number
     * @param paging - Pageable object
     *
     */
    public Page<CommitDTO> processCommits(int size, int page, Pageable paging) throws IOException, JSONException {
        this.commits = getCommitsFromRepository();
        List<CommitDTO> pageList = PagingUtils.getPage(commits, page, size);
        return PagingUtils.listToPage(pageList, 0, pageList.size(), paging, commits.size());
    }

}