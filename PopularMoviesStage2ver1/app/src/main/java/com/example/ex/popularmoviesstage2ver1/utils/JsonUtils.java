package com.example.ex.popularmoviesstage2ver1.utils;

import com.example.ex.popularmoviesstage2ver1.model.Movie;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    public JsonUtils() {
    }

    public static List<String> parseImagesPathsJsonWithID(JSONArray results,ArrayList<Integer> moviesId) {
        ArrayList imagePathList = null;

        try {
            imagePathList = new ArrayList();

            for (int j=0;j<moviesId.size();j++){

            for(int i = 0; i < results.length(); ++i) {
                JSONObject movieJson = results.getJSONObject(i);
                if (movieJson.getInt("id")!=moviesId.get(j)){
                    continue;
                }else {
                    imagePathList.add(movieJson.getString("poster_path"));
                    break;
                }
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return imagePathList;
    }
    public static Movie parseMovieJson(JSONArray results, int selectedImageView) {
        Movie movie = null;

        try {
            JSONObject movieJson = results.getJSONObject(selectedImageView);
            int id = movieJson.getInt("id");
            String original_title = movieJson.getString("original_title");
            String poster_path = movieJson.getString("poster_path");
            String overview = movieJson.getString("overview");
            Double vote_average = movieJson.getDouble("vote_average");
            String release_date = movieJson.getString("release_date");
            movie = new Movie(id, original_title, poster_path, overview, vote_average, release_date);
        } catch (JSONException var11) {
            var11.printStackTrace();
        }

        return movie;
    }

    public static List<String> parseImagesPathsJson(JSONArray results) {
        ArrayList imagePathList = null;

        try {
            imagePathList = new ArrayList();

            for(int i = 0; i < results.length(); ++i) {
                JSONObject movieJson = results.getJSONObject(i);
                imagePathList.add(movieJson.getString("poster_path"));
            }
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        return imagePathList;
    }

    public static LinkedHashMap<String, String> parseReviews(JSONArray results) {
        LinkedHashMap<String, String> reviewsLinkedHashMap = new LinkedHashMap();

        for(int i = 0; i < results.length(); ++i) {
            try {
                JSONObject reviewObject = results.getJSONObject(i);
                reviewsLinkedHashMap.put(reviewObject.getString("author"), reviewObject.getString("content"));
            } catch (JSONException var5) {
                var5.printStackTrace();
            }
        }

        return reviewsLinkedHashMap;
    }

    public static LinkedHashMap<String, String> parseTrailers(JSONArray results) {
        LinkedHashMap<String, String> trailersLinkedHashMap = new LinkedHashMap();

        for(int i = 0; i < results.length(); ++i) {
            try {
                JSONObject trailerObject = results.getJSONObject(i);
                trailersLinkedHashMap.put(trailerObject.getString("key"), trailerObject.getString("name"));
            } catch (JSONException var5) {
                var5.printStackTrace();
            }
        }

        return trailersLinkedHashMap;
    }
}
