package com.hilbing.mymovies.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hilbing.mymovies.R;
import com.hilbing.mymovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;


public class GridAdapter extends RecyclerView.Adapter<ViewHolder>{

    private Context mContext;
    private List<Movie> movieList;

    public GridAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picasso.get().load(movieList.get(i).getPosterPath())
                .placeholder(R.drawable.movie_placeholder)
                .into(viewHolder.mImageMove);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
