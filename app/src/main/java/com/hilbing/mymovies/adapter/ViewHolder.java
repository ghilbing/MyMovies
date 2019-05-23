package com.hilbing.mymovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hilbing.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_thumbnail_movie)
    ImageView mImageMove;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}
