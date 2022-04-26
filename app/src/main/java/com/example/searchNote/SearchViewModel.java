package com.example.searchNote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;

import java.util.List;

import io.reactivex.Flowable;

public class SearchViewModel extends AndroidViewModel {

    private NotesDataBase notesDataBase;
    private Flowable<List<Notes>> notes;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        notesDataBase = NotesDataBase.getInstance(getApplication());
    }

    public Flowable<List<Notes>> getNotes(String searchQuery) {
        notes = notesDataBase.noteDAO().searchOfNotes(searchQuery);
        return notes;
    }
}
