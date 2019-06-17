package com.hilbing.mymovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.hilbing.mymovies.database.FavoriteMoviesDBHelper;
import com.hilbing.mymovies.model.Movie;
import com.hilbing.mymovies.model.MovieResults;
import com.hilbing.mymovies.utils.PaginationScrollListener;
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

    public static int PAGE_START = 1;
    public static String LANGUAGE = "en_US";
    public static String CATEGORY = "";

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    private AppCompatActivity activity = MainActivity.this;

    private FavoriteMoviesDBHelper favoriteMoviesDBHelper;

    private GridLayoutManager gridLayoutManager;

    private List<Movie> movies;
    private GridAdapter mAdapter;

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.rl_refresh)
    SwipeRefreshLayout mRefresh;

    private PaginationScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        checkSortOrder(currentPage);

    }

    private void initUI() {

        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);  //hide it by default, it only appears when data is downloading

        movies = new ArrayList<>();
        mAdapter = new GridAdapter(this, movies);

        //Method to detect orientation of the screen and change the grid columns
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        //Adding infinite scrolling
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                checkNetworkStatus();
                checkSortOrder(currentPage);
                loadNextPage(CATEGORY);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        mAdapter.notifyDataSetChanged();

        favoriteMoviesDBHelper = new FavoriteMoviesDBHelper(activity);

        isConnected = checkNetworkStatus();

        registerSharedPreferencesListener();

        if (isConnected) {

            mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    checkSortOrder(currentPage);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    private void initFavorites() {

        ButterKnife.bind(this);

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
        favoriteMoviesDBHelper = new FavoriteMoviesDBHelper(activity);
        getAllFavorite();

    }

    private void getAllFavorite() {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                movies.clear();
                movies.addAll(favoriteMoviesDBHelper.getAllFavorites());
                Log.d(TAG, movies.toString());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();

    }

    private void registerSharedPreferencesListener() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

    }

    private void unregisterSharedPreferencesListener(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        checkNetworkStatus();
        checkSortOrder(currentPage);

    }

    private void checkSortOrder(int currentPage) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String order = preferences.getString(
                this.getString(R.string.pref_sort_order_key),"");

        if (order.equals(this.getString(R.string.pref_popular_key))){

            CATEGORY = getResources().getString(R.string.pref_popular_key);
            fetchMovies(CATEGORY, currentPage);
            mAdapter.notifyDataSetChanged();
            getSupportActionBar().setTitle(R.string.pref_popular);



        } else if (order.equals(this.getString(R.string.pref_favorite_key))){
            initFavorites();
            getSupportActionBar().setTitle(R.string.pref_favorite);
        }
        else {
            CATEGORY = getResources().getString(R.string.pref_top_rated_key);
            fetchMovies(CATEGORY, currentPage);
            mAdapter.notifyDataSetChanged();
            getSupportActionBar().setTitle(R.string.pref_top_rated);
        }

    }



    private void fetchMovies(String category, final int currentPage) {

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
            Call<MovieResults> call = tmdbInterface.getMovies(category, BuildConfig.TMDBApi, LANGUAGE, currentPage);

            mProgressBar.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    movies = new ArrayList<>();
                    movies = response.body().getResults();
                    mRecyclerView.setAdapter(new GridAdapter(getApplicationContext(), movies));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (mRefresh.isRefreshing()){
                        mRefresh.setRefreshing(false);
                    }

                    if(currentPage <= TOTAL_PAGES) mAdapter.notifyDataSetChanged();
                    else isLastPage = true;

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

    private void loadNextPage(String category){

        Log.d(TAG, "Loading next page: " + currentPage);

        ApiClient client = new ApiClient();
        TMDBInterface tmdbInterface = client.getClient().create(TMDBInterface.class);
        Call<MovieResults> call = tmdbInterface.getMovies(category, BuildConfig.TMDBApi, LANGUAGE, currentPage);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                isLoading = false;
                movies = response.body().getResults();
                mRecyclerView.setAdapter(new GridAdapter(getApplicationContext(), movies));
                if (mRefresh.isRefreshing()){
                    mRefresh.setRefreshing(false);
                }
                if (currentPage != TOTAL_PAGES) mAdapter.notifyDataSetChanged();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        registerSharedPreferencesListener();
        checkNetworkStatus();
        checkSortOrder(currentPage);

        if (movies.isEmpty()){
            checkSortOrder(currentPage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSharedPreferencesListener();
    }
}
