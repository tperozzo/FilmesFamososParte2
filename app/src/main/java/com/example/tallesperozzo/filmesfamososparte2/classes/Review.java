package com.example.tallesperozzo.filmesfamososparte2.classes;

public class Review {

    private String id;
    private String author;
    private String content;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Review(String id, String author, String content) {

        this.id = id;
        this.author = author;
        this.content = content;
    }
}
