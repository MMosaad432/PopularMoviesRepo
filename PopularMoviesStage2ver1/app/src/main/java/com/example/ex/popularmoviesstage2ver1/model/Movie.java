package com.example.ex.popularmoviesstage2ver1.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Movie implements Parcelable {
    private final int id;
    private final String original_title;
    private final String poster_path;
    private final String overview;
    private final Double vote_average;
    private final String release_date;
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(int id, String original_title, String poster_path, String overview, Double vote_average, String release_date) {
        this.id = id;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public int getId() {
        return this.id;
    }

    public String getOriginal_title() {
        return this.original_title;
    }

    public String getPoster_path() {
        return this.poster_path;
    }

    public String getOverview() {
        return this.overview;
    }

    public Double getVote_average() {
        return this.vote_average;
    }

    public String getRelease_date() {
        return this.release_date;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeValue(this.vote_average);
        dest.writeString(this.release_date);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.original_title = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.vote_average = (Double)in.readValue(Double.class.getClassLoader());
        this.release_date = in.readString();
    }
}
