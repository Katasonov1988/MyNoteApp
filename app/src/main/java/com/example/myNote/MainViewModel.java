package com.example.myNote;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;

import java.util.List;
public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Notes>> notes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        NotesDataBase notesDataBase = NotesDataBase.getInstance(getApplication());
        notes = notesDataBase.noteDAO().getAllNotes();
    }

    public LiveData<List<Notes>> getNotes() {
        return notes;
    }

}

