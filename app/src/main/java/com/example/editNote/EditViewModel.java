package com.example.editNote;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;

import io.reactivex.Flowable;

public class EditViewModel extends AndroidViewModel {

    private final Flowable<Notes> note;
    public String noteId;

    public EditViewModel(@NonNull Application application, String noteId) {
        super(application);
        this.noteId = noteId;
        NotesDataBase notesDataBase = NotesDataBase.getInstance(getApplication());
        note = notesDataBase.noteDAO().getNoteById(noteId);
    }

    public Flowable<Notes> getNote() {
        return note;
    }
}
