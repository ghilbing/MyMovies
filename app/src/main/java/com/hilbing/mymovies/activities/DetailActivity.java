package com.hilbing.mymovies.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.hilbing.mymovies.BuildConfig;
import com.hilbing.mymovies.R;
import com.hilbing.mymovies.adapter.ReviewAdapter;
import com.hilbing.mymovies.adapter.TrailerAdapter;
import com.hilbing.mymovies.apiConnection.ApiClient;
import com.hilbing.mymovies.apiConnection.TMDBInterface;
import com.hilbing.mymovies.database.FavoriteMoviesDBHelper;
import com.hilbing.mymovies.model.Movie;
import com.hilbing.mymovies.model.Review;
import com.hilbing.mymovies.model.ReviewResults;
import com.hilbing.mymovies.model.Trailer;
import com.hilbing.mymovies.model.TrailerResults;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private final static String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.tv_movie_synopsis)
    TextView mMovieSynopsis;
    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;
    @BindView(R.id.tv_release)
    TextView mMovieRelease;
    @BindView(R.id.iv_thumbnail_detail)
    ImageView mMovieImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ct_collapsing)
    CollapsingToolbarLayout mCollapsingTB;
    @BindView(R.id.rv_trailer)
    RecyclerView mRvTrailer;
    @BindView(R.id.rv_reviews)
    RecyclerView mRvReview;
    @BindView(R.id.btn_favorite)
    MaterialFavoriteButton mFavorite;

    private TrailerAdapter mTrailerAdapter;
    private List<Trailer> trailerList;

    private ReviewAdapter mReviewAdapter;
    private List<Review> reviewList;

    private FavoriteMoviesDBHelper favoriteMoviesDBHelper;
    private Movie favoriteMovie;
    private final AppCompatActivity activity = DetailActivity.this;


    public static final String MOVIE = "movie";
    private Movie mMovie;
    private  String baseImageUrl = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentThatStartedDetail = getIntent();
        if (intentThatStartedDetail.hasExtra("movie")){
            mMovie = intentThatStartedDetail.getParcelableExtra(MOVIE);
            mMovieSynopsis.setText(mMovie.getOverview());
            mRatingBar.setRating(Float.valueOf(String.valueOf(mMovie.getVoteAverage()/2)));
            mMovieRelease.setText(mMovie.getReleaseDate());
            String backdropPath = mMovie.getBackdropPath();
            Log.d(TAG, baseImageUrl + backdropPath);
            Picasso.get().load(baseImageUrl + backdropPath).placeholder(R.drawable.movie_placeholder).into(mMovieImage);
            mCollapsingTB.setTitle(mMovie.getTitle());
            mCollapsingTB.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            mCollapsingTB.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_fetching_data), Toast.LENGTH_LONG).show();
        }

        initViews();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mFavorite.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                    SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                    editor.putBoolean(getResources().getString(R.string.favorite_added), true);
                    editor.commit();
                    saveFavorite();
                    mFavorite.setFavoriteResource(R.drawable.ic_favorite);
                    Snackbar.make(buttonView, getResources().getString(R.string.favorite_added), Snackbar.LENGTH_LONG).show();
                } else {
                    int movieId = mMovie.getId();
                    SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                    editor.putBoolean(getResources().getString(R.string.favorite_removed), true);
                    editor.commit();
                    favoriteMoviesDBHelper = new FavoriteMoviesDBHelper(DetailActivity.this);
                    favoriteMoviesDBHelper.removeFavorite(movieId);
                    Snackbar.make(buttonView, getResources().getString(R.string.favorite_removed), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void saveFavorite() {
        favoriteMoviesDBHelper = new FavoriteMoviesDBHelper(activity);
        favoriteMovie = new Movie();

        favoriteMovie.setId(mMovie.getId());
        favoriteMovie.setTitle(mMovie.getTitle());
        favoriteMovie.setPosterPath(mMovie.getPosterPath());
        favoriteMovie.setVoteAverage(mMovie.getVoteAverage());
        favoriteMovie.setOverview(mMovie.getOverview());
        favoriteMovie.setBackdropPath(mMovie.getBackdropPath());

        favoriteMoviesDBHelper.addFavorite(favoriteMovie);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        trailerList = new ArrayList<>();
        reviewList = new ArrayList<>();
        mTrailerAdapter = new TrailerAdapter(this, trailerList);
        mReviewAdapter = new ReviewAdapter(this, reviewList);
        mRvTrailer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRvReview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRvTrailer.setAdapter(mTrailerAdapter);
        mRvReview.setAdapter(mReviewAdapter);
        mTrailerAdapter.notifyDataSetChanged();
        mReviewAdapter.notifyDataSetChanged();

        loadTrailers();
        loadReviews();

    }

    private void loadTrailers(){
        int movie_id = mMovie.getId();
        Toast.makeText(this, String.valueOf(movie_id), Toast.LENGTH_LONG).show();
        try{
            if (BuildConfig.TMDBApi.isEmpty()){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.get_api_key), Toast.LENGTH_LONG).show();
                return;
            }
            ApiClient client = new ApiClient();
            TMDBInterface tmdbInterface = client.getClient().create(TMDBInterface.class);
            Call<TrailerResults> call = tmdbInterface.getMovieTrailers(movie_id, BuildConfig.TMDBApi);
            call.enqueue(new Callback<TrailerResults>() {
                @Override
                public void onResponse(Call<TrailerResults> call, Response<TrailerResults> response) {
                    List<Trailer> trailers = response.body().getResults();
                    mRvTrailer.setAdapter(new TrailerAdapter(getApplicationContext(), trailers));
                    mRvTrailer.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<TrailerResults> call, Throwable t) {
                    Log.d(TAG, "ERROR FETCHING TRAILERS: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_fetching_trailers), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadReviews(){
        int movie_id = mMovie.getId();
        try{
            if(BuildConfig.TMDBApi.isEmpty()){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.get_api_key), Toast.LENGTH_LONG).show();
                return;
            }
            ApiClient client = new ApiClient();
            TMDBInterface tmdbInterface = client.getClient().create(TMDBInterface.class);
            Call<ReviewResults> call = tmdbInterface.getMovieReviews(movie_id, BuildConfig.TMDBApi);
            call.enqueue(new Callback<ReviewResults>() {
                @Override
                public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                    List<Review> reviews = response.body().getResults();
                    mRvReview.setAdapter(new ReviewAdapter(getApplicationContext(), reviews));
                    mRvReview.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<ReviewResults> call, Throwable t) {
                    Log.d(TAG, "ERROR FETCHING REVIEWS" + t.getMessage());
                }
            });

        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

}
