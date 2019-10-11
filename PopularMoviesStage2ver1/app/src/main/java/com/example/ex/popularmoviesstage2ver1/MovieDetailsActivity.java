package com.example.ex.popularmoviesstage2ver1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ex.popularmoviesstage2ver1.TrailersRecyclerViewAdapter.trailerRecyclerViewAdapterOnClickHandler;
import com.example.ex.popularmoviesstage2ver1.database.AppDatabase;
import com.example.ex.popularmoviesstage2ver1.database.MovieEntry;
import com.example.ex.popularmoviesstage2ver1.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements trailerRecyclerViewAdapterOnClickHandler {
    RecyclerView mReviewsRecyclerView;
    RecyclerView mTrailersRecyclerView;
    ReviewsRecyclerViewAdapter mReviewsRecyclerViewAdapter;
    TrailersRecyclerViewAdapter mTrailersRecyclerViewAdapter;
    ArrayList<Integer> moviesId;
    AppDatabase mDb;
    Movie movie;
    boolean found = false;
    ImageView favImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_movie_details);
        moviesId = new ArrayList<>();
        mDb = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();


        this.mReviewsRecyclerView = (RecyclerView)this.findViewById(R.id.reviewsRecyclerView);
        this.mTrailersRecyclerView = (RecyclerView)this.findViewById(R.id.trailerRecyclerView);
        final ImageView movieImageView = (ImageView)this.findViewById(R.id.iv_movie);
        favImageView = (ImageView)this.findViewById(R.id.favImageView);
        TextView titleTv = (TextView)this.findViewById(R.id.tv_title);
        TextView releaseDateTv = (TextView)this.findViewById(R.id.releaseDate);
        TextView rateTv = (TextView)this.findViewById(R.id.rate);
        final TextView overviewTv = (TextView)this.findViewById(R.id.tv_overview);
        final LinearLayout reviewsLinearLayout = (LinearLayout)this.findViewById(R.id.reviewsLinearLayout);
        final ImageView reviewsIcon = (ImageView)this.findViewById(R.id.reviewsIcon);
        final LinearLayout trailersLinearLayout = (LinearLayout)this.findViewById(R.id.trailersLinearLayout);
        final ImageView trailersIcon = (ImageView)this.findViewById(R.id.trailerIcon);
        final LinearLayout overviewLinearLayout = (LinearLayout)this.findViewById(R.id.overviewLinearLayout);
        final ImageView overviewIcon = (ImageView)this.findViewById(R.id.overviewIcon);
        final ScrollView scrollViewParent = (ScrollView)this.findViewById(R.id.scrollViewParent);
        final ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.pbIV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.mReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        this.mReviewsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        this.mTrailersRecyclerView.setLayoutManager(linearLayoutManager1);
        this.mTrailersRecyclerView.setHasFixedSize(true);

        progressBar.setVisibility(View.VISIBLE);
        movie = (Movie)this.getIntent().getParcelableExtra("key");
        int id = movie.getId();
        this.mReviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(id);
        this.mReviewsRecyclerView.setAdapter(this.mReviewsRecyclerViewAdapter);
        this.mTrailersRecyclerViewAdapter = new TrailersRecyclerViewAdapter(this, this, id);
        this.mTrailersRecyclerView.setAdapter(this.mTrailersRecyclerViewAdapter);
        String originalTitle = movie.getOriginal_title();
        final String overview = movie.getOverview();
        String posterPath = movie.getPoster_path();
        Double voteAverage = movie.getVote_average();
        String releaseDate = movie.getRelease_date();
        String rate = voteAverage.toString() + "/10";
        String url = "http://image.tmdb.org/t/p/w500/" + posterPath;
        titleTv.setText(originalTitle);

        if (this.checkInternetConnection()) {
            Picasso.with(this).load(url).into(movieImageView, new Callback() {
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                public void onError() {
                    Picasso.with(MovieDetailsActivity.this).load(R.drawable.download).into(movieImageView);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Picasso.with(this).load(R.drawable.download).into(movieImageView, new Callback() {
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                public void onError() {
                }
            });
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

        releaseDateTv.setText(releaseDate);
        rateTv.setText(rate);
        overviewTv.setText(overview);
        favImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        LinearLayout parent = (LinearLayout)this.findViewById(R.id.parent);
        parent.getLayoutTransition().enableTransitionType(4);
        reviewsLinearLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LayoutParams params = MovieDetailsActivity.this.mReviewsRecyclerView.getLayoutParams();
                params.height = params.height == 0 ? LayoutParams.WRAP_CONTENT : 0;
                if (params.height == 0) {
                    reviewsIcon.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                    MovieDetailsActivity.this.scrollToView(scrollViewParent, movieImageView);
                } else {
                    reviewsIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    MovieDetailsActivity.this.scrollToView(scrollViewParent, reviewsLinearLayout);
                }

                MovieDetailsActivity.this.mReviewsRecyclerView.setLayoutParams(params);
            }
        });
        LinearLayout parent1 = (LinearLayout)this.findViewById(R.id.parent1);
        parent1.getLayoutTransition().enableTransitionType(4);
        trailersLinearLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LayoutParams params = MovieDetailsActivity.this.mTrailersRecyclerView.getLayoutParams();
                params.height = params.height == 0 ? LayoutParams.WRAP_CONTENT: 0;
                if (params.height == 0) {
                    trailersIcon.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                    MovieDetailsActivity.this.scrollToView(scrollViewParent, movieImageView);
                } else {
                    trailersIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    MovieDetailsActivity.this.scrollToView(scrollViewParent, trailersLinearLayout);
                }

                MovieDetailsActivity.this.mTrailersRecyclerView.setLayoutParams(params);
            }
        });
        LinearLayout parent3 = (LinearLayout)this.findViewById(R.id.parent3);
        parent3.getLayoutTransition().enableTransitionType(4);
        overviewLinearLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LayoutParams params = overviewTv.getLayoutParams();
                params.height = params.height == 0 ? LayoutParams.WRAP_CONTENT: 0;
                if (params.height == 0) {
                    overviewIcon.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                    MovieDetailsActivity.this.scrollToView(scrollViewParent, movieImageView);
                } else {
                    overviewIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    MovieDetailsActivity.this.scrollToView(scrollViewParent, overviewLinearLayout);
                }

                overviewTv.setLayoutParams(params);
            }
        });

        favImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (found){
                    found = false;
                    favImageView.setImageResource(R.drawable.ic_star_border_64dp);
                    final MovieEntry movieEntry = new MovieEntry(movie.getId(),movie.getOriginal_title());
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            mDb.movieDao().deleteMovie(movieEntry);
                        }
                    });
                }
                else{
                    found =true;
                    favImageView.setImageResource(R.drawable.ic_star_64dp);
                    final MovieEntry movieEntry = new MovieEntry(movie.getId(),movie.getOriginal_title());
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            mDb.movieDao().insertMovie(movieEntry);
                        }
                    });
                }
            }
        });
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(@Nullable List<Integer> movieEntries) {
                Log.d("DADF", "Updating list of tasks from LiveData in ViewModel");
                MovieDetailsActivity.this.moviesId = new ArrayList<Integer>(movieEntries);
                if (!moviesId.isEmpty()) {
                    for (int i = 0; i < moviesId.size(); i++) {
                        if (moviesId.get(i) != movie.getId()) {
                            found = false;
                            continue;
                        } else {
                            found = true;
                            break;
                        }
                    }
                }else
                    found=false;

                if (found){

                    favImageView.setImageResource(R.drawable.ic_star_64dp);
                }else
                    favImageView.setImageResource(R.drawable.ic_star_border_64dp);
            }
        });
    }
    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    private void scrollToView(ScrollView scrollViewParent, View view) {
        Point childOffset = new Point();
        this.getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        scrollViewParent.smoothScrollTo(childOffset.x, childOffset.y);
    }

    private void getDeepChildOffset(ViewGroup mainParent, ViewParent parent, View child, Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup)parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (!parentGroup.equals(mainParent)) {
            this.getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
        }
    }

    public void onClickListener(String clickedItem) {
        this.openWebPage("https://www.youtube.com/watch?v=" + clickedItem);
    }

    private void openWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(intent);
        }

    }
}
