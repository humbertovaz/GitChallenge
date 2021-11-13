package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import com.github.humbertovaz.gitChallenge.config.Config;
import com.github.humbertovaz.gitChallenge.utils.DataCluster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Config.class)
public class LocalCommitLoaderTest {

    @Autowired
    private LocalCommitLoader localCommitLoader;


    @Test
    public void testLoadCommits() throws IOException {
        String fileName = "commits.txt";
        FileOutputStream fos = new FileOutputStream(fileName, true);
        String commitsDump = "commit 7c12ee0634ff5bb8f2bd29c289c49a21d1bed874\n" +
                "Author: John Doe <johndoe@mail.com>\n" +
                "Date:   Tue Nov 9 12:15:18 2021 +0000\n" +
                "\n" +
                "    Lorem ipsum dolor sit amet, consectetur adipiscing elit\n" +
                "\n" +
                "commit 662808d2aeb18329e3526fbafc5eb4f3896550ce\n" +
                "Author: John Doe <johndoe@mail.com>\n" +
                "Date:   Mon Nov 8 18:57:06 2021 +0000\n" +
                "\n" +
                "    Excepteur sint occaecat cupidatat non proident\n";
        fos.write(commitsDump.getBytes(StandardCharsets.UTF_8));
        fos.close();

        File file = new File(fileName);
        FileDescriptor fd = fos.getFD();
        InputStream fis = new FileInputStream(fd);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        DataCluster mockDc = new DataCluster(fis, br);
        localCommitLoader.setDataCluster(mockDc);
        Assert.assertTrue(localCommitLoader.loadCommits());
        List<CommitDTO> commitDTOList = localCommitLoader.getCommits();
        CommitDTO commitA = commitDTOList.get(0);
        CommitDTO commitB = commitDTOList.get(1);

        Assert.assertEquals("7c12ee0634ff5bb8f2bd29c289c49a21d1bed874", commitA.getSha());
        Assert.assertEquals("John Doe <johndoe@mail.com>", commitA.getAuthor());
        Assert.assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit", commitA.getMessage());
        Assert.assertEquals("Tue Nov 9 12:15:18 2021 +0000", commitA.getDate());
        Assert.assertEquals("662808d2aeb18329e3526fbafc5eb4f3896550ce", commitB.getSha());
        Assert.assertEquals("John Doe <johndoe@mail.com>", commitB.getAuthor());
        Assert.assertEquals("Excepteur sint occaecat cupidatat non proident", commitB.getMessage());
        Assert.assertEquals("Mon Nov 8 18:57:06 2021 +0000", commitB.getDate());
    }
}
