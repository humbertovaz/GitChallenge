package com.github.humbertovaz.gitChallenge.utils;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.io.*;
import java.util.*;

public class CommitLoader {
    private static final Logger logger = LogManager.getLogger();
    private List<CommitDTO> commits;


    @Bean("loadCommits")
    public void loadCommits() throws IOException {

        logger.info("Started to parse commits file");
        this.commits = new ArrayList<>();
        FileInputStream fstream;
        DataInputStream in = null;

        try{
            fstream = new FileInputStream(CommitParser.getFilename());
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                CommitDTO commitDTO = CommitParser.lineToCommitDTO(strLine);
                this.commits.add(commitDTO);
            }
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());

        } finally {
            assert in != null;
            in.close();
            // TODO: Remove file
        }
        logger.info("Finished parsing commits");

    }

    @Bean("commits")
    @DependsOn({"loadCommits"})
    public Set<CommitDTO> commits(){
        return new TreeSet<>(commits);
    }
}
