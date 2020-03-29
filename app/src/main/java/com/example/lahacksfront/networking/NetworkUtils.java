package com.example.lahacksfront.networking;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.lahacksfront.Recipe;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {
    private Activity con;
    private OkHttpClient client;
    private String url;

    private Object returnObject;
    private Semaphore locker;
    private Gson gson;

    public NetworkUtils() {
        client = new OkHttpClient();
        url = "https://ingredible.tech/api/";
        locker = new Semaphore(1);
        gson = new Gson();
    }

    private void sendRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        sendRawRequest(request, callback);
    }

    private void sendRawRequest(Request request, Callback callback) {

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

    public ArrayList<Recipe> userRecipes_remove(String recipe_id) {
        String user_token = "user";

        sendRequest(String.format("%suser_data/?user_token=%s&recipe_id=%s&action=remove", url, user_token, recipe_id), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Recipe[] recipes = gson.fromJson(response.body().string(), Recipe[].class);
                    returnObject = new ArrayList<>(Arrays.asList(recipes));

                    locker.release();
                }
            }
        });

        return (ArrayList<Recipe>) returnObject;
    }

    public String start_process(String process_id) {
        sendRequest(String.format("%sprocess/get/%s/", url, process_id), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    returnObject = response.body().string();
                    returnObject = ((String) returnObject).substring(((String) returnObject).indexOf(" ") + 1, ((String) returnObject).length() - 1);

                    locker.release();
                }
            }
        });

        return (String) returnObject;
    }

    public String upload_file(ArrayList<Bitmap> images) {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (int i = 0; i < images.size(); i++) {
            String name = String.format("image%d", i);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            images.get(i).compress(Bitmap.CompressFormat.PNG, 90, stream);

            byte[] byteArray = stream.toByteArray();

            requestBodyBuilder.addFormDataPart("file", name, RequestBody.create(MediaType.parse("image/png"), byteArray));
        }

        RequestBody requestBody = requestBodyBuilder.build();

        Request request = new Request.Builder().url(url + "process/start/")
                .post(requestBody).build();

        sendRawRequest(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {}

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    returnObject = response.body().string();
                    returnObject = ((String) returnObject).substring(((String) returnObject).indexOf(" ") + 1, ((String) returnObject).length() - 1);

                    locker.release();
                }
            }
        });

        return (String)returnObject;
    }

    public ArrayList<Recipe> userRecipes_add(String recipe_id) {
        String user_token = "user";

        sendRequest(String.format("%suser_data/?user_token=%s&recipe_id=%s&action=add", url, user_token, recipe_id), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Recipe[] recipes = gson.fromJson(response.body().string(), Recipe[].class);
                    returnObject = new ArrayList<>(Arrays.asList(recipes));

                    locker.release();
                }
            }
        });

        return (ArrayList<Recipe>) returnObject;
    }

    public ArrayList<Recipe> userRecipes() {
        String user_token = "user";

        sendRequest(url + "user_data/?user_token=" + user_token, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Recipe[] recipes = gson.fromJson(response.body().string(), Recipe[].class);
                    returnObject = new ArrayList<>(Arrays.asList(recipes));

                    locker.release();
                }
            }
        });

        return (ArrayList<Recipe>) returnObject;
    }

    public String getNewUserId() {
        sendRequest(url + "user_data/request/", new Callback() {
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

        return (String) returnObject;
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

        return (String) returnObject;
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
                    returnObject = gson.fromJson(response.body().string(), Recipe.class);
                    locker.release();
                }
            }
        });

        return (Recipe) returnObject;
    }
}
