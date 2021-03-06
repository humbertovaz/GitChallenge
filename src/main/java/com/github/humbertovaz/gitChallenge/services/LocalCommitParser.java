package com.github.humbertovaz.gitChallenge.services;

import com.github.humbertovaz.gitChallenge.DTO.CommitDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalCommitParser {

    final static Pattern SHA_PATTERN = Pattern.compile("^commit\\s([\\w\\d]+).*$");
    final static Pattern AUTHOR_PATTERN = Pattern.compile("^Author: ([\\w\\s]+<[\\w\\d.]+@[\\w\\d]+\\.\\w+>)$");
    final static Pattern DATE_PATTERN = Pattern.compile("^Date:\\s+(.+)$");
    public final static Pattern MESSAGE_PATTERN = Pattern.compile("^\\s{4}(.+)$");

    /**
     * This method is responsible to convert the requirements for converting a string about git log into a CommitDTO object
     * @param strLine - string about git log
     * @param commitDTO - CommitDTO object to be filled with commit info
     */
    public static CommitDTO lineToCommitDTO(String strLine, CommitDTO commitDTO) {
        Matcher shaMatcher = SHA_PATTERN.matcher(strLine);
        Matcher authorMatcher = AUTHOR_PATTERN.matcher(strLine);
        Matcher dateMatcher = DATE_PATTERN.matcher(strLine);
        Matcher messageMatcher = MESSAGE_PATTERN.matcher(strLine);

        boolean isSha = shaMatcher.find();
        boolean isAuthor = authorMatcher.find();
        boolean isDate = dateMatcher.find();
        boolean isMessage = messageMatcher.find();

        if(isSha){
            commitDTO.setSha(shaMatcher.group(1));
            return commitDTO;
        } else if (isAuthor) {
            commitDTO.setAuthor(authorMatcher.group(1));
            return commitDTO;
        } else if (isDate){
            commitDTO.setDate(dateMatcher.group(1));
            return commitDTO;
        } else if (isMessage){
            String oldMsg = commitDTO.getMessage();
            if(oldMsg!= null){
                commitDTO.setMessage(oldMsg + "\n" + messageMatcher.group(1));
            } else {
                commitDTO.setMessage(messageMatcher.group(1));
            }

            return commitDTO;
        }
        return null;
    }
}
