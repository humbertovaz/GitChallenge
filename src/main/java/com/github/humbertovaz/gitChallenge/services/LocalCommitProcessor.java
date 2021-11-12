package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class LocalCommitProcessor {

    @Autowired
    private LocalCommitLoader localCommitLoader;

    public LocalCommitProcessor() { }

    public LocalCommitProcessor(LocalCommitLoader localCommitLoader) {
        this.localCommitLoader = localCommitLoader;
    }

    public Page<CommitDTO> processCommits(int size, int page, Pageable paging){
        return localCommitLoader.processCommits(size, page, paging);
    }
}
