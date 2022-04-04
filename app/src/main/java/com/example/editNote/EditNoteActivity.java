package com.example.editNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;
import com.example.myNote.MainViewModel;
import com.example.myNote.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.reactivestreams.Subscription;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EditNoteActivity extends AppCompatActivity implements DeleteDialog.DeleteNoteToEditNoteActivityCallback {
    private EditText editTextHeader;
    private EditText editTextDescription;
    private TextView textViewDate;
    private int checkSearchActivity;
    private String id;
    private String noteId;
    private String color;
//  private NotesDataBase dataBase;
    private BottomAppBar bottomAppBar;
    private Notes notes;
    private EditViewModel editViewModel;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editTextHeader = findViewById(R.id.editTextHeader);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewDate = findViewById(R.id.lastChangeOfDateTextView);
        id = "";

        initToolbar();

        bottomAppBar = findViewById(R.id.bottomAppBar);
        initialBottomAppBar();

//        dataBase = NotesDataBase.getInstance(getApplicationContext());
        FloatingActionButton floatingActionButtonSaveNote = findViewById(R.id.floatingActionButtonSaveNote);
        floatingActionButtonSaveNote.setOnClickListener(onClickSaveNewNote);

        EditViewModelFactory editViewModelFactory = new EditViewModelFactory(noteId, this.getApplication());
        editViewModel = new ViewModelProvider(this, editViewModelFactory).get(EditViewModel.class);
        getIntentFromMainActivity();
    }

    public void getIntentFromMainActivity() {
        Intent intentGetNote = getIntent();
        if (intentGetNote.hasExtra("id") && !intentGetNote.getStringExtra("id").isEmpty()) {
            noteId = intentGetNote.getStringExtra("id");
            Log.i("id", noteId);
            compositeDisposable.add(editViewModel.getNote()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Notes>() {
                        @Override
                        public void accept(Notes notes) throws Exception {
                            Log.i("id", "one");
                            editTextHeader.setText(notes.getHeader());
                            editTextDescription.setText(notes.getDescription());
                            textViewDate.setVisibility(View.VISIBLE);
                            textViewDate.setText(notes.getDate());
                            id = notes.getId();
                            color = notes.getColor();
                        }
                    }));


//            editViewModel.getNote()
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Notes>() {
//                        @Override
//                        public void accept(Notes notes) {
//                            editTextHeader.setText(notes.getHeader());
//                            editTextDescription.setText(notes.getDescription());
//                            textViewDate.setVisibility(View.VISIBLE);
//                            textViewDate.setText(notes.getDate());
//                            id = notes.getId();
//                            color = notes.getColor();
//                        }
//                    });
//            Notes notes = dataBase.noteDAO().getNoteById(noteId);
//            Log.i("id", notes.getId());
//            for (Notes note : MainActivity.notes) {
//                if (note.getId().equals(idNote)) {
//                    editTextHeader.setText(notes.getHeader());
//                    editTextDescription.setText(notes.getDescription());
//                    textViewDate.setVisibility(View.VISIBLE);
//                    textViewDate.setText(notes.getDate());
//                    id = notes.getId();
//                    color = notes.getColor();
//                    positionOfNote = MainActivity.notes.indexOf(note);
        }
//            }
    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        if (noteId == null) {
//            MenuItem menuItemDeleteButton = menu.findItem(R.id.toolbarDownButtonDelete);
//            menuItemDeleteButton.setVisible(false);
//            MenuItem menuItemShareButton = menu.findItem(R.id.toolbarDownButtonShare);
//            menuItemShareButton.setVisible(false);
//        }
//
//        return true;
//
//    }


    public void initToolbar() {
        Toolbar toolbarEditNote = findViewById(R.id.toolbarEditNoteUp);
        setSupportActionBar(toolbarEditNote);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbarEditNote.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbarEditNote.setNavigationOnClickListener(onClickButtonBack);
    }

    View.OnClickListener onClickButtonBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNoteAndFinish();
        }
    };

    View.OnClickListener onClickSaveNewNote = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNoteAndFinish();
        }
    };

    public void saveNoteAndFinish() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        String date = dateFormat.format(currentDate);
        String header = editTextHeader.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
//
//        if (!id.isEmpty()) {
//            notes = dataBase.noteDAO().findNotesById(noteId);
//            if (!header.isEmpty() || !description.isEmpty()) {
//                dataBase.noteDAO().deleteNote(notes);
//                Notes newNote = new Notes(id, header, description, color, date);
//                dataBase.noteDAO().insertNote(newNote);
//
//            } else {
//                dataBase.noteDAO().deleteNote(notes);
//            }
//        } else {
//            if (!header.isEmpty() || !description.isEmpty()) {
//                color = getRandomColor().trim();
//                id = UUID.randomUUID().toString().replace("-", "").trim();
//                Notes newNote = new Notes(id, header, description, color, date);
//                dataBase.noteDAO().insertNote(newNote);
//            }
//        }
        finish();
    }

    private String getRandomColor() {

        String firstColor = "#E2F3F0";
        String secondColor = "#FDCCCA";
        String thirdColor = "#C3D9FF";
        String fourthColor = "#F8D9DE";
        String fifthColor = "#FFF5E6";

        String[] colors = {firstColor, secondColor, thirdColor, fourthColor, fifthColor};
        String randomColor = colors[new Random().nextInt(colors.length)];
        Log.i("random", "" + randomColor);

        return randomColor;
    }
//    View.OnClickListener onClickButtonShare = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            shareMessage();
//        }
//    };
    public void shareNote() {
        if (editTextHeader.getText().toString().isEmpty() && editTextDescription.getText().toString().isEmpty()) {
            Log.i("dialog", "sendIsEmpty!");
        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
//            if (editTextHeader.getText().toString().isEmpty() || editTextDescription.getText().toString().isEmpty()) {
//                sendIntent.putExtra(Intent.EXTRA_TEXT, editTextHeader.getText().toString().trim() + editTextDescription.getText().toString().trim());
//            } else {
//                sendIntent.putExtra(Intent.EXTRA_TEXT, editTextHeader.getText().toString().trim() + "\n" + editTextDescription.getText().toString().trim());
//            }
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, editTextHeader.getText().toString().trim());
            sendIntent.putExtra(Intent.EXTRA_TEXT, editTextDescription.getText().toString().trim());


            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }

    private void initialBottomAppBar() {
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
                        shareNote();
                        return true;
                    case R.id.toolbarDownButtonAdd:
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onDeleteButtonClicked() {
//        notes = dataBase.noteDAO().findNotesById(noteId);
//        dataBase.noteDAO().deleteNote(notes);
//        Log.i("id","из метода: " + notes.getId());
        finish();
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
        compositeDisposable.clear();
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

