package com.example.ex.popularmoviesstage2ver1.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favoriteMovies")
public class MovieEntry {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String title;

    @Ignore
    public MovieEntry(String title) {
        this.title = title;
    }

    public MovieEntry(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


