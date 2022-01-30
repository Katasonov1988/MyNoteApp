package com.example.searchNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myNote.MainActivity;
import com.example.myNote.R;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar searchToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(searchToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        MenuItem searchItem = menu.findItem(R.id.search_button);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.findViewById(androidx.appcompat.R.id.search_mag_icon).setVisibility(View.GONE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT);
        searchView.setQueryHint("Search");
        searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
        View closeBtn = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getQuery().toString().isEmpty()) {
                    Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    searchView.setQuery("", false);
                }
                searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setVisibility(View.VISIBLE);
            }
        });
        return true;
    }
}

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//            int id = item.getItemId();
//        switch (id) {
//            case R.id.close_item:
//                Toast.makeText(SearchActivity.this, "Закрыть", Toast.LENGTH_SHORT).show();
//            break;
//            case R.id.action_search_note:
//                Log.i("search", "Назад к поиску");
//                Toast.makeText(SearchActivity.this,"Назад к поиску", Toast.LENGTH_SHORT).show();
//                break;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

