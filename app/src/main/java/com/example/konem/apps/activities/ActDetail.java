package com.example.konem.apps.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.konem.apps.Adapters.CommentAdapter;
import com.example.konem.apps.R;
import com.example.konem.apps.helpers.HnAPI;
import com.example.konem.apps.helpers.RetroFitClient;
import com.example.konem.apps.model.AppDatabase;
import com.example.konem.apps.model.Comment;
import com.example.konem.apps.model.Story;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;

@EActivity(R.layout.activity_act_detail)
public class ActDetail extends AppCompatActivity {
    private static final String TAG = ActDetail_.class.getSimpleName();
    private AppDatabase mDb;

    @Extra("story")
    int ID;

    @ViewById(R.id.det_title)
    TextView title;

    @ViewById(R.id.det_by)
    TextView by;

    @ViewById(R.id.det_open_btn)
    Button button;

    @ViewById(R.id.rvComments)
    RecyclerView recyclerView;

    @AfterViews
    void updateTitle() {
        mDb = AppDatabase.getDiskDatabase(getApplicationContext());
        HnAPI apiService = RetroFitClient.getClient().create(HnAPI.class);


        Story story = mDb.storyModel().getStoryByID(ID);
        title.setText(story.getTitle());
        by.setText(story.getBy());

        for (Integer integer : story.getKids()) {
            Log.d(TAG, "updateTitle: " + integer);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(story.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        //Download Comments
        ArrayList<Comment> comments = new ArrayList<>();
        CommentAdapter commentAdapter = new CommentAdapter(comments);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        for (int i = 0; i < story.getKids().size(); i++) {
            retrofit2.Call<Comment> call = apiService.getComment(story.getKids().get(i).toString());
            int finalI = i;
            call.enqueue(new retrofit2.Callback<Comment>() {
                @Override
                public void onResponse(retrofit2.Call<Comment> call, retrofit2.Response<Comment> response) {
                    if (response.code() == 200) {
                        comments.add(response.body());
                        commentAdapter.notifyDataSetChanged();

                        if (story.getKids().size() == finalI + 1) {
                            Log.d(TAG, "onResponse: DONE");
                        }
                    } else {
                        Log.d(TAG, "Something went wrong: " + response.code());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Comment> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }



    }
}