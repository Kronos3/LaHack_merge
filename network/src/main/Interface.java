package main;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Interface {
    private String server_address;

    public Interface(String serverAddress) {
        this.server_address = serverAddress;
    }

    private JSONObject sendRequest(String api_call) {
        String url_str = String.format("%s/api/%s", this.server_address, api_call);

        try {
            URL url = new URL(url_str);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            con.connect();
            
        }
        catch (java.net.MalformedURLException e) {
            e.printStackTrace();
            System.err.printf("Invalid url '%s'\n", url_str);
        }
        catch (java.net.ProtocolException e) {
            e.printStackTrace();
            System.err.printf("Protocol error for '%s'\n", url_str);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.printf("Could not connect to '%s'\n", url_str);
        }
    }

    /**
     * Send a oauth2 login request to the backend
     * @return the URL that should be loaded for authentication
     */
    public String login() {
        //TODO
        return null;
    }

    /**
     * Search for recipes given an ingredient
     * @return a pollable object with nothing initially polled
     */
    public Pollable<Recipe> searchIngredient(Ingredient ing) {
        //TODO
        return null;
    }

    /**
     * Search for recipes given a tag
     * @return a pollable object with nothing initially polled
     */
    public Pollable<Recipe> searchTag(Tag tag) {
        //TODO
        return null;
    }

    /**
     * Search for recipes given keywords
     * @return a pollable object with nothing initially polled
     */
    public Pollable<Recipe> searchRecipe(String search) {
        //TODO
        return null;
    }
}
