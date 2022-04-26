package com.example.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


@Dao
public interface NoteDAO {

    @Query("SELECT * FROM note ORDER BY date DESC" )
    LiveData<List<Notes>> getAllNotes();

    @Query("SELECT * FROM note WHERE id = :noteId")
    Flowable <Notes> getNoteById(String noteId);

    @Query("SELECT * FROM note WHERE header LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY date DESC")
    Flowable <List<Notes>> searchOfNotes(String searchQuery);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public Completable insertOrUpdateNote (Notes notes);

    @Query("DELETE FROM note")
    public void deleteAllNotes();

    @Query("DELETE FROM note WHERE id = :noteId")
    public Completable deleteNoteById (String noteId);

}
