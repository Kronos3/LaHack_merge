package com.example.lahacksfront;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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


public class saveMenu extends AppCompatActivity {

    //Views
    private RecyclerView recipeRV;
    private LinearLayoutManager rLayoutManager;
    private recyclerAdapter rAdapter;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private String query;
    private TextView loadingText;
    private NetworkUtils networkUtils;

    private ArrayList<Recipe> al;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_menu);
        NetworkUtils nu = new NetworkUtils();
        final String[] id = {""};



        final Recipe[][] a = new Recipe[1][1];

        //Assign all views
        recipeRV = findViewById(R.id.recipeRV);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_view);
        loadingText = findViewById(R.id.loadingText);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //networking call for saved recipes

                    al = nu.userRecipes();

                    Log.d("userRecipes", al.toString());

                    a[0] = new Recipe[al.size()];

                    for(int j = 0; j<al.size(); j ++){
                        a[0][j] = al.get(j);
                    }



                    Log.d("recipes", Arrays.toString(a[0]));

                    saveMenu.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rAdapter.setRecipeList(a[0]);
                            recipeRV.setVisibility(View.VISIBLE);
                            loadingText.setVisibility(View.INVISIBLE);

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

        final Recipe[] rlist = a[0];


        //Recycler View Adapter
        recipeRV.setHasFixedSize(true);
        rAdapter = new recyclerAdapter(rlist, this);
        rLayoutManager = new LinearLayoutManager(this);
        recipeRV.setAdapter(rAdapter);
        recipeRV.setLayoutManager(rLayoutManager);

        //Search Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recipe Search");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                recyclerAdapter rvAdapter = new recyclerAdapter(rlist, saveMenu.this);
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
                    recyclerAdapter rvAdapter = new recyclerAdapter(listFound, saveMenu.this);
                    recipeRV.setAdapter(rvAdapter);


                } else {
                    recyclerAdapter rvAdapter = new recyclerAdapter(rlist, saveMenu.this);
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

