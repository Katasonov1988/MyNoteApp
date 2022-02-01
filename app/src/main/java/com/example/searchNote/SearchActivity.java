package com.example.searchNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.example.editNote.EditNoteActivity;
import com.example.myNote.MainActivity;
import com.example.myNote.Note;
import com.example.myNote.NoteAdapter;
import com.example.myNote.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerviewSearchNotes;
    ArrayList<Note> searchNotes = new ArrayList<>();
    private LinearLayout linearLayoutSearchScreen;
    NoteAdapter noteAdapter;
    private TextView searchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchTextView = findViewById(R.id.searchTextView);
        recyclerviewSearchNotes = findViewById(R.id.recyclerviewSearchNotes);
        linearLayoutSearchScreen = findViewById(R.id.linearLayoutSearchScreen);
        Toolbar searchToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(searchToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        searchNotes.add(new Note("2233","заголовок","описание","#F8D9DE","30.11.1988"));

        noteAdapter = new NoteAdapter(searchNotes);
        recyclerviewSearchNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //   recyclerViewNotes.setLayoutManager(new GridLayoutManager(this,2));
        recyclerviewSearchNotes.setAdapter(noteAdapter);
        noteAdapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {

                Note note = searchNotes.get(position);
                String noteId = note.getId();
                Intent intent = new Intent(SearchActivity.this, EditNoteActivity.class);
                intent.putExtra("id",noteId);
                startActivity(intent);
            }
        });




    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(SearchActivity.this,MainActivity.class);
        startActivity(intent);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search_button);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.findViewById(androidx.appcompat.R.id.search_mag_icon).setVisibility(View.GONE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT);
        searchView.setQueryHint("Search");
        searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
        View closeBtn = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                // If the list contains the search query
                // than filter the adapter
                // using the filter method
                // with the query as its argument

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                for (Note note : MainActivity.notes) {
                    if (!s.isEmpty() & !searchNotes.contains(note) & note.getHeader().toLowerCase().contains(s.toLowerCase()) || note.getDescription().toLowerCase().contains(s.toLowerCase())) {
//                         note.getId();

                        searchNotes.add(note);

                        linearLayoutSearchScreen.setVisibility(View.GONE);
                    } else if (searchNotes.isEmpty()) {
                        searchNotes.clear();
                        linearLayoutSearchScreen.setVisibility(View.VISIBLE);
                        searchTextView.setText("Ничего не найдено");
                    } else {
                        searchNotes.clear();
                        linearLayoutSearchScreen.setVisibility(View.VISIBLE);
                        searchTextView.setText("Ничего не найдено");
                    }


                    noteAdapter.notifyDataSetChanged();
                }

                return false;
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getQuery().toString().isEmpty()) {
                    Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {

                    searchView.setQuery("", false);
                    searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
                }

            }


        });
        return true;
    }


}

//

