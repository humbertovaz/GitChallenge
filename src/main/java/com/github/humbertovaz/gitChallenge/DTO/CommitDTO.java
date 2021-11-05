package com.github.humbertovaz.gitChallenge.DTO;

import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

// TODO: Rename it to CommitBuilder or Create simple DTO
@Component
public class CommitDTO implements Comparable<CommitDTO>{
    String sha;
    String message;
    String date;
    String author;

    public CommitDTO setSha(String sha) {
        this.sha = sha;
        return this;
    }

    public CommitDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommitDTO setDate(String date) {
        this.date = date;
        return this;
    }

    public CommitDTO setAuthor(String author) {
        this.author = author;
        return this;
    }
    // TODO: This could be useful in the future... Not sure
    public CommitDTO build() {
        return this;
    }

    @Override
    public int compareTo(CommitDTO o) {
        Date date = DatatypeConverter.parseDate(o.date).getTime();
        Date thisDate = DatatypeConverter.parseDate(this.date).getTime();
        if (thisDate.after(date)){
            return 1;
        }
        return -1;
    }
}
