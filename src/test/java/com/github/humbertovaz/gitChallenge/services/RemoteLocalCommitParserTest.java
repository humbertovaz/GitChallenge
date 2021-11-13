package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.List;

public class RemoteLocalCommitParserTest {

    @Test
    public void testJsonCommitDTO(){
        String jsonString = "[\n" +
                "  {\n" +
                "    \"sha\": \"ec74a154ee3601665de244264b84cfd76c448079\",\n" +
                "    \"node_id\": \"MDY6Q29tbWl0NDU5OTA1NzU6ZWM3NGExNTRlZTM2MDE2NjVkZTI0NDI2NGI4NGNmZDc2YzQ0ODA3OQ==\",\n" +
                "    \"commit\": {\n" +
                "      \"author\": {\n" +
                "        \"name\": \"John Doe\",\n" +
                "        \"email\": \"John.Doe@mail.com\",\n" +
                "        \"date\": \"2016-02-20T16:24:22Z\"\n" +
                "      },\n" +
                "      \"committer\": {\n" +
                "        \"name\": \"John Doe\",\n" +
                "        \"email\": \"John.Doe@mail.com\",\n" +
                "        \"date\": \"2016-02-20T16:24:22Z\"\n" +
                "      },\n" +
                "      \"message\": \"Create README.md\",\n" +
                "      \"tree\": {\n" +
                "        \"sha\": \"6df78ad224fd84308f5a0f17279bee4ba1d4662d\",\n" +
                "        \"url\": \"https://api.github.com/repos/johndoe/public-repos/git/trees/6df78ad224fd84308f5a0f17279bee4ba1d4662d\"\n" +
                "      },\n" +
                "      \"url\": \"https://api.github.com/repos/johndoe/public-repos/git/commits/ec74a154ee3601665de244264b84cfd76c448079\",\n" +
                "      \"comment_count\": 0,\n" +
                "      \"verification\": {\n" +
                "        \"verified\": false,\n" +
                "        \"reason\": \"unsigned\",\n" +
                "        \"signature\": null,\n" +
                "        \"payload\": null\n" +
                "      }\n" +
                "    },\n" +
                "    \"url\": \"https://api.github.com/repos/johndoe/public-repos/commits/ec74a154ee3601665de244264b84cfd76c448079\",\n" +
                "    \"html_url\": \"https://github.com/johndoe/public-repos/commit/ec74a154ee3601665de244264b84cfd76c448079\",\n" +
                "    \"comments_url\": \"https://api.github.com/repos/johndoe/public-repos/commits/ec74a154ee3601665de244264b84cfd76c448079/comments\",\n" +
                "    \"author\": {\n" +
                "      \"login\": \"johndoe\",\n" +
                "      \"id\": 10663283,\n" +
                "      \"node_id\": \"MDQ6VXNlcjEwNjYzMjgz\",\n" +
                "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/10663283?v=4\",\n" +
                "      \"gravatar_id\": \"\",\n" +
                "      \"url\": \"https://api.github.com/users/johndoe\",\n" +
                "      \"html_url\": \"https://github.com/johndoe\",\n" +
                "      \"followers_url\": \"https://api.github.com/users/johndoe/followers\",\n" +
                "      \"following_url\": \"https://api.github.com/users/johndoe/following{/other_user}\",\n" +
                "      \"gists_url\": \"https://api.github.com/users/johndoe/gists{/gist_id}\",\n" +
                "      \"starred_url\": \"https://api.github.com/users/johndoe/starred{/owner}{/repo}\",\n" +
                "      \"subscriptions_url\": \"https://api.github.com/users/johndoe/subscriptions\",\n" +
                "      \"organizations_url\": \"https://api.github.com/users/johndoe/orgs\",\n" +
                "      \"repos_url\": \"https://api.github.com/users/johndoe/repos\",\n" +
                "      \"events_url\": \"https://api.github.com/users/johndoe/events{/privacy}\",\n" +
                "      \"received_events_url\": \"https://api.github.com/users/johndoe/received_events\",\n" +
                "      \"type\": \"User\",\n" +
                "      \"site_admin\": false\n" +
                "    },\n" +
                "    \"committer\": {\n" +
                "      \"login\": \"johndoe\",\n" +
                "      \"id\": 10663283,\n" +
                "      \"node_id\": \"MDQ6VXNlcjEwNjYzMjgz\",\n" +
                "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/10663283?v=4\",\n" +
                "      \"gravatar_id\": \"\",\n" +
                "      \"url\": \"https://api.github.com/users/johndoe\",\n" +
                "      \"html_url\": \"https://github.com/johndoe\",\n" +
                "      \"followers_url\": \"https://api.github.com/users/johndoe/followers\",\n" +
                "      \"following_url\": \"https://api.github.com/users/johndoe/following{/other_user}\",\n" +
                "      \"gists_url\": \"https://api.github.com/users/johndoe/gists{/gist_id}\",\n" +
                "      \"starred_url\": \"https://api.github.com/users/johndoe/starred{/owner}{/repo}\",\n" +
                "      \"subscriptions_url\": \"https://api.github.com/users/johndoe/subscriptions\",\n" +
                "      \"organizations_url\": \"https://api.github.com/users/johndoe/orgs\",\n" +
                "      \"repos_url\": \"https://api.github.com/users/johndoe/repos\",\n" +
                "      \"events_url\": \"https://api.github.com/users/johndoe/events{/privacy}\",\n" +
                "      \"received_events_url\": \"https://api.github.com/users/johndoe/received_events\",\n" +
                "      \"type\": \"User\",\n" +
                "      \"site_admin\": false\n" +
                "    },\n" +
                "    \"parents\": [\n" +
                "      {\n" +
                "        \"sha\": \"503e5fddb1a104b62c777832dad209fea45051ef\",\n" +
                "        \"url\": \"https://api.github.com/repos/johndoe/public-repos/commits/503e5fddb1a104b62c777832dad209fea45051ef\",\n" +
                "        \"html_url\": \"https://github.com/johndoe/public-repos/commit/503e5fddb1a104b62c777832dad209fea45051ef\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]\n";
        try {
            List<CommitDTO> commitDTOList = RemoteCommitParser.jsonToCommitDTO(jsonString);
            CommitDTO firstCommit = commitDTOList.get(0);
            Assert.assertEquals(firstCommit.getSha(),"ec74a154ee3601665de244264b84cfd76c448079");
            Assert.assertEquals(firstCommit.getDate(),"2016-02-20T16:24:22Z");
            Assert.assertEquals(firstCommit.getAuthor(),"John Doe <John.Doe@mail.com>");
            Assert.assertEquals(firstCommit.getMessage(),"Create README.md");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
