package com.example.editNote;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.example.myNote.MainActivity;
import com.example.myNote.R;
import com.example.searchNote.SearchActivity;

public class DeleteDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper((EditNoteActivity)getActivity(),R.style.AlertDialogTheme));
            builder.setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_message);
            Log.i("dialog", "диалог");
            builder.setPositiveButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                }
            })
                    .setNegativeButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                        public int positionOfNote;
                        public int checkSearchActivity;

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.notes.remove(positionOfNote);
                            if (!SearchActivity.searchNotes.isEmpty()) {
                                SearchActivity.searchNotes.remove(positionOfNote);
                            }
                            Log.i("dialog", "кнопка удалить");
                            dialogInterface.dismiss();
                            getActivity().finish();
                        }
                    });
            return builder.create();
        }
    }
