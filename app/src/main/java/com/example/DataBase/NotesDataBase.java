package com.example.DataBase;

import android.content.Context;
import android.graphics.Color;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myNote.R;

@Database(entities = {Notes.class}, version = 1)
public abstract class NotesDataBase extends RoomDatabase {

private static NotesDataBase dataBase;
private static final String DB_NAME = "notes";

public static NotesDataBase getInstance(Context context)     {
    if (dataBase == null) {
dataBase = Room.databaseBuilder(context,NotesDataBase.class,DB_NAME)
        .build();
    }
    return dataBase;
}
    public abstract NoteDAO noteDAO();
}
