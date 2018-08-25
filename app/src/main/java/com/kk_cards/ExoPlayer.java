package com.kk_cards;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import tcking.github.com.giraffeplayer2.VideoView;

public class ExoPlayer extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer player;
    String link;

    VideoView videoView;

    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";

    private boolean startAutoPlay;
    private int currentWindow;
    private long playbackPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        getSupportActionBar().hide();

        videoView = findViewById(R.id.video_view);



     /*   currentWindow = savedInstanceState.getInt(KEY_WINDOW);
        playbackPosition = savedInstanceState.getLong(KEY_POSITION);
        startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);*/

        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        // _mobileText.setText(num);


        videoView.setVideoPath(link).getPlayer().start();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //initializePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // hideSystemUi();
        // initializePlayer();
    }



/*    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

     *//*   player.setPlayWhenReady(startAutoPlay);
        player.seekTo(currentWindow, playbackPosition);*//*

        Uri uri = Uri.parse(link);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

    }*/


 /*   private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        //releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // releasePlayer();
    }

/*
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
*/


/*    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            startAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }*/

}
