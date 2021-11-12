package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import com.github.humbertovaz.gitChallenge.utils.PagingUtils;
import com.github.humbertovaz.gitChallenge.utils.DataCluster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

@ConfigurationProperties("commitloader")
public class LocalCommitLoader {
    private static final Logger logger = LogManager.getLogger();
    private List<CommitDTO> commits;
    private static int lineCounter = 0;
    private static int commitsRead = 0;
    private boolean commandSuccess = false;
    private boolean readLineSuccess = false;

    ExecutorService bashThreadExecutor = Executors.newSingleThreadExecutor();
    BashExecutor bashExecutor = new BashExecutor();

    public String timeout;

    public DataCluster dataCluster;

    public String getTimeout() {
        return timeout;
    }

    public DataCluster getDataCluster() { return dataCluster; }

    public void setTimeout(String timeout) { this.timeout = timeout; }

    public void setDataCluster(DataCluster dataCluster) { this.dataCluster = dataCluster; }

    final Future<Boolean> handler = bashThreadExecutor.submit(new Callable() {
        @Override
        public Boolean call() throws Exception {
            return bashExecutor.executeBashCommand();
        }
    });

    public List<CommitDTO> getCommits() { return commits; }

    private Page<CommitDTO> listToPage(List<CommitDTO> fooList, int start, int end, Pageable pageable, int listSize){
        return new PageImpl<CommitDTO>(fooList.subList(start, end), pageable, listSize);
    }

    public Page<CommitDTO> processCommits(int size, int page, Pageable paging) {
        List<CommitDTO> pageList = PagingUtils.getPage(commits, page, size);
        return listToPage(pageList, 0, pageList.size(), paging, commits.size());
    }

    public void init(){
        FileInputStream fstream;
        try {
            File file = new File(BashExecutor.getFilename()); // Create file to avoid NPE
            if (file.createNewFile()) {

                logger.info("File has been created.");
            } else {
                logger.info("File already exists.");
            }
            fstream = new FileInputStream(BashExecutor.getFilename());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            this.dataCluster = new DataCluster(in, br);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public boolean loadCommits() throws IOException {
        init();
        logger.info("Started to parse commits file");
        this.commits = new ArrayList<>();
        InputStream in = null;
        BufferedReader br = null;
        try {
            Duration BASH_TIMEOUT = Duration.ofMillis(Integer.parseInt(timeout));
            commandSuccess = handler.get(BASH_TIMEOUT.toMillis(), TimeUnit.MINUTES);
            if (!commandSuccess) {
                throw new RuntimeException("Bash command" + bashExecutor.getCommand());
            }

            in = dataCluster.getIn();
            br = dataCluster.getBr();

            String strLine;
            CommitDTO commitDTO = new CommitDTO();
            while ((strLine = br.readLine()) != null) {
                logger.info("Line: " + strLine);
                if (CommitParser.MESSAGE_PATTERN.matcher(strLine).matches()) {
                    // We've reached the last commit info line
                    CommitParser.lineToCommitDTO(strLine, commitDTO);
                    this.commits.add(commitDTO);
                    commitDTO = new CommitDTO();
                    LocalCommitLoader.commitsRead++;
                    logger.info("Written in memory " + commitsRead + " commits");
                } else {
                    readLineSuccess = CommitParser.lineToCommitDTO(strLine, commitDTO) != null;
                    if (readLineSuccess) {
                        LocalCommitLoader.lineCounter++;
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
            if(in != null){
                in.close();
            }
            File file = new File(bashExecutor.getFilename());
            if (file.delete()) {
                logger.info("Deleted the file: " + file.getName());
            } else {
                logger.error("Failed to delete the file: " + file.getName());
            }
            logger.info("Finished parsing commits");
            return true;
        }
    }

}
