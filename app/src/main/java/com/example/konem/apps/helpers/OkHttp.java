package com.example.konem.apps.helpers;

import android.renderscript.Sampler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by konem on 13/09/2017.
 */

public class OkHttp {
    private static OkHttpClient client = new OkHttpClient();
    private static final String TAG = "OkHTTPcLass";

    public static ArrayList getTopStories(){
        final ArrayList<Integer> IDs = new ArrayList<Integer>();
        Request request = new Request.Builder()
                .url("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONArray array = new JSONArray(responseData);
                    for (int i = 0; i < array.length(); i++){
                        IDs.add(Integer.parseInt(array.get(i).toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
       return IDs;
    }

}
