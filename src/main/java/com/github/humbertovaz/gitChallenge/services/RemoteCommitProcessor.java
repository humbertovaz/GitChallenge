package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;


public class RemoteCommitProcessor {


    @Autowired
    private RemoteCommitLoader remoteCommitLoader;

    public RemoteCommitProcessor() { }

    public RemoteCommitProcessor(RemoteCommitLoader remoteCommitLoader) { this.remoteCommitLoader = remoteCommitLoader; }

    /**
     * This method is called by CommitAggregator controller and calls RemoteCommitLoader#processCommits
     * Retuns paged CommitDTO (Page<CommitDTO>)
     * @param size - size of the page
     * @param page - desired page
     * @param paging - Pageable object
     */
    public Page<CommitDTO> processCommits(int size, int page, Pageable paging) throws IOException, JSONException {
        return remoteCommitLoader.processCommits(size, page, paging);
    }
}
