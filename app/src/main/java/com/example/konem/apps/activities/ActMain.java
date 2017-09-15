package com.example.konem.apps.activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.konem.apps.R;
import com.example.konem.apps.StoryAdapter;
import com.example.konem.apps.helpers.EndlessRecyclerOnScrollListener;
import com.example.konem.apps.model.AppDatabase;
import com.example.konem.apps.model.Story;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import java.util.List;


@EActivity(R.layout.activity_act_main)
public class ActMain extends AppCompatActivity {
    private List<String> mStringList;
    private int mLoadedItems = 0;
    private StoryAdapter mSampleAdapter;
    private AppDatabase mDb;
    private static final String TAG = ActMain_.class.getSimpleName();

    @AfterViews
    void start() {
        mDb = AppDatabase.getDiskDatabase(getApplicationContext());
        RecyclerView rvItems = (RecyclerView) findViewById(R.id.recycleView);

        final List<Story> allStory = mDb.storyModel().getFirst10();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItems.setLayoutManager(linearLayoutManager);
        final StoryAdapter adapter = new StoryAdapter(allStory);
        rvItems.setAdapter(adapter);
        EndlessRecyclerOnScrollListener scrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                List<Story> moreStory = Contact.createContactsList(10, page);
                Log.d(TAG, "onLoadMore: page: " + page );
                List<Story> moreStory = mDb.storyModel().getMore(page*12);
                int curSize = adapter.getItemCount();

                allStory.addAll(moreStory);

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(curSize, allStory.size() - 1);
                    }
                });
            }
        };
        rvItems.addOnScrollListener(scrollListener);
    }
}
