package com.hilbing.mymovies.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.hilbing.mymovies.R;
import com.hilbing.mymovies.model.Movie;
import com.rhexgomez.typer.roboto.TyperRoboto;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_movie_synopsis)
    TextView mMovieSynopsis;
    @BindView(R.id.tv_movie_rating)
    TextView mMovieRating;
    @BindView(R.id.tv_release)
    TextView mMovieRelease;
    @BindView(R.id.iv_thumbnail_detail)
    ImageView mMovieImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ct_collapsing)
    CollapsingToolbarLayout mCollapsingTB;

    public static final String MOVIE = "movie";
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentThatStartedDetail = getIntent();
        if (intentThatStartedDetail.hasExtra("movie")){
            mMovie = intentThatStartedDetail.getParcelableExtra(MOVIE);
            mMovieSynopsis.setText(mMovie.getOverview());
            mMovieRating.setText(String.valueOf(mMovie.getVoteAverage()));
            mMovieRelease.setText(mMovie.getReleaseDate());
            Picasso.get().load(mMovie.getBackdropPath()).placeholder(R.drawable.movie_placeholder).into(mMovieImage);
            mCollapsingTB.setTitle(mMovie.getTitle());
            mCollapsingTB.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            mCollapsingTB.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
