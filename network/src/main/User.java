package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class User {

    private ArrayList<Recipe> recipes;

    public static User getCurrentUser(Interface parent) {
        JSONObject res = parent.getRequest("login-meta");

        //return new User(res);
        return null;
    }
/**
    public User (JSONObject obj) {
        this.email = (String)obj.get("email");
        this.name = (String)obj.get("name");
        this.picture = (String)obj.get("picture");

        this.recipes = new ArrayList<>();

        for (Object r : (JSONArray)obj.get("recipes")) {
            this.recipes.add(new Recipe((JSONObject) r));
        }
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
 */
}
