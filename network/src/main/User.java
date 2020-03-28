package main;

import java.util.ArrayList;

public class User {
    private String email;
    private String name;

    private String picture;
    private ArrayList<Recipe> recipes;

    public static User getCurrentUser(Interface ) {

    }

    public User (String email, String name, String picture, ArrayList<Recipe> recipes) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.recipes = recipes;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }
}
