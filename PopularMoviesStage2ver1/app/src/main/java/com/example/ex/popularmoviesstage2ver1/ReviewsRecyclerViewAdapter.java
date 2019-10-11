package com.example.ex.popularmoviesstage2ver1;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ex.popularmoviesstage2ver1.utils.JsonReviewDownload;
import com.example.ex.popularmoviesstage2ver1.utils.JsonUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;

public class ReviewsRecyclerViewAdapter extends Adapter<ReviewsRecyclerViewAdapter.ReviewsRecyclerViewAdapterViewHolder> {
    private final String downloadURL;
    private final LinkedHashMap<String, String> reviews;
    private JSONArray results;
    private final JsonReviewDownload jsonReviewDownload;

    public ReviewsRecyclerViewAdapter(int id) {
        this.downloadURL = "https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "?api_key=40d054e2ebafc9c504cb9e1a58dfd9d8&append_to_response=reviews";
        this.jsonReviewDownload = new JsonReviewDownload(this);

        try {
            this.jsonReviewDownload.execute(new String[]{this.downloadURL}).get();
        } catch (ExecutionException | InterruptedException ex) {
            ex.printStackTrace();
        }

        this.reviews = JsonUtils.parseReviews(this.results);
    }

    public ReviewsRecyclerViewAdapter.ReviewsRecyclerViewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.reviewslist;
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new ReviewsRecyclerViewAdapter.ReviewsRecyclerViewAdapterViewHolder(view);
    }

    public void onBindViewHolder(ReviewsRecyclerViewAdapter.ReviewsRecyclerViewAdapterViewHolder holder, int position) {
        if (this.getItemCount() == 1 && this.reviews.isEmpty()) {
            holder.authorTextView.setText("No reviews for this movie.");
            holder.contentTextView.setVisibility(View.GONE);
        } else {
            Set<Entry<String, String>> mapSet = this.reviews.entrySet();
            Entry<String, String> element = (Entry)(new ArrayList(mapSet)).get(position);
            holder.authorTextView.setText(position + 1 + "- " + (String)element.getKey());
            holder.contentTextView.setVisibility(View.VISIBLE);
            holder.contentTextView.setText((CharSequence)element.getValue());
        }

    }

    public int getItemCount() {
        return !this.reviews.isEmpty() ? this.reviews.size() : 1;
    }

    public void setResults(JSONArray result) {
        this.results = result;
    }

    public class ReviewsRecyclerViewAdapterViewHolder extends ViewHolder {
        private final TextView authorTextView;
        private final TextView contentTextView;

        public ReviewsRecyclerViewAdapterViewHolder(View itemView) {
            super(itemView);
            this.authorTextView = (TextView)itemView.findViewById(R.id.authorTextView);
            this.contentTextView = (TextView)itemView.findViewById(R.id.contentTextView);
        }
    }
}
