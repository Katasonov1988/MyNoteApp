package com.example.editNote;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.example.myNote.MainActivity;
import com.example.myNote.R;

public class DeleteDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper((EditNoteActivity)getActivity(),R.style.AlertDialogTheme));
            builder.setTitle(R.string.dialog_title)
                    .setMessage(R.string.dialog_message);

            builder.setPositiveButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            })
                    .setNegativeButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                        public int positionOfNote;

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.notes.remove(positionOfNote);
                            Intent intent = new Intent((EditNoteActivity)getActivity(), MainActivity.class);
                            startActivity(intent);
                            dialogInterface.dismiss();
                        }
                    });
            return builder.create();
        }
    }
