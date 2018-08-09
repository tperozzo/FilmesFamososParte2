package com.example.tallesperozzo.filmesfamososparte2.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tallesperozzo.filmesfamososparte2.classes.APIUtils;
import com.example.tallesperozzo.filmesfamososparte2.classes.FavoriteMoviesContract;
import com.example.tallesperozzo.filmesfamososparte2.classes.Movie;
import com.example.tallesperozzo.filmesfamososparte2.classes.MovieAdapter;
import com.example.tallesperozzo.filmesfamososparte2.classes.Review;
import com.example.tallesperozzo.filmesfamososparte2.classes.ReviewAdapter;
import com.example.tallesperozzo.filmesfamososparte2.classes.Trailer;
import com.example.tallesperozzo.filmesfamososparte2.classes.TrailerAdapter;
import com.perozzo.tmdbexample.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perozzo on 15/09/2017.
 * Activity to show the movies detail
 */

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickListener{

    private final static String MOVIE = "MOVIE";
    public final static String UNKNOWN_POSTER_PATH = "Unknown poster path";

    private Movie movie;
    private Context ctx;

    private FrameLayout frameLayout;
    private ImageView poster_image_iv;
    private ProgressBar poster_image_pb;
    private TextView title_tv;
    private TextView vote_average_tv;
    private TextView date_tv;
    private TextView overview_tv;

    public List<Trailer> trailerList;
    public List<Review> reviewList;

    public TrailerAdapter trailerAdapter;
    public RecyclerView trailer_rv;
    public ReviewAdapter reviewAdapter;
    public RecyclerView review_rv;

    public ProgressBar trailer_pb;
    public ProgressBar review_pb;

    public boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ctx = this;

        frameLayout = findViewById(R.id.frame_layout);
        poster_image_iv = findViewById(R.id.backdrop_image_iv);
        poster_image_pb = findViewById(R.id.backdrop_image_pb);
        title_tv = findViewById(R.id.title_tv);
        date_tv = findViewById(R.id.date_tv);
        vote_average_tv = findViewById(R.id.vote_average_tv);
        overview_tv = findViewById(R.id.overview_tv);

        final Intent intent = getIntent();

        movie = (Movie) intent.getSerializableExtra(MOVIE);
        if(movie != null) {
            initWidgets();
        }

        trailerList = new ArrayList<>();
        reviewList = new ArrayList<>();

        Cursor result = ctx.getContentResolver().query(
                FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                null,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ID_MOVIE + "=?",
                new String[]{movie.getId_movie()},
                null
        );
        if(result.getCount() > 0)
            isFavorite = true;
        else
            isFavorite = false;

        invalidateOptionsMenu();

        trailer_rv = findViewById(R.id.trailers_rv);
        trailer_rv.setLayoutManager(new LinearLayoutManager(ctx));
        trailerAdapter = new TrailerAdapter(trailerList, this,ctx);
        trailer_rv.setAdapter(trailerAdapter);

        review_rv = findViewById(R.id.reviews_rv);
        review_rv.setLayoutManager(new LinearLayoutManager(ctx));
        reviewAdapter = new ReviewAdapter(reviewList, ctx);
        review_rv.setAdapter(reviewAdapter);

        trailer_pb = findViewById(R.id.trailers_pb);
        review_pb = findViewById(R.id.reviews_pb);

        TrailerAsyncTask trailerTask = new TrailerAsyncTask();
        trailerTask.execute(3);
        ReviewAsyncTask reviewTask = new ReviewAsyncTask();
        reviewTask.execute(4);
    }

    //Initialize widgets from interface
    private void initWidgets(){

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT, (float)1));
        }
        else if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT, (float)0.5));
        }

        if(movie.getPoster_path().equals(UNKNOWN_POSTER_PATH) || movie.getPoster_path().equals(APIUtils.IMAGE_URL +"/null")) {
            frameLayout.setVisibility(View.GONE);
            poster_image_iv.setVisibility(View.GONE);
            poster_image_pb.setIndeterminate(false);
            poster_image_pb.setVisibility(View.GONE);
        }
        else {
            if(isOnline()) {

                Picasso.get().load(movie.getPoster_path()).fit().into(poster_image_iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        poster_image_pb.setIndeterminate(false);
                        poster_image_pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        poster_image_pb.setIndeterminate(false);
                        poster_image_pb.setVisibility(View.GONE);
                    }
                });
            }
            else {
                Toast.makeText(ctx, getString(R.string.no_connection_load_image), Toast.LENGTH_SHORT).show();
                poster_image_pb.setIndeterminate(false);
                poster_image_pb.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
                poster_image_iv.setVisibility(View.GONE);
            }
        }
        title_tv.setText(movie.getTitle());

        date_tv.setText(movie.getDate());

        vote_average_tv.setText(getString(R.string.vote_average) + " " + String.valueOf(movie.getVote_average()));

        overview_tv.setText(movie.getOverview());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT) {
            frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT, (float)1));
        }else if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT, (float)0.5));
        }
    }

    //Method to verify connection, necessary to download the image
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isFavorite)
            getMenuInflater().inflate(R.menu.details_menu_favorite, menu);
        else
            getMenuInflater().inflate(R.menu.details_menu_not_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.not_favorite:
                //become isFavorite
                isFavorite = true;
                insert();
                invalidateOptionsMenu();
                return true;
            case R.id.favorite:
                //become not isFavorite
                isFavorite = false;
                delete();
                invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void insert(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ID_MOVIE, movie.getId_movie());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_DATE, movie.getDate());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, movie.getOverview());

        Uri uri = getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, contentValues);

        if(uri != null)
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();

    }

    public void delete(){
        Uri uri = FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movie.getId_movie()).build();
        getContentResolver().delete(uri, null, null);
    }

    @Override
    public void onTrailerClick(int clickedItemIndex) {
        watchYoutubeVideo(ctx, trailerList.get(clickedItemIndex).getKey());
    }


    public static void watchYoutubeVideo(Context context, String key){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }


    public class TrailerAsyncTask extends AsyncTask<Integer,Void,List<Trailer>> {

        @Override
        protected void onPreExecute() {
            review_pb.setIndeterminate(true);
            review_pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Trailer> doInBackground(Integer... mode) {
            List<Trailer> result = APIUtils.fetchTrailers(mode[0], movie.getId_movie());
            if(result != null) {
                if (!result.isEmpty()) {
                    trailerList.addAll(result);
                    }
            }
            if(result.size() == 0)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.trailers_cv).setVisibility(View.GONE);
                    }
                });

            return result;
        }

        @Override
        protected void onPostExecute(List<Trailer> data) {
            trailerAdapter.notifyDataSetChanged();
            trailer_pb.setIndeterminate(false);
            trailer_pb.setVisibility(View.GONE);
        }

    }

    public class ReviewAsyncTask extends AsyncTask<Integer,Void,List<Review>> {

        @Override
        protected void onPreExecute() {
            review_pb.setIndeterminate(true);
            review_pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Review> doInBackground(Integer... mode) {
            List<Review> result = APIUtils.fetchReviews(mode[0], movie.getId_movie());
            if(result != null) {
                if (!result.isEmpty()) {
                    reviewList.addAll(result);
                }
            }

            if(result.size() == 0)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.reviews_cv).setVisibility(View.GONE);
                    }
                });

            return result;
        }

        @Override
        protected void onPostExecute(List<Review> data) {
            review_pb.setIndeterminate(false);
            review_pb.setVisibility(View.GONE);
            reviewAdapter.notifyDataSetChanged();
        }


    }
}
