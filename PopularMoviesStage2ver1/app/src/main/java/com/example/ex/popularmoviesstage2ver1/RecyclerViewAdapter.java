package com.example.ex.popularmoviesstage2ver1;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.ex.popularmoviesstage2ver1.utils.JsonDownload;
import com.example.ex.popularmoviesstage2ver1.utils.JsonUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.RecyclerViewAdapterViewHolder> {
    private final String downloadURL;
    private List<String> paths;
    private final MainActivity mainActivity;
    private static JSONArray results;
    private final JsonDownload jsonDownload;
    ProgressBar progressBar;
    private ArrayList<Integer> moviesId;
    private final RecyclerViewAdapter.RecyclerViewAdapterOnClickHandler mRecyclerViewAdapterOnClickHandler;

    public RecyclerViewAdapter(RecyclerViewAdapter.RecyclerViewAdapterOnClickHandler recyclerViewAdapterOnClickHandler, MainActivity main, String dowload, ProgressBar progress) {
        this.progressBar = progress;
        this.mainActivity = main;
        this.mRecyclerViewAdapterOnClickHandler = recyclerViewAdapterOnClickHandler;

        if (dowload == null){
            jsonDownload =null;
            downloadURL = null;
        }else {
            this.downloadURL = dowload;

            this.jsonDownload = new JsonDownload(this.mainActivity, this);

            try {
                this.progressBar.setVisibility(View.VISIBLE);
                this.jsonDownload.execute(new String[]{this.downloadURL}).get();
            } catch (ExecutionException | InterruptedException ex) {
                ex.printStackTrace();
            }

            this.paths = JsonUtils.parseImagesPathsJson(this.results);
        }
    }

    public RecyclerViewAdapter.RecyclerViewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.movielist;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new RecyclerViewAdapter.RecyclerViewAdapterViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerViewAdapter.RecyclerViewAdapterViewHolder holder, int position) {
        Picasso.with(this.mainActivity).load("http://image.tmdb.org/t/p/w185/" + (String)this.paths.get(position)).fit().centerCrop().into(holder.imageView, new Callback() {
            public void onSuccess() {
                RecyclerViewAdapter.this.progressBar.setVisibility(View.GONE);
            }

            public void onError() {
                Picasso.with(RecyclerViewAdapter.this.mainActivity).load(2131099733).into(holder.imageView);
                RecyclerViewAdapter.this.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public int getItemCount() {
        return this.paths !=null ? this.paths.size() : 0;
    }

    public void setResults(JSONArray result) {
        this.results = result;
    }

    public class RecyclerViewAdapterViewHolder extends ViewHolder implements OnClickListener {
        private final ImageView imageView;

        public RecyclerViewAdapterViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView)itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            int clickedAdapter = this.getAdapterPosition();
            RecyclerViewAdapter.this.mRecyclerViewAdapterOnClickHandler.onClickListener(clickedAdapter);
        }
    }
    public List<Integer> getMoviesId() {
        return moviesId;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setTasks(ArrayList<Integer> moviesId) {
        this.moviesId = moviesId;
        paths = JsonUtils.parseImagesPathsJsonWithID(results,moviesId);
        notifyDataSetChanged();
    }
    public interface RecyclerViewAdapterOnClickHandler {
        void onClickListener(int var1);
    }
}
