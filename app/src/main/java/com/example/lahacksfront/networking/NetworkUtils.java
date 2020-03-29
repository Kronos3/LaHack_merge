package com.example.lahacksfront.networking;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.lahacksfront.Recipe;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

    private Recipe returnedRecipe;
    private Semaphore locker;

    public NetworkUtils () {
        client = new OkHttpClient();
        url = "https://ingredible.tech/api/";
        locker = new Semaphore(1);
    }

    public String searchId(String query) throws IOException {
        Log.d("query", "called Search Function");
        Request request = new Request.Builder()
                .url(url + "search/start/k/?search=" + query)
                .build();


        try (Response r = client.newCall(request).execute()) {
            return r.body().string();
        }


//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.d("query", "failure");
//                e.printStackTrace();
//
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//                myResponse[0] = response.body().string();
//
//            }
//
//
//        });
    }

    public Recipe getRecipe(String search_id) {

        Request request = new Request.Builder()
                //Temporary Test
                .url(String.format("%ssearch/poll/%s/", this.url, search_id))
                .build();

        try {
            locker.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("here", "getRecipeOnReponse");
                    returnedRecipe = parseRecipeJson(response.body().string());
                    locker.release();
                }
            }
        });

        try {
            locker.acquire();
            locker.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return returnedRecipe;
    }

    private static Recipe parseRecipeJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Recipe.class);

    }
}
