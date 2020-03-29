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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private Activity con;
    private OkHttpClient client = new OkHttpClient();
    private String url = "https://ingredible.tech/api/";


    private String searchedValue;
    private Recipe returnedRecipe;


    public Recipe getReturnedRecipe() {
        return returnedRecipe;
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

    public void getRecipes(String id, Recipe[] recipes, int index) {

        Request request = new Request.Builder()
                //Temporary Test
                .url(url + "search/poll/test/?")
                .build();

        final Recipe[] myResponse = new Recipe[1];

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    Log.d("here", "getRecipeOnReponse");
                   // Log.d("here", "" + parseRecipeJson(response.body().string().toString()));
                   // returnedRecipe = parseRecipeJson(response.body().string());
                    recipes[index]= parseRecipeJson(response.body().string());

                }
            }


        });

        //return myResponse[0];

    }

    public Recipe parseRecipeJson(String json) {
        Gson gson = new Gson();
        Recipe entity = gson.fromJson(json, Recipe.class);

        return entity;

    }
}
