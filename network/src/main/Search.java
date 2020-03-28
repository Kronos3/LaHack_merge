package main;

import org.json.simple.JSONObject;

public class Search extends Pollable<Search, Recipe> {
    private String searchId;
    private Interface i;

    /**
     * Create a new pollable search
     * @param parent Parent interface from where we make requests
     * @param search_id the search id of this search
     */
    public Search(Interface parent, String search_id) {
        super(Search::recv);
        this.i = parent;
        this.searchId = search_id;
    }

    public static Recipe recv(Search parent) {
        JSONObject obj = parent.i.getRequest(String.format("search/poll/%s", parent.searchId));
        return new Recipe(obj);
    }
}
