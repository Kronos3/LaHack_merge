package main;

import org.json.simple.JSONObject;

public abstract class Requestable {
    private JSONObject obj;

    public Requestable (JSONObject obj) {
        this.obj = obj;
    }

    protected JSONObject get_obj() {
        return this.obj;
    }
}
