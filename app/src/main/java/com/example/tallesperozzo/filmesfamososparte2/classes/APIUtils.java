package com.example.tallesperozzo.filmesfamososparte2.classes;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Perozzo on 15/09/2017.
 * APIUtils class to communicate with the server and parse data
 */

public final class APIUtils {
    private final static String LOG_TAG = APIUtils.class.getSimpleName();
    private final static String API_KEY = "f827b138334bef6d6591d3678db2a21a";
    private final static String API_KEY_ = "api_key=" + API_KEY;
    public final static String IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private final static String GENERIC_URL = "http://api.themoviedb.org/3/movie/";
    private final static String MOST_POPULAR_URL = GENERIC_URL + "popular?" + API_KEY_;
    private final static String TOP_RATED_URL = GENERIC_URL + "top_rated?" + API_KEY_;
    private final static String TRAILERS_URL_END = "/videos?" + API_KEY_;
    private final static String REVIEWS_URL_END = "/reviews?" + API_KEY_;
    //http://api.themoviedb.org/3/movie/299536/videos?api_key=f827b138334bef6d6591d3678db2a21a

    private final static String UNKNOWN_TITLE = "Unknown title";
    private final static String UNKNOWN_DATE = "Unknown date"; //
    private final static String UNKNOWN_POSTER_PATH = "Unknown poster path";
    private final static String UNKNOWN_VOTE_AVERAGE = "Unknown vote average";
    private final static String UNKNOWN_OVERVIEW = "Unknown overview";

    private APIUtils() {
    }

    //Method to search Movies
    public static List<Movie> fetchMovies(int mode){
        URL url = buildURL(mode);
        String jsonResponse = null;
        try
        {
            jsonResponse = getResponseFromHttpUrl(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error in making http request",e);
        }
        return extractMovies(jsonResponse);
    }

    public static List<Trailer> fetchTrailers(int mode, String id){
        URL url = buildURL(mode, id);
        String jsonResponse = null;
        try
        {
            jsonResponse = getResponseFromHttpUrl(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error in making http request",e);
        }
        return extractTrailers(jsonResponse);
    }

    public static List<Review> fetchReviews(int mode, String id){
        URL url = buildURL(mode, id);
        String jsonResponse = null;
        try
        {
            jsonResponse = getResponseFromHttpUrl(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error in making http request",e);
        }
        return extractReviews(jsonResponse);
    }



    //Method to create an URL
    private static URL buildURL(int mode){

        URL url = null;
        try
        {
            if(mode == 1)
                url = new URL(MOST_POPULAR_URL);
            else if (mode == 2)
                url = new URL(TOP_RATED_URL);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error in Creating URL",e);
        }
        return url;
    }

    private static URL buildURL(int mode, String id){

        URL url = null;
        try
        {
            if(mode == 3)
                url = new URL(GENERIC_URL + id + TRAILERS_URL_END );
            else if (mode == 4)
                url = new URL(GENERIC_URL + id + REVIEWS_URL_END);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error in Creating URL",e);
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    //Method to parse movies
    private static List<Movie> extractMovies(String moviesJSON){
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);
            JSONArray moviesArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject currentMovie = moviesArray.getJSONObject(i);
                String id = currentMovie.getString("id");
                String title;
                String date;
                String poster_path;
                String vote_average;
                String overview;

                if(!currentMovie.getString("title").equals(""))
                    title = currentMovie.getString("title");
                else
                    title = UNKNOWN_TITLE;

                if(!currentMovie.getString("release_date").equals(""))
                    date = currentMovie.getString("release_date").substring(0,10);
                else
                    date = UNKNOWN_DATE;

                if(!currentMovie.getString("poster_path").equals(""))
                    poster_path = IMAGE_URL + currentMovie.getString("poster_path");
                else
                    poster_path = UNKNOWN_POSTER_PATH;

                if(!currentMovie.getString("vote_average").equals(""))
                    vote_average = String.valueOf(currentMovie.getDouble("vote_average"));
                else
                    vote_average = UNKNOWN_VOTE_AVERAGE;

                if(!currentMovie.getString("overview").equals(""))
                    overview = currentMovie.getString("overview");
                else
                    overview = UNKNOWN_OVERVIEW;

                Movie movie = new Movie(id, title, date, poster_path, vote_average, overview);
                movies.add(movie);
            }

        }catch (JSONException e){
            Log.e(LOG_TAG,"Error in fetching data",e);
        }
        return movies;
    }

    private static List<Trailer> extractTrailers(String trailersJSON){
        if (TextUtils.isEmpty(trailersJSON)) {
            return null;
        }

        ArrayList<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(trailersJSON);
            JSONArray trailersArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < trailersArray.length(); i++) {
                JSONObject currentTrailer = trailersArray.getJSONObject(i);
                String id = currentTrailer.getString("id");
                String name;
                String key = currentTrailer.getString("key");

                if(!currentTrailer.getString("name").equals(""))
                    name = currentTrailer.getString("name");
                else
                    name = UNKNOWN_TITLE;

                Trailer trailer = new Trailer(id, name, key);
                trailers.add(trailer);

            }

        }catch (JSONException e){
            Log.e(LOG_TAG,"Error in fetching data",e);
        }
        return trailers;


    }

    private static List<Review> extractReviews(String reviewsJSON){
        if (TextUtils.isEmpty(reviewsJSON)) {
            return null;
        }

        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(reviewsJSON);
            JSONArray trailersArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < trailersArray.length(); i++) {
                JSONObject currentTrailer = trailersArray.getJSONObject(i);
                String id = currentTrailer.getString("id");
                String author;
                String content = currentTrailer.getString("content");

                if(!currentTrailer.getString("author").equals(""))
                    author = currentTrailer.getString("author");
                else
                    author = UNKNOWN_TITLE;

                Review review = new Review(id, author, content);
                reviews.add(review);

            }

        }catch (JSONException e){
            Log.e(LOG_TAG,"Error in fetching data",e);
        }
        return reviews;


    }
}
