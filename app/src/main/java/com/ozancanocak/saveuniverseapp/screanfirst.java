package com.ozancanocak.saveuniverseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.Toast;

public class screanfirst extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screanfirst);
        Thread myThread = new Thread() {

            @Override
            public void run() {
                ConstraintLayout layout;
                layout = findViewById(R.id.layout);
                SharedPreferences sharedTuto;
                sharedTuto = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int tuto = sharedTuto.getInt("tuto",0);
                try {
                    sleep(3000);
                    layout.setBackgroundResource(R.drawable.chback);
                    sleep(3000);
                      if(tuto==0) {
                        Intent intent2 = new Intent(getApplicationContext(), gameplayvideo.class);
                        startActivity(intent2);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
