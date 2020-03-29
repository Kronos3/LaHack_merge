package com.example.lahacksfront;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.lahacksfront.networking.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.awaitility.Awaitility.await;


public class MainActivity extends AppCompatActivity {

    //Views
    private RecyclerView recipeRV;
    private LinearLayoutManager rLayoutManager;
    private recyclerAdapter rAdapter;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private NetworkUtils networkUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkUtils nu = new NetworkUtils();

        final Recipe[] a = new Recipe[15];


        //Assign all views
        recipeRV = findViewById(R.id.recipeRV);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_view);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //Log.d("test", nu.searchId("beans"));
                    String id = nu.searchId("apple");
                    id = id.substring(id.indexOf(" ") + 1, id.length() - 1);

                    for (int i = 0; i < 15; i++) {
                        Log.d("test1", id);
                        a[i] = nu.getRecipe(id);


                    }
                   
                    Log.d("recipes", Arrays.toString(a));

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rAdapter.setRecipeList(a);

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });

        thread.start();


        //Temporary Code
        Recipe r1 = new Recipe("Sandwich", new Ingredient[]{new Ingredient("Ing 1"), new Ingredient("Ing 1"), new Ingredient("Ing 1"), new Ingredient("Ing 1"), new Ingredient("Ing 1")});
        Recipe r2 = new Recipe("Fried Chicken", new Ingredient[]{new Ingredient("Ing 1"), new Ingredient("Ing 1"), new Ingredient("Ing 1")});
        Recipe r3 = new Recipe("Fried Butter", new Ingredient[]{new Ingredient("Ing 1"), new Ingredient("Ing 1"), new Ingredient("Ing 1")});
        Recipe r4 = new Recipe("Pizza", new Ingredient[]{new Ingredient("Ing 1"), new Ingredient("Ing 1"), new Ingredient("Ing 1")});
        Recipe r5 = new Recipe("Mushroom stew", new Ingredient[]{new Ingredient("Ing 1"), new Ingredient("Ing 1"), new Ingredient("Ing 1")});

        final Recipe[] rlist = a;


        //Recycler View Adapter
        recipeRV.setHasFixedSize(true);
        rAdapter = new recyclerAdapter(rlist, this);
        rLayoutManager = new LinearLayoutManager(this);
        recipeRV.setAdapter(rAdapter);
        recipeRV.setLayoutManager(rLayoutManager);

        //Search Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recipe Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                recyclerAdapter rvAdapter = new recyclerAdapter(rlist, MainActivity.this);
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

                if (newText != null && !newText.isEmpty()) {
                    Log.d("im here", "yoooo");
                    //Log.d("im here", "im here!!!!");


                    ArrayList<Recipe> listFoundTemp = new ArrayList<>();
                    for (Recipe item : rlist) {
                        Log.d("im here", "" + Arrays.toString(rlist));
                        Log.d("im here", "" + item);

                        if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                            listFoundTemp.add(item);

                        }
                    }

                    Recipe[] listFound = new Recipe[listFoundTemp.size()];
                    for (int i = 0; i < listFound.length; i++) {
                        listFound[i] = listFoundTemp.get(i);
                    }


                    recipeRV.invalidate();
                    recyclerAdapter rvAdapter = new recyclerAdapter(listFound, MainActivity.this);
                    recipeRV.setAdapter(rvAdapter);


                } else {
                    recyclerAdapter rvAdapter = new recyclerAdapter(rlist, MainActivity.this);
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

