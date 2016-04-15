package com.thetutors.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.VideoView;

import com.thetutors.R;

public class Animation extends ActionBarActivity {

    VideoView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        //instance the videoview layout
        animation = (VideoView) findViewById(R.id.animation);
        //get video from directory
        animation.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animation_0));
        //start the video
        animation.start();

        animation.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //force to change the page after the video finish
                Intent i = new Intent(Animation.this, AuthActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
