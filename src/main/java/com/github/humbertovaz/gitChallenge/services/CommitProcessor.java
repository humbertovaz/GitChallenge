package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import com.github.humbertovaz.gitChallenge.utils.CommitLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class CommitProcessor {

    @Autowired
    private CommitLoader commitLoader;

    public CommitProcessor() { }

    public CommitProcessor(CommitLoader commitLoader) {
        this.commitLoader = commitLoader;
    }

    public List<CommitDTO> processCommits(){
        return commitLoader.getCommits();
    }

    public Page<CommitDTO> processCommits(int size, int page, Pageable paging){
        return commitLoader.processCommitsOnPage(size, page, paging);
    }
}
