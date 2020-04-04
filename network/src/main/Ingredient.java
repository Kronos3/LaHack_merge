package main;

import org.json.simple.JSONObject;

public class Ingredient extends Requestable {
    private String name;

    public Ingredient(JSONObject object) {
        super(object);
        this.name = (String)this.get_obj().get("name");
    }

    public String toString() {
        return this.name;
    }
}
