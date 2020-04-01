package com.example.lahacksfront;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lahacksfront.networking.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class SavedRecipes extends AppCompatActivity {

    private FloatingActionButton fab;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabManual;
    private FloatingActionButton fabGallery;
    private FloatingActionButton fabReceipt;
    private Button saved;
    private NetworkUtils nu;
    private String searchId;

    private ImageView cameraImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nu = new NetworkUtils();

        setContentView(R.layout.activity_saved_recipes);

        fab = findViewById(R.id.fab);
        fabCamera = findViewById(R.id.fabCamera);
        fabGallery = findViewById(R.id.fabGallery);
        fabManual = findViewById(R.id.fabManual);
        fabReceipt = findViewById(R.id.fabReceipt);
        saved = findViewById(R.id.savebutton);

        ViewAnimation.init(fabCamera);
        ViewAnimation.init(fabGallery);
        ViewAnimation.init(fabManual);
        ViewAnimation.init(fabReceipt);


        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent saveIntent = new Intent(SavedRecipes.this, saveMenu.class);
                startActivity(saveIntent);

            }
        });


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
                    ViewAnimation.showIn(fabReceipt);

                } else {
                    ViewAnimation.showOut(fabCamera);
                    ViewAnimation.showOut(fabGallery);
                    ViewAnimation.showOut(fabManual);
                    ViewAnimation.showOut(fabReceipt);
                }

            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.d("camera", "camera");
                startActivityForResult(cameraIntent, 0);


            }
        });

        fabManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manualIntent = new Intent(SavedRecipes.this, ManualEntry.class);
                startActivity(manualIntent);

            }
        });

        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Log.d("camera", "camera2");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);





            }


        });

        fabReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 12346);



            }


        });


    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch(requestCode) {
//            case 1234:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImage = data.getData();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String filePath = cursor.getString(columnIndex);
//                    cursor.close();
//
//
//                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
//                    Log.d("bitmap", "123"+yourSelectedImage.toString());
//                    /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
//                }
//            case 12345:
//                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//                Log.d("bitmap", "123"+bitmap.toString());
//        }
//
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("requestCode", ""+requestCode);
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){

                    Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                    Log.d("bitmap", "Camera"+bitmap.toString());
                    bitmaps.add(bitmap);

                    searchId = nu.start_process(nu.upload_file_obj(bitmaps));
                    Log.d("bitmap", searchId);


                }
                break;

            case 1:
                Log.d("camera", "camera22");
                if(resultCode == RESULT_OK){
                    final Uri imageUri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    Log.d("bitmap", "Gallery"+selectedImage.toString());

                    bitmaps.add(selectedImage);

                    searchId = nu.start_process(nu.upload_file_obj(bitmaps));
                    Log.d("bitmap", searchId);
                    /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                }

                break;

            case 12346:
                if(resultCode == RESULT_OK){
                    final Uri imageUri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    Log.d("bitmap", "Gallery"+selectedImage.toString());

                    bitmaps.add(selectedImage);

                    searchId = nu.start_process(nu.upload_file_text(bitmaps));
                    Log.d("bitmap", searchId);
                    /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                }
                break;

        }

        Intent picIntent = new Intent(SavedRecipes.this, MainActivity.class);
        picIntent.putExtra("querySearchId", searchId);
        startActivity(picIntent);



    }
}
