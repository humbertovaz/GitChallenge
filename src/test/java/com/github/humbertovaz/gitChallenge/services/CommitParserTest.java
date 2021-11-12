package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


public class CommitParserTest {

    @Test
    public void testLineToCommitDTOCommitSHAWithBranches() {
        String line = "commit 7c12ee0634ff5bb8f2bd29c289c49a21d1bed874 (HEAD -> dev, origin/master, master)\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertEquals("7c12ee0634ff5bb8f2bd29c289c49a21d1bed874", commitDTO.getSha());
    }

    @Test
    public void testLineToCommitDTOCommitSHARegular() {
        String line = "commit 662808d2aeb18329e3526fbafc5eb4f3896550ce\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertEquals("662808d2aeb18329e3526fbafc5eb4f3896550ce", commitDTO.getSha());
    }

    @Test
    public void testLineToCommitDTOCommitAuthor() {
        String line = "Author: John Doe <user@mail.com>\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertEquals("John Doe <user@mail.com>", commitDTO.getAuthor());
    }

    @Test
    public void testLineToCommitDTOCommitAuthorStrangeEmail() {
        String line = "Author: John Doe <123123asd_asdasd.23@mail.com>\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertEquals("John Doe <123123asd_asdasd.23@mail.com>", commitDTO.getAuthor());
    }

    @Test
    public void testLineToCommitDTOCommitDate() {
        String line = "Date:   Tue Nov 9 12:15:18 2021 +0000\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertEquals("Tue Nov 9 12:15:18 2021 +0000", commitDTO.getDate());
    }

    @Test
    public void testLineToCommitDTOCommitMessage() {
        String line = "    Relocated CommitProcessor to services package\n";
        CommitDTO commitDTO = new CommitDTO();
        CommitParser.lineToCommitDTO(line, commitDTO);
        Assert.assertEquals("Relocated CommitProcessor to services package", commitDTO.getMessage());
    }
}
