package com.github.humbertovaz.gitChallenge.utils;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.annotation.PostConstruct;
import java.io.*;
import java.sql.Time;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

@ConfigurationProperties("commitloader")
public class CommitLoader {
    private static final Logger logger = LogManager.getLogger();
    private List<CommitDTO> commits;
    private static int lineCounter = 0;
    private static int commitsRead = 0;
    private boolean commandSuccess = false;
    private boolean readLineSuccess = false;
    public String timeout;
    ExecutorService bashThreadExecutor = Executors.newSingleThreadExecutor();
    BashExecutor bashExecutor = new BashExecutor();

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }


    final Future<Boolean> handler = bashThreadExecutor.submit(new Callable() {
        @Override
        public Boolean call() throws Exception {
            return bashExecutor.executeBashCommand();
        }
    });


    public List<CommitDTO> getCommits() {
        return commits;
    }


    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("Invalid page: " + page + " or Invalid page size: " + pageSize + "\n");
        }

        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() <= fromIndex){
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    private Page<CommitDTO> listToPage(List<CommitDTO> fooList, int start, int end, Pageable pageable, int listSize){
        Page<CommitDTO> page // TODO: Remove unecessary variable
                = new PageImpl<CommitDTO>(fooList.subList(start, end), pageable, listSize);
        return page;
    }

    public Page<CommitDTO> processCommitsOnPage(int size, int page, Pageable paging) {

        List<CommitDTO> pageList = getPage(commits, page, size);
        return listToPage(pageList, 0, pageList.size(), paging, commits.size());

    }


    @PostConstruct
    public void loadCommits() throws IOException {

        logger.info("Started to parse commits file");
        this.commits = new ArrayList<>();
        FileInputStream fstream;
        DataInputStream in = null;
        try {
            Duration BASH_TIMEOUT = Duration.ofMillis(Integer.parseInt(timeout));
            commandSuccess = handler.get(BASH_TIMEOUT.toMillis(), TimeUnit.MINUTES);
            if (!commandSuccess) {
                throw new RuntimeException("Bash command" + bashExecutor.getCommand());
            }
            fstream = new FileInputStream(bashExecutor.getFilename());
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            CommitDTO commitDTO = new CommitDTO();
            while ((strLine = br.readLine()) != null) {
                logger.info("Line: " + strLine);
                if (CommitParser.MESSAGE_PATTERN.matcher(strLine).matches()) {
                    // We've reached the last commit info line
                    CommitParser.lineToCommitDTO(strLine, commitDTO);
                    this.commits.add(commitDTO.build());
                    commitDTO = new CommitDTO();
                    CommitLoader.commitsRead++;
                    logger.info("Written in memory " + commitsRead + " commits");
                } else {
                    readLineSuccess = CommitParser.lineToCommitDTO(strLine, commitDTO) != null;
                    if (readLineSuccess) {
                        CommitLoader.lineCounter++;
                        logger.info("Read " + lineCounter + " lines");
                    }
                }
            }
        }catch (TimeoutException e){
            handler.cancel(true);
            logger.error("Timeout on bash command: " + bashExecutor.getCommand());
        }catch (Exception e){
            logger.error("Error: " + e.getMessage());
        } finally {
            bashThreadExecutor.shutdownNow();
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
        return;

    }

}
