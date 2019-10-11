package com.example.ex.popularmoviesstage2ver1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.ex.popularmoviesstage2ver1.database.AppDatabase;
import com.example.ex.popularmoviesstage2ver1.database.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private LiveData<List<Integer>> moviesId;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        moviesId = database.movieDao().loadAllMoviesId();
    }

    public LiveData<List<Integer>> getMovies() {
        return moviesId;
    }
}