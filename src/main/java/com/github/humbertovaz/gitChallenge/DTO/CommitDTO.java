package com.github.humbertovaz.gitChallenge.DTO;

import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.util.Date;

// TODO: Rename it to CommitBuilder or Create simple DTO
// TODO: Remove or consider use order (Comparable)
@Component
public class CommitDTO implements Comparable<CommitDTO>, Serializable {
    String sha;
    String message;
    String date;
    String author;

    public String getSha() {
        return sha;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

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
