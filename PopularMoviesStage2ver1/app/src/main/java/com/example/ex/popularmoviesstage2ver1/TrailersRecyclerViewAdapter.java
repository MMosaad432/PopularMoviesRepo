package com.example.ex.popularmoviesstage2ver1;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ex.popularmoviesstage2ver1.utils.JsonTrailersDownload;
import com.example.ex.popularmoviesstage2ver1.utils.JsonUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;

public class TrailersRecyclerViewAdapter extends Adapter<TrailersRecyclerViewAdapter.TrailerRecyclerViewAdapterViewHolder> {
    private final String downloadURL;
    private final LinkedHashMap<String, String> trailers;
    private final MovieDetailsActivity movieDetailsActivity;
    private JSONArray results;
    private final JsonTrailersDownload jsonTrailersDownload;
    private final TrailersRecyclerViewAdapter.trailerRecyclerViewAdapterOnClickHandler mTrailerRecyclerViewAdapterOnClickHandler;

    public TrailersRecyclerViewAdapter(TrailersRecyclerViewAdapter.trailerRecyclerViewAdapterOnClickHandler trailerRecyclerViewAdapterOnClickHandler, MovieDetailsActivity main, int id) {
        this.movieDetailsActivity = main;
        this.downloadURL = "https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "?api_key=40d054e2ebafc9c504cb9e1a58dfd9d8&language=en-US&append_to_response=videos";
        this.mTrailerRecyclerViewAdapterOnClickHandler = trailerRecyclerViewAdapterOnClickHandler;
        this.jsonTrailersDownload = new JsonTrailersDownload(this);

        try {
            this.jsonTrailersDownload.execute(new String[]{this.downloadURL}).get();
        } catch (ExecutionException | InterruptedException var5) {
            var5.printStackTrace();
        }

        this.trailers = JsonUtils.parseTrailers(this.results);
    }

    public TrailersRecyclerViewAdapter.TrailerRecyclerViewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.trailerslist;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new TrailersRecyclerViewAdapter.TrailerRecyclerViewAdapterViewHolder(view);
    }

    public void onBindViewHolder(final TrailersRecyclerViewAdapter.TrailerRecyclerViewAdapterViewHolder holder, int position) {
        if (this.getItemCount() == 1 && this.trailers.isEmpty()) {
            holder.trailerThumbnailImageView.setVisibility(View.GONE);
            holder.trailer.setText("No trailers for this movie");
        } else {
            holder.trailerThumbnailImageView.setVisibility(View.VISIBLE);
            Set<Entry<String, String>> mapSet = this.trailers.entrySet();
            Entry<String, String> element = (Entry)(new ArrayList(mapSet)).get(position);
            Picasso.with(this.movieDetailsActivity).load("https://img.youtube.com/vi/" + (String)element.getKey() + "/0.jpg").into(holder.trailerThumbnailImageView, new Callback() {
                public void onSuccess() {
                }

                public void onError() {
                    Picasso.with(TrailersRecyclerViewAdapter.this.movieDetailsActivity).load(R.drawable.download).into(holder.trailerThumbnailImageView);
                }
            });
            holder.trailer.setText((String)element.getValue() + "\nTrailer " + (position + 1));
            holder.itemView.setTag(element.getKey());
        }
    }

    public int getItemCount() {
        return !this.trailers.isEmpty() ? this.trailers.size() : 1;
    }

    public void setResults(JSONArray result) {
        this.results = result;
    }

    public class TrailerRecyclerViewAdapterViewHolder extends ViewHolder implements OnClickListener {
        private final ImageView trailerThumbnailImageView;
        private final TextView trailer;

        public TrailerRecyclerViewAdapterViewHolder(View itemView) {
            super(itemView);
            this.trailerThumbnailImageView = (ImageView)itemView.findViewById(R.id.trailerThumbnailImageView);
            this.trailer = (TextView)itemView.findViewById(R.id.trailerName);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            String clickedAdapter = view.getTag().toString();
            TrailersRecyclerViewAdapter.this.mTrailerRecyclerViewAdapterOnClickHandler.onClickListener(clickedAdapter);
        }
    }

    public interface trailerRecyclerViewAdapterOnClickHandler {
        void onClickListener(String var1);
    }
}
