package com.github.humbertovaz.gitChallenge.utils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("bash")
public class BashExecutor {

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
        String cmd = command + ">" + filename;
        System.out.println("Executing BASH command:\n   " + cmd);
        Runtime r = Runtime.getRuntime();
        String[] commands = {"bash", "-c", cmd};
        try {
            Process p = r.exec(commands);
            p.waitFor();
            success = true;
        } catch (Exception e) {
            System.err.println("Failed to execute bash with command: " + cmd);
            e.printStackTrace();
        }
        return success;
    }

}
