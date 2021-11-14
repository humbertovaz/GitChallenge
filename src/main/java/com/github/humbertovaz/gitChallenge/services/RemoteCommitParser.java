package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class RemoteCommitParser {
    private static final Logger logger = LogManager.getLogger();

    /**
     * This method is responsible to convert a json fomatted list of commits (string) into a List of CommitDTO (List<CommitDTO>)
     */
    public static List<CommitDTO> jsonToCommitDTO(String jsonString) throws JSONException {
        logger.info("Started parsing commits");
        JSONArray arr = new JSONArray(jsonString);

        List<CommitDTO> commitList = new ArrayList<>();
        CommitDTO commitDTO;
        for (int i = 0; i < arr.length(); i++)
        {
            String sha = arr.getJSONObject(i).getString("sha");
            JSONObject commit = arr.getJSONObject(i).getJSONObject("commit");
            String authorName = commit.getJSONObject("author").getString("name");
            String authorEmail = commit.getJSONObject("author").getString("email");
            String date = commit.getJSONObject("author").getString("date");
            String message = commit.getString("message");
            commitDTO = new CommitDTO();
            commitDTO.setSha(sha).setAuthor(authorName + " <" + authorEmail +">").setDate(date).setMessage(message);
            commitList.add(commitDTO);
        }
        logger.info("Finished parsing Commits");
        return commitList;
    }

}