package com.github.humbertovaz.gitChallenge.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bash")
public class BashExecutor {
    private static final Logger logger = LogManager.getLogger();

    public static String filename;

    public static String command;

    /**
     * This setter is responsible to set the externalized bash.filename string on application.properties
     * @param filename is a String that is going to be used on bash command
     */
    public void setFilename(String filename) {
        BashExecutor.filename = filename;
    }

    /**
     * This setter is responsible to set the externalized bash.command string on application.properties
     * @param command is a String that is going to be used on bash command
     */
    public void setCommand(String command) {
        BashExecutor.command = command;
    }

    /**
     * This getter is responsible to expose the externalized bash.command string on application.properties
     */
    public String getCommand() {
        return command;
    }

    /**
     * This getter is responsible to expose the externalized bash.filename string on application.properties
     */
    public static String getFilename() {
        return filename;
    }

    /**
     * This method is responsible for executing a local bash command and print its results to a file
     */
    public boolean executeBashCommand() {
        boolean success = false;
        this.setCommand(command + " > " + filename);
        logger.info("Executing BASH command:\n   " + command);
        Runtime r = Runtime.getRuntime();
        String[] commands = {"bash", "-c", command};
        try {
            Process p = r.exec(commands);
            p.waitFor();
            success = true;
        } catch (Exception e) {
            logger.error("Failed to execute bash with command: " + command);
            e.printStackTrace();
        }
        return success;
    }

}
