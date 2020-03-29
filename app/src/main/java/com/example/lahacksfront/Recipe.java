package com.example.lahacksfront;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;

public class Recipe {

    private String name;
    private Ingredient[] ingredients;
    private String image;
    private String id;


    public Recipe(String n, Ingredient[] i){
        name = n;
        ingredients = i;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "name:" + name + " ingreidents: " + Arrays.toString(ingredients) + " image: " + image + " id: " + id;
    }
}
