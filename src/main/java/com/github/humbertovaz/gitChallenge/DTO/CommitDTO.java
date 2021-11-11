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

    public void setSha(String sha) {
        this.sha = sha;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
