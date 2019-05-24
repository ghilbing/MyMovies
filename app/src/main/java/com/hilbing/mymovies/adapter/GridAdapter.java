package com.hilbing.mymovies.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.hilbing.mymovies.R;
import com.hilbing.mymovies.activities.DetailActivity;
import com.hilbing.mymovies.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    private Context mContext;
    private List<Movie> movieList;

    public static final String MOVIE = "movie";

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_thumbnail_movie_rv)
        ImageView mImageMove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            mContext = view.getContext();
            if (pos != RecyclerView.NO_POSITION) {
                Log.i("Movies", movieList.toString());
                Movie movieClicked = movieList.get(pos);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(MOVIE, movieClicked);
                mContext.startActivity(intent);

            }
        }
    }


}
