package com.example.editNote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.DataBase.Notes;

public class EditViewModelFactory implements ViewModelProvider.Factory {
    private final String noteId;
    private final Application application;

    public EditViewModelFactory(String noteId, Application application) {
        super();
        this.noteId = noteId;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == EditViewModel.class) {
            return (T) new EditViewModel(application, noteId);
        }
        return null ;
    }
}
