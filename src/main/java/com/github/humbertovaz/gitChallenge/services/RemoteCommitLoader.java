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


@ConfigurationProperties("remotecommitloader")
public class RemoteCommitLoader {
    private static final Logger logger = LogManager.getLogger();
    String url;
    String timeout;
    private List<CommitDTO> commits;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    private StringBuffer sendGET() throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000); // TODO: Define programmatically number later via properties
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

    List<CommitDTO> getCommitsFromRepository() throws IOException, JSONException {
        StringBuffer sb = sendGET();
        return RemoteCommitParser.jsonToCommitDTO(sb.toString());
    }

    public Page<CommitDTO> processCommits(int size, int page, Pageable paging) throws IOException, JSONException {
        this.commits = getCommitsFromRepository();
        List<CommitDTO> pageList = PagingUtils.getPage(commits, page, size);
        return PagingUtils.listToPage(pageList, 0, pageList.size(), paging, commits.size());
    }

}