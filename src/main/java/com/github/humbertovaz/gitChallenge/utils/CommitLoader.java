package com.github.humbertovaz.gitChallenge.utils;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class CommitLoader {
    private static final Logger logger = LogManager.getLogger();
    private List<CommitDTO> commits;
    private static int lineCounter = 0;
    private static int commitsRead = 0;
    private boolean commandSuccess = false;
    private boolean readLineSuccess = false;


    @Bean("loadCommits")
    public void loadCommits() throws IOException {

        logger.info("Started to parse commits file");
        this.commits = new ArrayList<>();
        FileInputStream fstream;
        DataInputStream in = null;
        BashExecutor bashExecutor = new BashExecutor();

        try{
            commandSuccess = bashExecutor.executeBashCommand();
            if (!commandSuccess){
                throw new RuntimeException("Bash command" + bashExecutor.getCommand()); // NPE
            }
            fstream = new FileInputStream(bashExecutor.getFilename());
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            CommitDTO commitDTO = new CommitDTO();
            while ((strLine = br.readLine()) != null) {
                logger.info("Line: "+ strLine);
                if(CommitParser.MESSAGE_PATTERN.matcher(strLine).matches()){
                    // We've reached the last commit info line
                    CommitParser.lineToCommitDTO(strLine, commitDTO);
                    this.commits.add(commitDTO.build());
                    commitDTO = new CommitDTO();
                    CommitLoader.commitsRead++;
                    logger.info("Written in memory "+ commitsRead + " commits");
                } else {
                   readLineSuccess = CommitParser.lineToCommitDTO(strLine, commitDTO) != null;
                   if(readLineSuccess){
                       CommitLoader.lineCounter++;
                       logger.info("Read "+ lineCounter + " lines");
                   }
                }
            }
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());

        } finally {
            assert in != null;
            in.close();
            File file = new File(bashExecutor.getFilename());
            if (file.delete()) {
                logger.info("Deleted the file: " + file.getName());
            } else {
                logger.error("Failed to delete the file.");
            }
        }
        logger.info("Finished parsing commits");

    }

    @Bean("commits")
    @DependsOn({"loadCommits"})
    public List<CommitDTO> commits(){
        return new ArrayList<>(commits);
    }
}
