package com.example.lahacksfront;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.recyclerViewHolder> {

    private Recipe[] recipeList;
    private Context c;


    public recyclerAdapter(Recipe[] recipeList, Context c) {

        this.recipeList = recipeList;
        this.c = c;
    }

    public void setRecipeList(Recipe[] newRcp) {
        this.recipeList = newRcp;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return recipeList.length;
    }

    @Override
    public void onBindViewHolder(final recyclerViewHolder p, int i) {

        if (recipeList[i] != null) {
            //Set Recipe Name
            p.recipeName.setText(recipeList[i].getName());

            //Add Ingredients
            ArrayList<String> ingredientAL = new ArrayList<>();
            for (int j = 0; j < recipeList[i].getIngredients().length; j++) {
                ingredientAL.add(recipeList[i].getIngredients()[j].getName());
            }

            Log.d("Printed Array", "" + ingredientAL.toString());

            //Don't want to display more than 3 ingredients, show click for more...
            //Old code to handle list view
//            if(recipeList[i].getIngredients().length<= 3){
//                for (int j = 0; j < recipeList[i].getIngredients().length; j++) {
//                    ingredientAL.add(recipeList[i].getIngredients()[j].getName());
//                }
//            }
//            else{
//                for (int j = 0; j < 3; j++) {
//                    ingredientAL.add(recipeList[i].getIngredients()[j].getName());
//                    if(j == 2){
//                        ingredientAL.add("...Click for more");
//                    }
//                }
//            }
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, R.layout.small_list_view_text, ingredientAL);
//            p.recipeIngredients.setAdapter(adapter);

            String ings = "";
            for (int h = 0; h < recipeList[i].getIngredients().length; h++) {
                String n = recipeList[i].getIngredients()[h].getName();
                if (h != recipeList[i].getIngredients().length - 1) {
                    ings += n + ", ";
                } else {
                    ings += n;
                }


            }


            if (ings.length() < 115) {
                p.recipeIngredients.setText(ings);
            } else {
                p.recipeIngredients.setText(ings.substring(0, 115) + " ...");
            }


            //Set Recipe Image
            String url = recipeList[i].getImage();
            if (url != null && url.length()>0) {
                Picasso.get().load(url).into(p.recipeImage);
            }
            else{
                p.recipeImage.setImageResource(R.drawable.ic_cloud_off_black_24dp);

            }


        }


        //Bookmark Logic
        p.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (p.bookmark.getTag().toString()) {
                    case "" + R.drawable.ic_bookmark_blank_24dp:
                        p.bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                        p.bookmark.setTag(R.drawable.ic_bookmark_black_24dp);
                        break;
                    case "" + R.drawable.ic_bookmark_black_24dp:
                    default:
                        p.bookmark.setImageResource(R.drawable.ic_bookmark_blank_24dp);
                        p.bookmark.setTag(R.drawable.ic_bookmark_blank_24dp);
                        break;
                }

            }
        });

    }

    @Override
    public recyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.recipe_card, viewGroup, false);
        itemView.invalidate();


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("url", recipeList[i].getFoodurl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.food.com/" + recipeList[i].getId()));
                c.startActivity(browserIntent);
            }
        });

        return new recyclerViewHolder(itemView);
    }

    public static class recyclerViewHolder extends RecyclerView.ViewHolder {

        protected TextView recipeName;
        protected ImageView recipeImage;
        protected TextView recipeIngredients;
        protected ImageView bookmark;

        public recyclerViewHolder(View v) {
            super(v);

            recipeName = v.findViewById(R.id.recipeName);
            recipeImage = v.findViewById(R.id.recipeImage);
            recipeIngredients = v.findViewById(R.id.recipeIngredients);
            bookmark = v.findViewById(R.id.bookmark);
            bookmark.setTag(R.drawable.ic_bookmark_blank_24dp);


        }


    }
}