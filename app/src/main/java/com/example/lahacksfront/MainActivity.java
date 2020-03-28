package com.example.lahacksfront;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recipeRV;
    private LinearLayoutManager rLayoutManager;
    private recyclerAdapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Recipe r1 = new Recipe("1", new String[]{"Ing 1", "Ing 2", "Ing 3"});
        Recipe r2 = new Recipe("2", new String[]{"Ing 1", "Ing 2", "Ing 3"});
        Recipe r3 = new Recipe("3", new String[]{"Ing 1", "Ing 2", "Ing 3"});
        Recipe r4 = new Recipe("4", new String[]{"Ing 1", "Ing 2", "Ing 3"});
        Recipe r5 = new Recipe("5", new String[]{"Ing 1", "Ing 2", "Ing 3"});

        Recipe[] rlist = {r1, r2, r3, r4, r5};


        recipeRV = (RecyclerView) findViewById(R.id.recipeRV);
        recipeRV.setHasFixedSize(true);


        rAdapter = new recyclerAdapter(rlist, this);

        rLayoutManager = new LinearLayoutManager(this);
        recipeRV.setAdapter(rAdapter);
        recipeRV.setLayoutManager(rLayoutManager);
    }


}
