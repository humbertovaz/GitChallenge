package com.github.humbertovaz.gitChallenge.utils;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import java.util.regex.Pattern;

public class CommitParser {

    final Pattern SHA_PATTERN = Pattern.compile("^commit ([a-zA-Z0-9]+)\\s.+");
    final Pattern AUTHOR_PATTERN = Pattern.compile("Author: ([\\w\\s]+<[\\w\\d]+@[\\w\\d]+\\.\\w+>)");
    final Pattern DATE_PATTERN = Pattern.compile("Date:\\s+(.+)");
    final Pattern MESSAGE_PATTERN = Pattern.compile("\\s+(.*)$");
    private static String filename;

    public static String getFilename() {
        return filename;
    }

    /**
     * This setter is responsible to set the externalized file.filename string on application.properties
     * @param filename is a String that is going to be used on the loading of the commits
     * on the defined file
     */
    public void setFilename(String filename){
        this.filename = filename;
    }

    public static CommitDTO lineToCommitDTO(String strLine) {
        //TODO
        return null;
    }

    public static boolean executeBashCommand() {
        boolean success = false;
        String command = "git log > "+ filename;
        System.out.println("Executing BASH command:\n   " + command);
        Runtime r = Runtime.getRuntime();
        // Use bash -c so we can handle things like multi commands separated by ; and
        // things like quotes, $, |, and \. My tests show that command comes as
        // one argument to bash, so we do not need to quote it to make it one thing.
        // Also, exec may object if it does not have an executable file as the first thing,
        // so having bash here makes it happy provided bash is installed and in path.
        String[] commands = {"bash", "-c", command};
        try {
            Process p = r.exec(commands);

            p.waitFor();
            success = true;
        } catch (Exception e) {
            System.err.println("Failed to execute bash with command: " + command);
            e.printStackTrace();
        }
        return success;
    }

    public static void loadCommitsToFile(){
        // Print to file
    }
}
