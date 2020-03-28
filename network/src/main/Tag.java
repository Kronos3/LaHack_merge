package main;

import org.json.simple.JSONObject;

public class Tag extends Requestable {
    private String name;

    public Tag(JSONObject object) {
        super(object);
        this.name = (String)this.get_obj().get("name");
    }



    public String toString() {
        return this.name;
    }
}
