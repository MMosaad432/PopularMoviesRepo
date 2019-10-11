package com.example.ex.popularmoviesstage2ver1.utils;

import android.os.AsyncTask;
import com.example.ex.popularmoviesstage2ver1.ReviewsRecyclerViewAdapter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReviewDownload extends AsyncTask<String, Void, Void> {
    private final ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;

    public JsonReviewDownload(ReviewsRecyclerViewAdapter Adapter) {
        this.reviewsRecyclerViewAdapter = Adapter;
    }

    protected Void doInBackground(String... strings) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(4000);
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            for(int data = reader.read(); data != -1; data = reader.read()) {
                char ch = (char)data;
                content.append(ch);
            }

            JSONObject allJson = new JSONObject(content.toString());
            JSONObject reviewsString = allJson.getJSONObject("reviews");
            JSONArray results = reviewsString.getJSONArray("results");
            this.setResults(results);
            return null;
        } catch (Exception var12) {
            var12.printStackTrace();
            return null;
        }
    }

    private void setResults(JSONArray results) {
        this.reviewsRecyclerViewAdapter.setResults(results);
    }
}
