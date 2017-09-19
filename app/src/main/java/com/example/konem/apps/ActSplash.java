package com.example.konem.apps;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.konem.apps.activities.ActMain_;
import com.example.konem.apps.helpers.HnAPI;
import com.example.konem.apps.helpers.RetroFitClient;
import com.example.konem.apps.model.AppDatabase;
import com.example.konem.apps.model.Story;


import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.AfterViews;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


@EActivity(R.layout.act_splash)
public class ActSplash extends AppCompatActivity {

    private AppDatabase mDb;
    private static final String TAG = ActSplash_.class.getSimpleName();

    @AfterViews
    void start() {
        mDb = AppDatabase.getDiskDatabase(getApplicationContext());

        ArrayList<Integer> IDs = new ArrayList<Integer>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty")
                .build();

        //Get latest story IDs from the API
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
                        IDs.add((Integer) array.get(i));
                    }
                    mDb.storyModel().nukeTable();
                    getStories(compareLists(IDs));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    /**
     * Compare locale data to the latest
     * @param newList the new IDS list
     * @return  the IDs which are not in localDatabase
     */
    public List<Integer> compareLists(ArrayList<Integer> newList){
        List<Integer> IdsFromLocal = mDb.storyModel().getIds();
        List<Integer> newStories = new ArrayList<Integer>(newList);
        newStories.removeAll(IdsFromLocal);
//        for (Integer newStory : newStories) {
//            Log.d(TAG, "compareLists: NEW IDS: " + newStory );
//        }
        if (newStories.size() == 0) {
            ActMain_.intent(getApplicationContext()).start();
        }
        return newStories;
    }


    public void getStories(List<Integer> idForDownload){
        HnAPI apiService = RetroFitClient.getClient().create(HnAPI.class);
        Log.d(TAG, "getStories: length: " + idForDownload.size());
        ArrayList<Story> resultList = new ArrayList<>();
        for (int i = 0; i < idForDownload.size(); i++){
            retrofit2.Call<Story> call = apiService.getStory(idForDownload.get(i).toString());
            int finalI = i;
            call.enqueue(new retrofit2.Callback<Story>() {
                @Override
                public void onResponse(retrofit2.Call<Story> call, retrofit2.Response<Story> response) {
                    if (response.code() == 200) {
                        resultList.add(response.body());
                        if (idForDownload.size() == finalI+1) {
                            Log.d(TAG, "onResponse: DONE");
                            OrderResult(idForDownload, resultList);
//                            ActMain_.intent(getApplicationContext()).start();
                        }
                    }else {
                        Log.d(TAG,"Something went wrong: " + response.code());
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<Story> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }
    }


    public void OrderResult(List<Integer> IDs, List<Story> stories){
        List<Story> finalResult = new ArrayList<>();
        for (Integer id : IDs) {
            for (Story story : stories) {
//                Log.d(TAG, "OrderResult: OrigID:" + id + ", resultId " + story.getId());
                if (id.equals(story.getId())){
                    finalResult.add(story);
                    break;
                }
            }
        }
//        for (int i = 0; i < finalResult.size(); i++) {
//            Log.d(TAG, "OrderResult: origID: " + IDs.get(i) + " :: " + finalResult.get(i).getId());
//        }
        mDb.storyModel().insertAll(finalResult);


        List<Story> fromDatabase = mDb.storyModel().getAllStory();
        Log.d(TAG, "OrderResult: size: "  + fromDatabase.size());
//        for (int i = 0; i < fromDatabase.size(); i++) {
//            Log.d(TAG, "OrderResult: origID: " + IDs.get(i) + " :: " + fromDatabase.get(i).getId());
//        }
        ActMain_.intent(getApplicationContext()).start();
    }
}
