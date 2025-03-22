package com.emsi.recycleviewtp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emsi.recycleviewtp.adapter.StarAdapter;
import com.emsi.recycleviewtp.beans.Star;
import com.emsi.recycleviewtp.service.StarService;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private List<Star> stars;
    private RecyclerView recyclerView;
    private StarAdapter starAdapter = null;
    private StarService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        try {
            stars = new ArrayList<>();
            service = StarService.getInstance();
            init();

            recyclerView = findViewById(R.id.recycle_view);
            if (recyclerView != null) {
                starAdapter = new StarAdapter(this, service.findAll());
                recyclerView.setAdapter(starAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                Log.e(TAG, "RecyclerView not found in layout");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void init(){
        try {
            // Using reliable image URLs from placeholder services
            service.create(new Star(1, "Kate Bosworth", "https://i.pravatar.cc/300?img=1", 4));
            service.create(new Star(2, "George Clooney", "https://i.pravatar.cc/300?img=8", 3));
            service.create(new Star(3, "Michelle Rodriguez", "https://i.pravatar.cc/300?img=5", 5));
            service.create(new Star(4, "Tom Cruise", "https://i.pravatar.cc/300?img=11", 1));
            service.create(new Star(5, "Louise Bouroin", "https://i.pravatar.cc/300?img=9", 5));
            service.create(new Star(6, "Brad Pitt", "https://i.pravatar.cc/300?img=15", 1));
        } catch (Exception e) {
            Log.e(TAG, "Error initializing data: " + e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.menu, menu);
            MenuItem menuItem = menu.findItem(R.id.app_bar_search);
            if (menuItem != null) {
                SearchView searchView = (SearchView) menuItem.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (starAdapter != null) {
                            starAdapter.getFilter().filter(newText);
                        }
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateOptionsMenu: " + e.getMessage());
        }
        return true;
    }
}
