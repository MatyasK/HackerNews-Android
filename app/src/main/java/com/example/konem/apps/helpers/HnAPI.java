package com.example.konem.apps.helpers;
import com.example.konem.apps.model.Stories;
import com.example.konem.apps.model.Story;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by konem on 12/09/2017.
 */

public interface HnAPI {

    @GET("item/{id}.json?print=pretty")
    Call<Story> getStory(@Path("id") String id);

}
