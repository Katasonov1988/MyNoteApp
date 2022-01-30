package com.example.myNote;

public class Note {
    private String id;
    private String header;
    private String description;
    private String color;
    private String date;

    public Note(String id, String header, String description, String color, String date) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.color = color;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String time) {
        this.date = date;
    }
}
