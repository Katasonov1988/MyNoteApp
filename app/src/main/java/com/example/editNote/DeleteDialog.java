package com.example.editNote;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;
import com.example.myNote.MainActivity;
import com.example.myNote.R;
import com.example.searchNote.SearchActivity;

import java.util.List;

public class DeleteDialog extends DialogFragment {

    private static final String TAG = "MyDeleteDialog";

    public DeleteNoteToEditNoteActivityCallback deleteNoteToEditNoteActivityCallback;

    public interface DeleteNoteToEditNoteActivityCallback {
        void onDeleteButtonClicked();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper((EditNoteActivity) getActivity(), R.style.AlertDialogTheme));
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .setNegativeButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteNoteToEditNoteActivityCallback.onDeleteButtonClicked();
                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            deleteNoteToEditNoteActivityCallback = (DeleteNoteToEditNoteActivityCallback) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: " + e.getMessage());
        }
    }
}
