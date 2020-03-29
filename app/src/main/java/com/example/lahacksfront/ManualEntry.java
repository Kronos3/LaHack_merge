package com.example.lahacksfront;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManualEntry extends AppCompatActivity {

    private EditText editText;
    private Button addButton;
    private ListView listview;
    private FloatingActionButton fab;

    private ArrayList<String> al;
    private ArrayAdapter<String> alAdapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry2);

        editText = findViewById(R.id.editText);
        addButton = findViewById(R.id.ingButton);
        listview = findViewById(R.id.ingList);
        fab = findViewById(R.id.savemanualfab);

        al = new ArrayList<>();

        alAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);

        listview.setAdapter(alAdapter);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        buttonClick();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rs = "";

                rs += al.get(0);
                for (int i = 1; i < al.size(); i++) {
                    rs += ","+al.get(i);

                }

                Log.d("rs", rs);


                Intent intent = new Intent(ManualEntry.this, MainActivity.class);
                intent.putExtra("query", rs);
                startActivity(intent);

            }
        });


    }


    public void buttonClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                String result = editText.getText().toString();
                if (result.length() > 0 && result != null) {
                    fab.setVisibility(View.VISIBLE);
                    al.add(result);
                    alAdapter.notifyDataSetChanged();
                }

            }
        });
    }
}
