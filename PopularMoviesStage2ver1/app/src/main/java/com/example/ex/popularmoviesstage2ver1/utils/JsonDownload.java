package com.example.ex.popularmoviesstage2ver1.utils;

import android.os.AsyncTask;
import com.example.ex.popularmoviesstage2ver1.MainActivity;
import com.example.ex.popularmoviesstage2ver1.RecyclerViewAdapter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonDownload extends AsyncTask<String, Void, Void> {
    private final MainActivity mainActivity;
    private final RecyclerViewAdapter recyclerViewAdapter;

    public JsonDownload(MainActivity main, RecyclerViewAdapter Adapter) {
        this.mainActivity = main;
        this.recyclerViewAdapter = Adapter;
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

            JSONObject moviesString = new JSONObject(content.toString());
            JSONArray results = moviesString.getJSONArray("results");
            this.setResults(results);
            return null;
        } catch (Exception var11) {
            var11.printStackTrace();
            return null;
        }
    }

    private void setResults(JSONArray results) {
        this.mainActivity.setResults(results);
        this.recyclerViewAdapter.setResults(results);
    }
}
