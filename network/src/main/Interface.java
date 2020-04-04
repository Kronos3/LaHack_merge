package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Interface {
    private String server_address;

    public Interface(String serverAddress) {
        this.server_address = serverAddress;
    }

    public JSONObject getRequest (String api_call) {
        String url_str = String.format("%s/api/%s", this.server_address, api_call);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url_str))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JSONParser parser = new JSONParser();
            return (JSONObject)parser.parse(response.body());
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Not parse JSON string");
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject postRequest(String api_call, String query_string) {
        String url_str = String.format("%s/api/%s/?%s", this.server_address, api_call, query_string);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url_str))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JSONParser parser = new JSONParser();
            return (JSONObject)parser.parse(response.body());
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Not parse JSON string");
            e.printStackTrace();
        }

        return null;
    }

    public String getPath(String path) {
        return String.format("%s/%s", this.server_address, path);
    }

    /**
     * Send a oauth2 login request to the backend.
     * This just returns the site that should be loaded
     * to begin the oauth.
     *
     * Once the user is redirected to the homepage, this dialog should
     * close and the login_meta function should be called.
     *
     * @return the URL that should be loaded for authentication
     */
    public String login() {
        return "https://ingredible.tech/oauth/";
    }

    /**
     * Search for recipes given an ingredient
     * @return a pollable object with nothing initially polled
     */
    public Search search(ArrayList<String> toks) {


        JSONObject response = this.postRequest("search/start/i", "search=a,c,b,d");

        assert response != null;
        return new Search(this, (String)response.get("search_id"));
    }

    /**
     * Search for recipes given a tag
     * @return a pollable object with nothing initially polled
     */
    public Search searchTags(ArrayList<Tag> tags) {
        //TODO
        return null;
    }

    /**
     * Search for recipes given keywords
     * @return a pollable object with nothing initially polled
     */
    public Search searchRecipes(String search) {
        //TODO
        return null;
    }
}
