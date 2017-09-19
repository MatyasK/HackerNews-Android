package com.example.konem.apps.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by konem on 14/09/2017.
 */
@Dao
public interface StoryDAO {

    @Query("select * from story")
    List<Story> getAllStory();

    @Insert
    void insertAll(List<Story> stories);

    @Query("select Story.id from story")
    List<Integer> getIds();

    @Query("select * from story limit 12")
    List<Story> getFirst10();


    @Query("select * from story limit 11 offset :page")
    List<Story> getMore(int page);

    @Query("select * from story where id = :id")
    Story getStoryByID(int id);

    @Insert(onConflict = IGNORE)
    void insertStory(Story story);

    @Query("DELETE FROM story")
    public void nukeTable();
}
