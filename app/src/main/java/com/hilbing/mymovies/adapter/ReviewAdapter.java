package com.hilbing.mymovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hilbing.mymovies.R;
import com.hilbing.mymovies.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context mContext;
    private List<Review> mReviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        mContext = context;
        mReviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        reviewViewHolder.mAuthor.setText(mReviewList.get(i).getAuthor());
        reviewViewHolder.mReview.setText(mReviewList.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author)
        TextView mAuthor;
        @BindView(R.id.tv_review)
        TextView mReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
