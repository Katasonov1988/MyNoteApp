package com.example.editNote;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myNote.MainActivity;
import com.example.myNote.Note;
import com.example.myNote.R;
import com.example.searchNote.SearchActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class EditNoteActivity extends AppCompatActivity {
    private EditText editTextHeader;
    private EditText editTextDescription;
    private TextView textViewDate;
    private int checkSearchActivity;
    private String id;
    private String idNote;
    private String color;
    private int positionOfNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editTextHeader = findViewById(R.id.editTextHeader);
        editTextDescription = findViewById(R.id.editTextDescription);
        FloatingActionButton floatingActionButtonSaveNote = findViewById(R.id.floatingActionButtonSaveNote);
        textViewDate = findViewById(R.id.lastChangeOfDateTextView);
        Toolbar toolbarEditNoteUp = findViewById(R.id.toolbarEditNoteUp);
        setSupportActionBar(toolbarEditNoteUp);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbarEditNoteUp.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        checkSearchActivity = 1;


        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbarDownButtonDelete:
                        if (id.isEmpty()) {
                            editTextHeader.setText("");
                            editTextDescription.setText("");


                        } else {
                            DialogFragment deleteDialogFragment = new DeleteDialog();
                            deleteDialogFragment.show(getSupportFragmentManager(), "delete");
                        }
                        return true;
                    case R.id.toolbarDownButtonShare:
                        sendMessage();
                        return true;
                    case R.id.toolbarDownButtonAdd:
                        return true;
                    default:
                        return true;
                }
            }
        });

        floatingActionButtonSaveNote.setOnClickListener(onClickSaveNewNote);
        toolbarEditNoteUp.setNavigationOnClickListener(onClickButtonBack);
        id = "";
        getIntentFromMainActivity();
    }

    public void getIntentFromMainActivity() {
        Intent intentGetNote = getIntent();
        checkSearchActivity = intentGetNote.getIntExtra("checkSearchActivity", 1);
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

    View.OnClickListener onClickButtonShare = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendMessage();
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
        String date = dateFormat.format(currentDate);
        String header = editTextHeader.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        if (id.equals(idNote)) {
            if (!header.isEmpty() || !description.isEmpty()) {
                Note oldNote = new Note(id, header, description, color, date);
                MainActivity.notes.set(positionOfNote, oldNote);
                if (checkSearchActivity == 0) {
                    SearchActivity.searchNotes.set(positionOfNote, oldNote);
                }
            } else {
                MainActivity.notes.remove(positionOfNote);
                if (checkSearchActivity == 0) {
                    SearchActivity.searchNotes.remove(positionOfNote);
                }
            }
        } else {
            if (!header.isEmpty() || !description.isEmpty()) {
                color = getRandomColor().trim();
                id = UUID.randomUUID().toString().replace("-", "").trim();
                Note note = new Note(id, header, description, color, date);
                MainActivity.notes.add(note);
            }
        }
        finish();
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

    public void sendMessage() {
        if (editTextHeader.getText().toString().isEmpty() && editTextDescription.getText().toString().isEmpty()) {
            Log.i("dialog", "sendIsEmpty!");
        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            if (editTextHeader.getText().toString().isEmpty() || editTextDescription.getText().toString().isEmpty()) {
                sendIntent.putExtra(Intent.EXTRA_TEXT, editTextHeader.getText().toString().trim() + editTextDescription.getText().toString().trim());
            } else {
                sendIntent.putExtra(Intent.EXTRA_TEXT, editTextHeader.getText().toString().trim() + "\n" + editTextDescription.getText().toString().trim());
            }
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("dialog", "onStart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("dialog", "onPostResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("dialog", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("dialog", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("dialog", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dialog", "onBackPressed");

    }
}

