package com.example.myNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.editNote.EditNoteActivity;
import com.example.searchNote.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonAddNote;
    private RecyclerView recyclerViewNotes;
    public static List<Note> notes = new ArrayList<>();
    private LinearLayout linearLayoutStartScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewNotes = findViewById(R.id.recyclerviewNotes);
        linearLayoutStartScreen = findViewById(R.id.linearLayoutStartScreen);
        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        floatingActionButtonAddNote = findViewById(R.id.floatingActionButtonAddNote);
        floatingActionButtonAddNote.setOnClickListener(onClickNewNote);

        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note t1, Note t2) {
                return t1.getDate().compareToIgnoreCase(t2.getDate());
            }
        });
        Collections.reverse(notes);

        if (notes.isEmpty()) {
                linearLayoutStartScreen.setVisibility(View.VISIBLE);
                getSupportActionBar().hide();

        }
        NoteAdapter noteAdapter = new NoteAdapter(notes);

        recyclerViewNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
     //   recyclerViewNotes.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewNotes.setAdapter(noteAdapter);
        noteAdapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {

             Note note = notes.get(position);
             String noteId = note.getId();
                Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
                intent.putExtra("id",noteId);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
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

}