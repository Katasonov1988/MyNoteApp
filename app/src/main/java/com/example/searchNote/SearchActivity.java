package com.example.searchNote;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.myNote.NoteAdapter;
import com.example.myNote.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    private final List<Notes> searchActivityNotes = new ArrayList<>();
    private String searchViewString;
    private TextView searchTextView;
    NoteAdapter noteAdapter;
    RecyclerView recyclerviewSearchNotes;
    private SearchViewModel searchViewModel;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("start", "onCreate");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            searchViewString = savedInstanceState.getString("searchView");
            Log.i("start", "searchViewString = " + searchViewString);

        }
        Log.i("start", "onCreateSearchActivity");

        setContentView(R.layout.activity_search);
        searchTextView = findViewById(R.id.searchTextView);


        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

//        pressedBackButton ();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

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
        MenuItem searchItem = menu.findItem(R.id.search_button);
        searchItem.expandActionView();


        searchView = (SearchView) searchItem.getActionView();
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//      searchView.setIconified(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.isFocusable();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocusFromTouch();
        searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
        searchView.findViewById(androidx.appcompat.R.id.search_mag_icon).setVisibility(View.GONE);

//        searchView.onActionViewExpanded();
//        searchView.requestFocus();


//        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT);
//        searchView.setQuery(searchViewString, false);


        View closeBtn = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
                if (s.isEmpty()) {
                    searchTextView.setVisibility(View.VISIBLE);
                    searchTextView.setText(R.string.for_search_enter_text);
                    noteAdapter.clearDataFromAdapter();
                } else {
                    compositeDisposable.add(searchViewModel.getNotes(s)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<Notes>>() {
                                           @Override
                                           public void accept(List<Notes> notes) throws Exception {
                                               if (notes.isEmpty()) {
                                                   noteAdapter.clearDataFromAdapter();
                                                   searchTextView.setVisibility(View.VISIBLE);
                                                   searchTextView.setText(R.string.found_nothing);
                                               } else {
                                                   noteAdapter.setNotes(notes);
                                                   searchTextView.setVisibility(View.GONE);
                                               }

                                           }
                                       }, new Consumer<Throwable>() {
                                           @Override
                                           public void accept(Throwable notes) throws Exception {
                                               Log.e("error", "error: ", notes);
                                           }
                                       }
                            ));

                }

                return false;
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getQuery().toString().isEmpty()) {
                    finish();
                } else {
//                    searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
                    searchView.setQuery("", false);
//                    searchView.requestFocus();
                }
            }
        });
        return true;
    }


//    public void pressedBackButton () {
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (searchView.getQuery().toString().isEmpty()) {
//                    finish();
//                } else {
//                    searchView.setQuery("", false);
//                }
//                Log.i("start", "handleOnBackPressedSearchActivity");
////                finish();
//            }
//        };
//        SearchActivity.this.getOnBackPressedDispatcher().addCallback(
//                this, // LifecycleOwner
//                callback);
//    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.i("start", "onBackPressedSearchActivity");
//
//    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("searchView", searchViewString);
        Log.i("start", "onSaveInstanceStateSearchActivity: " + searchViewString);
        super.onSaveInstanceState(outState);
    }

    private void changeSearchActivityScreen() {
        if (!searchActivityNotes.isEmpty()) {
            searchTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("start", "onStartSearchActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("start", "onResumeSearchActivity");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("start", "onPostResumeSearchActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("start", "onPauseSearchActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("start", "onStopSearchActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("start", "onDestroySearchActivity");
    }
}


