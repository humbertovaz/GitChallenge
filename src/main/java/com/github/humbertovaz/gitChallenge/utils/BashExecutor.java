package com.github.humbertovaz.gitChallenge.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bash")
public class BashExecutor {
    private static final Logger logger = LogManager.getLogger();

    public static String filename;

    public static String command;

    public void setFilename(String filename) {
        BashExecutor.filename = filename;
    }

    public void setCommand(String command) {
        BashExecutor.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getFilename() {
        return filename;
    }

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
