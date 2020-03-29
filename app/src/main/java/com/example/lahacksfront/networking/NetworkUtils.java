package com.example.lahacksfront.networking;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.lahacksfront.Recipe;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {
    private Activity con;
    private OkHttpClient client;
    private String url;

    private Object returnObject;
    private Semaphore locker;

    public NetworkUtils () {
        client = new OkHttpClient();
        url = "https://ingredible.tech/api/";
        locker = new Semaphore(1);
    }

    private void sendRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                //Temporary Test
                .url(url)
                .build();

        try {
            locker.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.newCall(request).enqueue(callback);

        try {
            locker.acquire();
            locker.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Recipe> userRecipes() {
        ArrayList<Recipe> out = new ArrayList<>();

        return out;
    }

    public String searchId(String query) throws IOException {
        Log.d("query", "called Search Function");

        this.sendRequest(url + "search/start/k/?search=" + query, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    returnObject = response.body().string();
                    locker.release();
                }
            }
        });

        return (String)returnObject;
    }

    public Recipe getRecipe(String search_id) {
        String request_url = String.format("%ssearch/poll/%s/", this.url, search_id);
        this.sendRequest(request_url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    returnObject = parseRecipeJson(response.body().string());
                    locker.release();
                }
            }
        });

        return (Recipe) returnObject;
    }

    private static Recipe parseRecipeJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Recipe.class);
    }
}
