package com.hilbing.mymovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hilbing.mymovies.R;
import com.hilbing.mymovies.activities.DetailActivity;
import com.hilbing.mymovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.iv_thumbnail_movie_rv)
    ImageView mImageMove;

    private Context mContext;
    private List<Movie> movieList = new ArrayList<>();

    public static final String MOVIE = "movie";


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int pos = getAdapterPosition();
        Log.i("POS", String.valueOf(pos));
        Log.i("Movies", movieList.toString());
        mContext = view.getContext();
        if (pos != RecyclerView.NO_POSITION) {
            Log.i("Movies", movieList.toString());
            Movie movieClicked = movieList.get(pos);
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(MOVIE, movieClicked);
            mContext.startActivity(intent);
            Toast.makeText(mContext, "You clicked " + movieClicked.getOriginalTitle(), Toast.LENGTH_SHORT).show();

        }
    }
}
