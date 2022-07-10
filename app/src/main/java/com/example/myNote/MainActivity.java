package com.example.myNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.DataBase.Notes;
import com.example.editNote.EditNoteActivity;
import com.example.searchNote.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final List<Notes> notes = new ArrayList<>();
    private final List<Notes> notesForDB = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        Log.i("start", "onCreateMainActivity");

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notesFromLiveData) {
                Log.i("start", "onChangeMainActivity");
                notesForDB.clear();
                notesForDB.addAll(notesFromLiveData);
                Log.i("start", "notes = " + notes.size() );
                noteAdapter.setNotes(notesForDB);
                changeMainScreen();
            }
        });

        Toolbar mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        FloatingActionButton floatingActionButtonAddNote = findViewById(R.id.floatingActionButtonAddNote);
        floatingActionButtonAddNote.setOnClickListener(onClickNewNote);

        RecyclerView recyclerViewNotes = findViewById(R.id.recyclerviewNotes);
        recyclerViewNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteAdapter = new NoteAdapter(notes);
        recyclerViewNotes.setAdapter(noteAdapter);

        noteAdapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Notes notes) {
                String noteId = notes.getId();
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("id", noteId);
                startActivity(intent);
            }
        });


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search_note) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onClickNewNote = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivity(intent);
        }
    };

    private void changeMainScreen() {
        if (notes.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getSupportActionBar()).hide();
        } else {
            textView.setVisibility(View.GONE);
            Objects.requireNonNull(getSupportActionBar()).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("start", "onStartMainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("start", "onResumeMainActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("start", "onRestartMainActivity");
    }

    @Override
    protected void onPostResume() {
        noteAdapter.setNotes(notesForDB);
        super.onPostResume();
        Log.i("start", "onPostResumeMainActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("start", "onPauseMainActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("start", "onStopMainActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("start", "onDestroyMainActivity");
    }
}