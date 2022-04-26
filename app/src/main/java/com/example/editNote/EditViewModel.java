package com.example.editNote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class EditViewModel extends AndroidViewModel {

    private final Flowable<Notes> note;
    private final NotesDataBase notesDataBase;

    public EditViewModel(@NonNull Application application, String noteId) {
        super(application);
        notesDataBase = NotesDataBase.getInstance(getApplication());
        note = notesDataBase.noteDAO().getNoteById(noteId);
    }

    public Flowable<Notes> getNote() {
        return note;
    }

    public Completable insertOrUpdateNote(Notes notes) {
        return notesDataBase.noteDAO().insertOrUpdateNote(notes);
    }

    public Completable deleteNoteById(String noteId) {
        return notesDataBase.noteDAO().deleteNoteById(noteId);
    }
}
