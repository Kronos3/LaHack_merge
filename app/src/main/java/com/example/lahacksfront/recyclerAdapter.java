package com.example.lahacksfront;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

    }

    @Override
    public int getItemCount() {
        return recipeList.length;
    }

    @Override
    public void onBindViewHolder(recyclerViewHolder p, int i) {

        if (recipeList[i] != null) {
            p.recipeName.setText(recipeList[i].getName());
            ArrayList<String> ingredientAL = new ArrayList<>();


            Log.d("length ", "" + recipeList[i].getIngredients().length);
            if(recipeList[i].getIngredients().length<= 3){
                for (int j = 0; j < recipeList[i].getIngredients().length; j++) {
                    ingredientAL.add(recipeList[i].getIngredients()[j].getName());
                }
            }
            else{
                for (int j = 0; j < 3; j++) {
                    ingredientAL.add(recipeList[i].getIngredients()[j].getName());
                    if(j == 2){
                        ingredientAL.add("...Click for more");
                    }
                }
            }

            Log.d("array ", "" + Arrays.toString(recipeList[i].getIngredients()));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, R.layout.small_list_view_text, ingredientAL);
            p.recipeIngredients.setAdapter(adapter);

            p.recipeImage.setBackgroundResource(R.drawable.ic_launcher_background);

        }


    }

    @Override
    public recyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.recipe_card, viewGroup, false);

        return new recyclerViewHolder(itemView);
    }

    public static class recyclerViewHolder extends RecyclerView.ViewHolder {

        protected TextView recipeName;
        protected ImageView recipeImage;
        protected ListView recipeIngredients;

        public recyclerViewHolder(View v) {
            super(v);

            recipeName = v.findViewById(R.id.recipeName);
            recipeImage = v.findViewById(R.id.recipeImage);
            recipeIngredients = v.findViewById(R.id.recipeIngredients);


        }


    }
}