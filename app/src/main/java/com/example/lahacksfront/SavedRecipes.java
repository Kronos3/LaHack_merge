package com.example.lahacksfront;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SavedRecipes extends AppCompatActivity {

    private FloatingActionButton fab;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabManual;
    private FloatingActionButton fabGallery;

    private ImageView cameraImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        fab = findViewById(R.id.fab);
        fabCamera = findViewById(R.id.fabCamera);
        fabGallery = findViewById(R.id.fabGallery);
        fabManual = findViewById(R.id.fabManual);

        ViewAnimation.init(fabCamera);
        ViewAnimation.init(fabGallery);
        ViewAnimation.init(fabManual);


        //Floating Action Button

        final boolean[] isRotate = {false};

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "fabclicked");
                isRotate[0] = ViewAnimation.rotateFab(v, !isRotate[0]);

                if (isRotate[0]) {
                    ViewAnimation.showIn(fabCamera);
                    ViewAnimation.showIn(fabGallery);
                    ViewAnimation.showIn(fabManual);

                } else {
                    ViewAnimation.showOut(fabCamera);
                    ViewAnimation.showOut(fabGallery);
                    ViewAnimation.showOut(fabManual);
                }

            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = (Bitmap)data.getExtras().get("data");


    }
}
