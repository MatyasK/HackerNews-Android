package com.example.konem.apps.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.konem.apps.R;
import com.example.konem.apps.model.Story;

import java.util.List;

/**
 * Created by konem on 14/09/2017.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private List<Story> storyList;

    public StoryAdapter(List<Story> storyList) {
        this.storyList = storyList;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleTxtV;
        public TextView authorTextV;
        public TextView scoreTextV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleTxtV = itemView.findViewById(R.id.storyTitle);
            authorTextV = itemView.findViewById(R.id.storyAuthor);
            scoreTextV = itemView.findViewById(R.id.storyScore);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View storyView = inflater.inflate(R.layout.one_story_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(storyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Story story = storyList.get(position);

        TextView title = viewHolder.titleTxtV;
        TextView author = viewHolder.authorTextV;
        TextView score = viewHolder.scoreTextV;

        title.setText(story.getTitle());
        author.setText(story.getBy());
        score.setText(story.getScore().toString());

    }


    @Override
    public int getItemCount() {
        return storyList.size();
    }


}
