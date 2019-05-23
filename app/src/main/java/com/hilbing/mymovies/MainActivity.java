package com.hilbing.mymovies;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hilbing.mymovies.adapter.GridAdapter;
import com.hilbing.mymovies.apiConnection.ApiClient;
import com.hilbing.mymovies.apiConnection.TMDBInterface;
import com.hilbing.mymovies.model.Movie;
import com.hilbing.mymovies.model.MovieResults;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isConnected = false;

    public static int PAGE = 1;
    public static String LANGUAGE = "en_US";
    public static String CATEGORY = "top_rated";

    private List<Movie> movies;
    private GridAdapter mAdapter;

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.rl_refresh)
    SwipeRefreshLayout mRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        //it will charge the movies by default
        fetchMovies();
    }

    private void initUI() {

        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);  //hide it by default, it only appears when data is downloading

        movies = new ArrayList<>();
        mAdapter = new GridAdapter(this, movies);

        //Method to detect orientation of the screen and change the grid columns
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        isConnected = checkNetworkStatus();

        if (isConnected) {

            mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchMovies();
                }
            });
        }

    }

    private void fetchMovies() {

        mRefresh.setRefreshing(true);

        try{
            //checking if API-KEY is empty
            if(BuildConfig.TMDBApi.isEmpty()){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.get_api_key), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
                return;
            }
            ApiClient client = new ApiClient();
            TMDBInterface tmdbInterface = client.getClient().create(TMDBInterface.class);
            Call<MovieResults> call = tmdbInterface.getMovies(CATEGORY, BuildConfig.TMDBApi, LANGUAGE, PAGE);

            mProgressBar.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    movies = response.body().getResults();
                    mRecyclerView.setAdapter(new GridAdapter(getApplicationContext(), movies));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (mRefresh.isRefreshing()){
                        mRefresh.setRefreshing(false);
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error_fetching_data), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkNetworkStatus() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
