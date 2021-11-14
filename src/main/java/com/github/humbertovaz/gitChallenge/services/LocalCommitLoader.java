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

    public int timeout;

    public DataCluster dataCluster;

    /**
     * This getter is responsible to expose the externalized commitloader.timeout int on application.properties
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * This setter is responsible to set the externalized commitloader.timeout string on application.properties
     * @param timeout is an integer that is going to be used when loading commits to memory
     */
    public void setTimeout(int timeout) { this.timeout = timeout; }

    /**
     * This setter is responsible to set the DataCluster object
     */
    public void setDataCluster(DataCluster dataCluster) { this.dataCluster = dataCluster; }

    /**
     * This getter is responsible to get commits field
     */
    public List<CommitDTO> getCommits() { return commits; }


    final Future<Boolean> handler = bashThreadExecutor.submit(new Callable() {
        /**
         * This method is responsible to call BashExecutor#executeBashCommand with a timeout
         */
        @Override
        public Boolean call() throws Exception {
            return bashExecutor.executeBashCommand();
        }
    });

    /**
     * This method is responsible to convert a List<CommitDTO> to Page<CommitDTO>
     * @param fooList - list of commits
     * @param start - start of paging
     * @param end - end of paging
     * @param pageable - Pageable object
     * @param listSize - size of the desired list
     *
     */
    private Page<CommitDTO> listToPage(List<CommitDTO> fooList, int start, int end, Pageable pageable, int listSize){
        return new PageImpl<CommitDTO>(fooList.subList(start, end), pageable, listSize);
    }
    /**
     * This method is responsible to process the list that was loaded and return it by pages
     * @param size - size of the desired list
     * @param page - desired page
     * @param paging - Pageable object
     *
     */
    public Page<CommitDTO> processCommits(int size, int page, Pageable paging) {
        List<CommitDTO> pageList = PagingUtils.getPage(commits, page, size);
        return listToPage(pageList, 0, pageList.size(), paging, commits.size());
    }
    /**
     * This method is responsible to initiating the requirements for executing a bash command
     */
    public void init(){
        FileInputStream fstream;
        try {
            File file = new File(BashExecutor.getFilename());
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

    /**
     * This method is responsible for executing a bash command. It is initiated on startup of the application
     */
    @PostConstruct
    public boolean loadCommits() throws IOException {
        init();
        logger.info("Started to parse commits file");
        this.commits = new ArrayList<>();
        InputStream in = null;
        BufferedReader br = null;
        try {
            Duration BASH_TIMEOUT = Duration.ofMillis(timeout);
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
                if (LocalCommitParser.MESSAGE_PATTERN.matcher(strLine).matches()) {
                    // We've reached the last commit info line
                    LocalCommitParser.lineToCommitDTO(strLine, commitDTO);
                    br.mark(9999);
                    String nextLine = br.readLine();
                    br.reset();
                    if(nextLine== null || LocalCommitParser.SHA_PATTERN.matcher(nextLine).matches() || nextLine.equals("")){
                        this.commits.add(commitDTO);
                        commitDTO = new CommitDTO();
                        LocalCommitLoader.commitsRead++;
                        logger.info("Written in memory " + commitsRead + " commits");
                    }
                } else {
                    readLineSuccess = LocalCommitParser.lineToCommitDTO(strLine, commitDTO) != null;
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
