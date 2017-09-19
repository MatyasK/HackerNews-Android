package com.example.konem.apps.model;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by konem on 19/09/2017.
 */

//TypeConverter to store list of comment IDs in the Room.
public class ListConverter {
    @TypeConverter
    public static List<Integer> stringToList(String value){
        List<String> comments = Arrays.asList((value.split("\\s*,\\s*")));
        List<Integer> reply = new ArrayList<>();
        for (String comment : comments) {
            if (!comment.equals("")) {
               reply.add(Integer.valueOf(comment.toString()));
            }
        }
        return reply;
    }

    @TypeConverter
    public static String listToString(List<Integer> kids){
        String value = "";
        if (kids != null) {
            for (Integer kid : kids) {
                value += kid + ",";
            }
        }
        return value;
    }
}
