package com.hilbing.mymovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hilbing.mymovies.R;
import com.hilbing.mymovies.activities.YoutubePlayerActivity;
import com.hilbing.mymovies.model.Trailer;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;
    private List<Trailer> mTrailerList;

    public TrailerAdapter(Context context, List<Trailer> trailerList){
        mContext = context;
        mTrailerList = trailerList;
    }


    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {
        trailerViewHolder.mTrailerTitle.setText(mTrailerList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_trailer_title)
        TextView mTrailerTitle;


        public TrailerViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Uri uriVideo = mTrailerList.get(pos).getYoutubeURL();
                    String key = mTrailerList.get(getAdapterPosition()).getKey();
                    Log.d(TAG, "Play URL: " + uriVideo);

                    Intent intent = new Intent(mContext, YoutubePlayerActivity.class);
                    intent.putExtra(YoutubePlayerActivity.EXTRA_KEY, key);
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
