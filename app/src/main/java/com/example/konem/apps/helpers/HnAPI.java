package com.example.konem.apps.helpers;
import com.example.konem.apps.model.Comment;
import com.example.konem.apps.model.Story;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by konem on 12/09/2017.
 */

public interface HnAPI {

    @GET("item/{id}.json?print=pretty")
    Call<Story> getStory(@Path("id") String id);

    @GET("item/{id}.json?print=pretty")
    Call<Comment> getComment(@Path("id") String id);
}
