package com.github.humbertovaz.gitChallenge.utils;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


public class CommitParserTest {

    @Test
    public void testLineToCommitDTOCommitSHA() {
        String line = "commit 7c12ee0634ff5bb8f2bd29c289c49a21d1bed874 (HEAD -> dev, origin/master, master)\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertTrue(commitDTO.getSha().equals("7c12ee0634ff5bb8f2bd29c289c49a21d1bed874"));
    }

    @Test
    public void testLineToCommitDTOCommitAuthor() {
        String line = "Author: John Doe <user@mail.com>\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertTrue(commitDTO.getAuthor().equals("John Doe <user@mail.com>"));
    }

    @Test
    public void testLineToCommitDTOCommitDate() {
        String line = "Date:   Tue Nov 9 12:15:18 2021 +0000\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertTrue(commitDTO.getDate().equals("Tue Nov 9 12:15:18 2021 +0000"));
    }

    @Test
    public void testLineToCommitDTOCommitMessage() {
        String line = "    Relocated CommitProcessor to services package\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertTrue(commitDTO.getMessage().equals("Relocated CommitProcessor to services package"));
    }
}
