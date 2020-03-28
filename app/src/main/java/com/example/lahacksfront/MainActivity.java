package com.example.lahacksfront;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recipeRV;
    private LinearLayoutManager rLayoutManager;
    private recyclerAdapter rAdapter;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Recipe r1 = new Recipe("Sandwich", new String[]{"Ing 1", "Ing 2", "Ing 3", "Ing4"});
        Recipe r2 = new Recipe("Fried Chicken", new String[]{"Ing 1", "Ing 2", "Ing 3"});
        Recipe r3 = new Recipe("Fried Fish", new String[]{"Ing 1", "Ing 2", "Ing 3"});
        Recipe r4 = new Recipe("Broccoli", new String[]{"Ing 1", "Ing 2", "Ing 3"});
        Recipe r5 = new Recipe("Mushroom Soup", new String[]{"Ing 1", "Ing 2", "Ing 3"});

        final Recipe[] rlist = {r1, r2, r3, r4, r5};


        recipeRV = (RecyclerView) findViewById(R.id.recipeRV);
        recipeRV.setHasFixedSize(true);


        rAdapter = new recyclerAdapter(rlist, this);

        rLayoutManager = new LinearLayoutManager(this);
        recipeRV.setAdapter(rAdapter);
        recipeRV.setLayoutManager(rLayoutManager);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recipe Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView = findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                recyclerAdapter rvAdapter = new recyclerAdapter(rlist,MainActivity.this);
                recipeRV.setAdapter(rvAdapter);
                recipeRV.invalidate();

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText != null && !newText.isEmpty()){
                    Log.d("im here", "yoooo");
                    //Log.d("im here", "im here!!!!");


                    ArrayList<Recipe> listFoundTemp = new ArrayList<>();
                    for(Recipe item: rlist){
                        Log.d("im here", ""+Arrays.toString(rlist));
                        Log.d("im here", ""+item);

                        if(item.getName().toLowerCase().contains(newText.toLowerCase())){
                            listFoundTemp.add(item);

                        }
                    }

                    Recipe[] listFound = new Recipe[listFoundTemp.size()];
                    for(int i = 0; i < listFound.length; i++){
                        listFound[i] = listFoundTemp.get(i);
                    }





                    recipeRV.invalidate();
                    recyclerAdapter rvAdapter = new recyclerAdapter(listFound,MainActivity.this);
                    recipeRV.setAdapter(rvAdapter);


                }
                else{
                    recyclerAdapter rvAdapter = new recyclerAdapter(rlist,MainActivity.this);
                    recipeRV.setAdapter(rvAdapter);
                    recipeRV.invalidate();
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
}
