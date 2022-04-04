package com.example.searchNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.SearchView;
import androidx.room.Database;

import android.annotation.SuppressLint;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;
import com.example.editNote.EditNoteActivity;
import com.example.myNote.MainActivity;
import com.example.myNote.NoteAdapter;
import com.example.myNote.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    private List<Notes> searchActivityNotes = new ArrayList<>();
    private String searchViewString;
    private TextView searchTextView;
//    private NotesDataBase database;
    private String queryFromStringQuery;
    NoteAdapter noteAdapter;
    RecyclerView recyclerviewSearchNotes;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("start", "onCreate");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            searchViewString = savedInstanceState.getString("searchView");
            Log.i("start", "searchViewString = " + searchViewString);
        }
        queryFromStringQuery = "";

        setContentView(R.layout.activity_search);
        searchTextView = findViewById(R.id.searchTextView);


//            searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

//        database = NotesDataBase.getInstance(this);

        Toolbar searchToolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(searchToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerviewSearchNotes = findViewById(R.id.recyclerviewSearchNotes);
        recyclerviewSearchNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteAdapter = new NoteAdapter(searchActivityNotes);
        noteAdapter.clearDataFromAdapter();
        recyclerviewSearchNotes.setAdapter(noteAdapter);

        noteAdapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Notes notes) {
                String noteId = notes.getId();
                Intent intent = new Intent(SearchActivity.this, EditNoteActivity.class);
                intent.putExtra("id", noteId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search_button);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.findViewById(androidx.appcompat.R.id.search_mag_icon).setVisibility(View.GONE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT);
        searchView.setQueryHint(getResources().getString( R.string.search ));
        searchView.setQuery(searchViewString, false);
        searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
        View closeBtn = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchViewString = s;
                searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
//                if (s.isEmpty()) {
//                    noteAdapter.clearDataFromAdapter();
//                    searchTextView.setVisibility(View.VISIBLE);
//                    searchTextView.setText(R.string.for_search_enter_text);
//                } else {
//
//                    searchViewModel.setQueryFromSearchString(s);
//                    LiveData<List<Notes>> notesFromDB = searchViewModel.getNotesFromHeaderOrDescription();
//                    notesFromDB.observe(SearchActivity.this, new Observer<List<Notes>>() {
//                        @Override
//                        public void onChanged(List<Notes> notesFromLiveData) {
//                            noteAdapter.setNewDataToAdapter(notesFromLiveData);
//                            if (searchActivityNotes.size() >= 1) {
//                                searchTextView.setVisibility(View.GONE);
//                            } else {
//                                searchTextView.setVisibility(View.VISIBLE);
//                                searchTextView.setText(R.string.found_nothing);
//                            }
//
//                        }
//                    });
////                    searchActivityNotes = database.noteDAO().searchNotesFromHeaderOrDescription(s);
////                    noteAdapter.setNewDataToAdapter(searchActivityNotes);
//
//                }

                return false;
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getQuery().toString().isEmpty()) {
                    finish();
                } else {
                    searchView.setQuery("", false);
                }
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("searchView", searchViewString);
        Log.i("start", "onSaveInstanceState: " + searchViewString);
        super.onSaveInstanceState(outState);
    }

    private  void changeSearchActivityScreen() {
        if (!searchActivityNotes.isEmpty()) {
            searchTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("start", "onStart");
//        if (!queryFromStringQuery.isEmpty()) {
//            searchActivityNotes = database.noteDAO().searchNotesFromHeaderOrDescription(queryFromStringQuery) ;
//            noteAdapter.setNewDataToAdapter(searchActivityNotes);
//            changeSearchActivityScreen();
//        };
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("start", "onPostResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("start", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        queryFromStringQuery = searchViewString;
        Log.i("start", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("start", "onDestroy");
    }
}


