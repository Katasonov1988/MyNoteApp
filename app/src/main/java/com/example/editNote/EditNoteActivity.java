package com.example.editNote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myNote.MainActivity;
import com.example.myNote.Note;
import com.example.myNote.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class EditNoteActivity extends AppCompatActivity {
    private  Context context;
    private Button buttonToolbarDelete;
    private Button buttonToolbarShare;
    private Button buttonToolbarAdd;
    private EditText editTextHeader;
    private EditText editTextDescription;
    private TextView textViewDate;
    private FloatingActionButton floatingActionButtonSaveNote;

    private String id;
    private String idNote;
    private String header;
    private String description;
    private String color;
    private String date;
    int positionOfNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        buttonToolbarDelete = findViewById(R.id.toolbarDownButtonDelete);
        buttonToolbarShare = findViewById(R.id.toolbarDownButtonShare);
        buttonToolbarAdd = findViewById(R.id.toolbarDownButtonAdd);
        editTextHeader = findViewById(R.id.editTextHeader);
        editTextDescription = findViewById(R.id.editTextDescription);
        floatingActionButtonSaveNote = findViewById(R.id.floatingActionButtonSaveNote);
        textViewDate = findViewById(R.id.lastChangeOfDateTextView);


        Toolbar toolbarEditNoteUp = findViewById(R.id.toolbarEditNoteUp);
        setSupportActionBar(toolbarEditNoteUp);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbarEditNoteUp.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);


        Toolbar toolbarEditNoteDown = findViewById(R.id.toolbarEditNoteDown);
        setSupportActionBar(toolbarEditNoteDown);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        buttonToolbarAdd.setOnClickListener(onClickButtonAdd);
        buttonToolbarDelete.setOnClickListener(onClickButtonDelete);
        buttonToolbarShare.setOnClickListener(onClickButtonShare);
        floatingActionButtonSaveNote.setOnClickListener(onClickSaveNewNote);
        toolbarEditNoteUp.setNavigationOnClickListener(onClickButtonBack);
        id = "";
        getIntentFromMainActivity();


    }

    public void getIntentFromMainActivity() {
        Intent intentGetNote = getIntent();
        if (intentGetNote.hasExtra("id") && !intentGetNote.getStringExtra("id").isEmpty()) {
            idNote = intentGetNote.getStringExtra("id");
            Log.i("id", idNote);
            for (Note note : MainActivity.notes) {
                if (note.getId().equals(idNote)) {
                    editTextHeader.setText(note.getHeader());
                    editTextDescription.setText(note.getDescription());
                    textViewDate.setVisibility(View.VISIBLE);
                    textViewDate.setText(note.getDate());
                    id = note.getId();
                    color = note.getColor();
                    positionOfNote = MainActivity.notes.indexOf(note);
                    Log.i("id", id);
                }
            }
        }
    }


    View.OnClickListener onClickSaveNewNote = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            saveNote();
        }
    };

    View.OnClickListener onClickButtonDelete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (textViewDate.getText().toString().isEmpty()) {
                Toast.makeText(EditNoteActivity.this,"Заметка еще не создана", Toast.LENGTH_SHORT).show();
            } else {
                DialogFragment deleteDialogFragment = new DeleteDialog();
                if ( !editTextHeader.getText().toString().isEmpty() || !editTextDescription.getText().toString().isEmpty()) {
                    deleteDialogFragment.show(getSupportFragmentManager(), "delete");
                } else {
                    deleteDialogFragment.onDestroyView();
                }
            }


        }
    };

    View.OnClickListener onClickButtonShare = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendMessage();
        }
    };

    View.OnClickListener onClickButtonAdd = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(EditNoteActivity.this, "В заметку добавленны данные", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onClickButtonBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNote();
        }
    };

    public void saveNote() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        date = dateFormat.format(currentDate);
        header = editTextHeader.getText().toString().trim();
        description = editTextDescription.getText().toString().trim();
        if (id.equals(idNote)) {
            if (!header.isEmpty() || !description.isEmpty()) {
                Note oldNote = new Note(id, header, description, color, date);
                MainActivity.notes.set(positionOfNote, oldNote);
            } else {
                MainActivity.notes.remove(positionOfNote);
            }


        } else {

            if (!header.isEmpty() || !description.isEmpty()) {
                color = getRandomColor().trim();
                id = UUID.randomUUID().toString().replace("-", "").trim();
                Note note = new Note(id, header, description, color, date);
                MainActivity.notes.add(note);
            }
//            else {
//                Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
//                startActivity(intent);
//            }

        }
        Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
        startActivity(intent);
    }


    private String getRandomColor() {
        String colorRandom = null;
        Random random = new Random();
        int randomNumber = random.nextInt(6 - 1) + 1;
        switch (randomNumber) {
            case 1:
                colorRandom = "#E2F3F0";
                break;
            case 2:
                colorRandom = "#FDCCCA";
                break;
            case 3:
                colorRandom = "#C3D9FF";
                break;
            case 4:
                colorRandom = "#F8D9DE";
            case 5:
                colorRandom = "#FFF5E6";
                break;
        }
        return colorRandom;
    }

public void sendMessage () {
        if (editTextHeader.getText().toString().isEmpty() && editTextDescription.getText().toString().isEmpty()) {
Log.i("empty","empty message");

        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            if (editTextHeader.getText().toString().isEmpty() || editTextDescription.getText().toString().isEmpty()) {
                sendIntent.putExtra(Intent.EXTRA_TEXT,editTextHeader.getText().toString().trim() + editTextDescription.getText().toString().trim());
            } else {
                sendIntent.putExtra(Intent.EXTRA_TEXT,editTextHeader.getText().toString().trim() + "\n" + editTextDescription.getText().toString().trim());
            }
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

}

}