package com.hilbing.mymovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hilbing.mymovies.BuildConfig;
import com.hilbing.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubePlayerActivity extends YouTubeBaseActivity {

    @BindView(R.id.youtube_player)
    YouTubePlayerView youTubePlayer;

    public static final String EXTRA_KEY = "key";
    private String key = "";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_player);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        key = intent.getStringExtra(EXTRA_KEY);

        if(BuildConfig.YouTubeKey.isEmpty()){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.get_api_key_youtube), Toast.LENGTH_SHORT).show();
            return;
        }

        youTubePlayer.initialize((BuildConfig.YouTubeKey), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(key);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });


    }
}
