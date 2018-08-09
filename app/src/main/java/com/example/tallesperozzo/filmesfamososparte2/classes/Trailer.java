package com.example.tallesperozzo.filmesfamososparte2.classes;

public class Trailer {
    private String id;
    private String name;
    private String key;

    public Trailer(String id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
