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

    public Page<CommitDTO> processCommits(int size, int page, Pageable paging) throws IOException, JSONException {
        return remoteCommitLoader.processCommits(size, page, paging);
    }
}
