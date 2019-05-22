package com.hilbing.mymovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hilbing.mymovies.apiConnection.TMDBInterface;
import com.hilbing.mymovies.model.Movie;
import com.hilbing.mymovies.model.MovieResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String BASE_URL = "https://api.themoviedb.org";
    public static int PAGE = 1;
    public static String LANGUAGE = "en_US";
    public static String CATEGORY = "top_rated";

    private TextView mMovieTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieTitle = findViewById(R.id.tv_movieTitle);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBInterface tmdbInterface = retrofit.create(TMDBInterface.class);

        Call<MovieResults> call = tmdbInterface.getMovies(CATEGORY, getResources().getString(R.string.api_key), LANGUAGE, PAGE);

        Log.i("BEFORE CALL", "HELLO");

        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                Log.i("GETTING IN ONRESPONSE", "CHECKED");
                MovieResults results = response.body();
                List<Movie> listOfMovies = results.getResults();
                Movie firstMovie = listOfMovies.get(0);
                mMovieTitle.setText(firstMovie.getTitle());
                Log.i("JSON", firstMovie.getTitle());

            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                t.printStackTrace();
                Log.i("ERROR", t.getMessage());
            }
        });

    }
}
