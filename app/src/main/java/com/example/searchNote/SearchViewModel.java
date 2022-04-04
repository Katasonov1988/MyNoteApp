package com.example.searchNote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private NotesDataBase notesDataBase;
    private LiveData<List<Notes>> notes;
    private String queryFromSearchString;


    public void setQueryFromSearchString(String queryFromSearchString) {
        this.queryFromSearchString = queryFromSearchString;
    }

    public SearchViewModel(@NonNull Application application) {
        super(application);
//        notesDataBase = NotesDataBase.getInstance(getApplication());
//        notes = notesDataBase.noteDAO().searchNotesFromHeaderOrDescription(queryFromSearchString);
    }

    public LiveData<List<Notes>> getNotesFromHeaderOrDescription() {
        return  notes;
    }


}
