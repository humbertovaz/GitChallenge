package com.github.humbertovaz.gitChallenge.DTO;

import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
public class CommitDTO implements Serializable {
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

}
