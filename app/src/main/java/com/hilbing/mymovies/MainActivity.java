package com.hilbing.mymovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hilbing.mymovies.activities.SettingsActivity;
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


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isConnected = false;

    public static int PAGE = 1;
    public static String LANGUAGE = "en_US";
    public static String CATEGORY = "";

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

        checkSortOrder();

        //it will charge the movies by default
       // fetchMovies(CATEGORY);
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

        registerSharedPreferencesListener();

        if (isConnected) {

            mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    checkSortOrder();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    private void registerSharedPreferencesListener() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        preferences.registerOnSharedPreferenceChangeListener(this);

    }

    private void unregisterSharedPreferencesListener(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        preferences.unregisterOnSharedPreferenceChangeListener(this);
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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "Entra o no entra.....");
        checkNetworkStatus();
        checkSortOrder();
    }

    private void checkSortOrder() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String order = preferences.getString(
                this.getString(R.string.pref_sort_order_key),"");

        if (order.equals(this.getString(R.string.pref_popular_key))){
            CATEGORY = getResources().getString(R.string.pref_popular_key);
        } else {
            CATEGORY = getResources().getString(R.string.pref_top_rated_key);
        }
        fetchMovies(CATEGORY);
        mAdapter.notifyDataSetChanged();
    }

    private void fetchMovies(String category) {

        mRefresh.setRefreshing(true);
        Log.d(TAG, "CATEGORY: " + category);

        try{
            //checking if API-KEY is empty
            if(BuildConfig.TMDBApi.isEmpty()){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.get_api_key), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
                return;
            }
            ApiClient client = new ApiClient();
            TMDBInterface tmdbInterface = client.getClient().create(TMDBInterface.class);
            Call<MovieResults> call = tmdbInterface.getMovies(category, BuildConfig.TMDBApi, LANGUAGE, PAGE);

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



    @Override
    protected void onResume() {
        super.onResume();

        unregisterSharedPreferencesListener();

        if (movies.isEmpty()){
            checkSortOrder();
        } else {

        }
    }
}
