package com.example.DataBase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "note")
public class Notes {
//    @PrimaryKey(autoGenerate = true)
    @PrimaryKey
    @NonNull
    private String id;
    private String header;
    private String description;
    private String color;
    private String date;


    public Notes(@NonNull String id, String header, String description, String color, String date) {
        this.id = id;
        this.header = header;
        this.description = description;
        this.color = color;
        this.date = date;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public void setDate(String date) {
        this.date = date;
    }


}
