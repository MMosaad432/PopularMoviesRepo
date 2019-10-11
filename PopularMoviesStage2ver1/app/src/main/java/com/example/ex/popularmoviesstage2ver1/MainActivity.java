package com.example.ex.popularmoviesstage2ver1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.ex.popularmoviesstage2ver1.database.AppDatabase;
import com.example.ex.popularmoviesstage2ver1.database.MovieEntry;
import com.example.ex.popularmoviesstage2ver1.model.Movie;
import com.example.ex.popularmoviesstage2ver1.utils.JsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterOnClickHandler{
    private final String LAYOUT_ID = "layoutID";
    private int layoutId ;
    private final int SPAN_COUNT=2;
    private final String POPULARITY_URL="http://api.themoviedb.org/3/movie/popular?api_key=40d054e2ebafc9c504cb9e1a58dfd9d8";
    private final String TOP_RATES_URL="http://api.themoviedb.org/3/movie/top_rated?api_key=40d054e2ebafc9c504cb9e1a58dfd9d8";
    private final String INTERNET_CONNECTION="No internet connection!";

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    private JSONArray results;
    ProgressBar progressBar;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutId = R.id.popularity;
        if (savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_ID)) {
            layoutId = savedInstanceState.getInt(LAYOUT_ID, 0);
        }
        if(checkInternetConnection()) {
            progressBar = (ProgressBar) findViewById(R.id.pb);
            mRecyclerView = findViewById(R.id.rv_movies);
            if (layoutId == R.id.popularity) {
                mRecyclerViewAdapter = new RecyclerViewAdapter(this, this, POPULARITY_URL, progressBar);
            }else if (layoutId == R.id.top_rated){
                mRecyclerViewAdapter = new RecyclerViewAdapter(this,this,TOP_RATES_URL,progressBar);
            }else {
                mDb = AppDatabase.getInstance(getApplicationContext());
                setupViewModel();
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT, GridLayout.VERTICAL, false);

            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setHasFixedSize(true);


            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        }
        else Toast.makeText(this,INTERNET_CONNECTION,Toast.LENGTH_SHORT).show();

    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(@Nullable List<Integer> movieEntries) {

                mRecyclerViewAdapter.setTasks(new ArrayList<Integer>(movieEntries));
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            }
        });
    }
    @Override
    public void onClickListener(int clickedItem) {
        Movie movie;
        Intent startMovieDetailsActivity;

        movie = JsonUtils.parseMovieJson(results, clickedItem);
        startMovieDetailsActivity = new Intent(MainActivity.this, MovieDetailsActivity.class);
        startMovieDetailsActivity.putExtra("key", movie);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(MainActivity.this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(startMovieDetailsActivity,bundle);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.popularity){
            layoutId = R.id.popularity;
            if (checkInternetConnection()) {
                mRecyclerViewAdapter = new RecyclerViewAdapter(this,this,POPULARITY_URL,progressBar);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            }else {
                Toast.makeText(this,INTERNET_CONNECTION,Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId()==R.id.top_rated){
            layoutId = R.id.top_rated;
            if (checkInternetConnection()) {
                mRecyclerViewAdapter = new RecyclerViewAdapter(this,this,TOP_RATES_URL,progressBar);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            }else {
                Toast.makeText(this,INTERNET_CONNECTION,Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId()==R.id.favorites){
            layoutId = R.id.favorites;
            if (checkInternetConnection()) {
                mDb = AppDatabase.getInstance(getApplicationContext());
                setupViewModel();
            }else {
                Toast.makeText(this,INTERNET_CONNECTION,Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected();
    }
    public void setResults(JSONArray result){
        this.results = result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAYOUT_ID,layoutId);
        super.onSaveInstanceState(outState);
    }
}