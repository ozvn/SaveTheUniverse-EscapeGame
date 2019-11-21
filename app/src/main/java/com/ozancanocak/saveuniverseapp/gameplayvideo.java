package com.ozancanocak.saveuniverseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class gameplayvideo extends AppCompatActivity {

    VideoView videoV;
    TextView textView6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplayvideo);


        SavePreferences("tuto",1);

        videoV = findViewById(R.id.videoView);
        textView6 = findViewById(R.id.textView6);


        String videopath = "android.resource://com.ozancanocak.saveuniverseapp/"+R.raw.gameplay;
        Uri uri = Uri.parse(videopath);
        videoV.setVideoURI(uri);
        videoV.start();

        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long l) { textView6.setText(""+l/1000); }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }

        }.start();




    }

    private void SavePreferences(String key, int value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }


}
