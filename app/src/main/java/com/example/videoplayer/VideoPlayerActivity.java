package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;


public class VideoPlayerActivity extends AppCompatActivity {
    PlayerView playerView;
   SimpleExoPlayer exoPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.videoplayer);
        setScreenOrientation(getIntent().getStringExtra("path_file"));

    }

    private void showVideo(String filePath) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(VideoPlayerActivity.this);
        playerView.setPlayer(exoPlayer);
        DataSource.Factory dataSource = new DefaultDataSourceFactory(VideoPlayerActivity.this, Util.getUserAgent(VideoPlayerActivity.this, getString(R.string.app_name)));

        MediaSource videoSource= new ExtractorMediaSource.Factory(dataSource).createMediaSource(Uri.parse(filePath));
        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(true);

    }


    private void setScreenOrientation(String filepath){
        MediaPlayer mp = new MediaPlayer();
        try{
            mp.setDataSource(filepath);
            mp.prepare();
            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    if (width<height){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        showVideo(filepath);
                    }else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        showVideo(filepath);
                    }
                }
            });
        }
         catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (exoPlayer != null){
            exoPlayer.release();
        }
        if (exoPlayer.isLoading()){
            exoPlayer.release();
        }
        if (exoPlayer.isPlayingAd()){
            exoPlayer.release();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null){
            exoPlayer.release();
        }
        if (exoPlayer.isLoading()){
            exoPlayer.release();
        }
        if (exoPlayer.isPlayingAd()){
            exoPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (exoPlayer != null){
            exoPlayer.release();
        }
        if (exoPlayer.isLoading()){
            exoPlayer.release();
        }
        if (exoPlayer.isPlayingAd()){
            exoPlayer.release();
        }
    }

}
