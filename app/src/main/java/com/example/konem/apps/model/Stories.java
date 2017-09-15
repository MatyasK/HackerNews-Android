package com.example.konem.apps.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by konem on 12/09/2017.
 */
@Entity(tableName = "Stories")
public class Stories {
    public @PrimaryKey int ids;

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }
}
