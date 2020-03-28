package com.example.lahacksfront;

import java.util.ArrayList;

public class Recipe {
    private String name;
    private Ingredient[] ingredients;

    public Recipe(String n, Ingredient[] i){
        name = n;
        ingredients = i;
    }

    public String getName() {
        return name;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }






}
