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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.example.editNote.EditNoteActivity;
import com.example.myNote.MainActivity;
import com.example.myNote.Note;
import com.example.myNote.NoteAdapter;
import com.example.myNote.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements Filterable {
    public static final int checkSearchActivity = 0;
    public static List<Note> searchNotes = new ArrayList<>();

    private String searchViewString;

    private LinearLayout linearLayoutSearchScreen;
    NoteAdapter noteAdapter;
    RecyclerView recyclerviewSearchNotes;

    private TextView searchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("start", "onCreate");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            searchViewString = savedInstanceState.getString("searchView");
            Log.i("start", "searchViewString = " + searchViewString);
        }
        searchNotes.clear();
        setContentView(R.layout.activity_search);
        searchTextView = findViewById(R.id.searchTextView);
        recyclerviewSearchNotes = findViewById(R.id.recyclerviewSearchNotes);
        linearLayoutSearchScreen = findViewById(R.id.linearLayoutSearchScreen);
        Toolbar searchToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(searchToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerviewSearchNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteAdapter = new NoteAdapter(searchNotes);
        recyclerviewSearchNotes.setAdapter(noteAdapter);

        noteAdapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Note note = searchNotes.get(position);
                String noteId = note.getId();
                Intent intent = new Intent(SearchActivity.this, EditNoteActivity.class);
                intent.putExtra("id", noteId);
                intent.putExtra("checkSearchActivity", checkSearchActivity);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        searchNotes.clear();
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
        searchView.setQueryHint("Search");
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
                getFilter().filter(s);
                return false;
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getQuery().toString().isEmpty()) {
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    searchView.setQuery("", false);
                }
            }
        });
        return true;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        final List<Note> filterNotes = new ArrayList<>();

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filterNotes.clear();
            if (charSequence.toString().isEmpty()) {
                filterNotes.clear();
            } else {
                for (Note note : MainActivity.notes) {
                    if (note.getHeader().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            note.getDescription().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filterNotes.add(note);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterNotes;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            searchNotes.clear();
            searchNotes.addAll((Collection<? extends Note>) filterResults.values);
            noteAdapter.notifyDataSetChanged();
            if (charSequence.toString().isEmpty()) {
                linearLayoutSearchScreen.setVisibility(View.VISIBLE);
                searchTextView.setText(R.string.for_search_enter_text);
            } else if (searchNotes.size() >= 1) {
                linearLayoutSearchScreen.setVisibility(View.GONE);
            } else {
                linearLayoutSearchScreen.setVisibility(View.VISIBLE);
                searchTextView.setText(R.string.found_nothing);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("searchView", searchViewString);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.notifyDataSetChanged();
        if (!searchNotes.isEmpty()) {
            linearLayoutSearchScreen.setVisibility(View.GONE);
        }
        Log.i("start", "onStart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        noteAdapter.notifyDataSetChanged();
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
        Log.i("start", "onStop");
        linearLayoutSearchScreen.setVisibility(View.VISIBLE);
        searchTextView.setText(R.string.found_nothing);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("start", "onDestroy");
    }

}


