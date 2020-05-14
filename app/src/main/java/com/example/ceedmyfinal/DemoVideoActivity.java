package com.example.ceedmyfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.khizar1556.mkvideoplayer.MKPlayer;

public class DemoVideoActivity extends AppCompatActivity
{

    public MKPlayer mkPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_video);

        mkPlayer = new MKPlayer(this);

        String videoLink = SharedPreferenceManager.getInstance(this).getUserExamDemoVideo();
        Log.d("TAG", "onCreate: "+videoLink);

        mkPlayer.play(videoLink);

        if(savedInstanceState != null)
        {
            mkPlayer.seekTo(savedInstanceState.getInt("currentposition"),false);
            mkPlayer.onResume();

        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mkPlayer.pause();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        mkPlayer.start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("currentposition", mkPlayer.getCurrentPosition());
    }
}
