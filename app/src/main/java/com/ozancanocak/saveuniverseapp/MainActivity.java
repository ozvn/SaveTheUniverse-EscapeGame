package com.ozancanocak.saveuniverseapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    int addTimer;
    int BugController;
    int timerBugController;
    ImageView leaderboard;


    // Frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;
    private LinearLayout startTheGame;



    // Image
    private ImageView box, black, orange, pink;
    private Drawable imageBoxRight, imageBoxLeft;
    private ImageView imageDown, imageUp;

    // Size
    private int boxSize;

    // Position
    private float boxX, boxY;
    float tutBoxX, tutBoxY;
    private float blackX, blackY;
    private float orangeX, orangeY;
    private float pinkX, pinkY;

    // Score
    private TextView scoreLabel, highScoreLabel;
    private TextView touchFor,secondG;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    // Class
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayer soundPlayer;

    // Status
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean pink_flg = false;
    boolean connected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        soundPlayer = new SoundPlayer(this);

        MobileAds.initialize(this,
                "ca-app-pub-9872856099372186~7785858626");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9872856099372186/5140896873");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        touchFor = findViewById(R.id.touchFor);
        secondG = findViewById(R.id.secondG);
        addTimer = 0;
        BugController = 1;
        timerBugController = 1;
        imageDown = findViewById(R.id.imageDown);
        imageUp = findViewById(R.id.imageUp);
        imageUp = findViewById(R.id.imageUp);
        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        startTheGame = findViewById(R.id.startTheGame);
        box = findViewById(R.id.box);
        black = findViewById(R.id.black);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highScoreLabel);

        imageBoxLeft = getResources().getDrawable(R.drawable.box_left);
        imageBoxRight = getResources().getDrawable(R.drawable.box_right);

        // High Score
        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);
        highScoreLabel.setText("High Score : " + highScore);

    }

    public void changePos() {

        int pwalk = startTheGame.getHeight();
        int bwalk = startTheGame.getHeight();
        int owalk = startTheGame.getHeight();
        owalk = owalk / 80;
        pwalk = pwalk / 70;


        if(score>=70 & score<200) {
            imageDown.setImageResource(R.drawable.parrot);
            imageUp.setImageResource(R.drawable.cloud);
            gameFrame.setBackgroundResource(R.drawable.gradients2);
        }

            if(score>=200 & score<500) {
            imageDown.setImageResource(R.drawable.zeppelin);
            imageUp.setImageResource(R.drawable.moon);
            gameFrame.setBackgroundResource(R.drawable.gradients3);
        }

        if(score>=500) {
            imageDown.setImageResource(R.drawable.saturn);
            imageUp.setImageResource(R.drawable.galaxy);
            gameFrame.setBackgroundResource(R.drawable.gradients4);
        }


        // Add timeCount
        timeCount += 20;

        // Orange
        orangeY += owalk;

        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight() / 2;

        if (hitCheck(orangeCenterX, orangeCenterY)) {
            orangeY = frameHeight + 100;
            score += 10;
            soundPlayer.playHitOrangeSound();
        }

        if (orangeY > frameHeight) {
            orangeY = -100;
            orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        // Pink
        if (!pink_flg && timeCount % 15000 == 0) {
            pink_flg = true;
            pinkY = -19;
            pinkX = (float) Math.floor(Math.random() * (frameWidth - pink.getWidth()));
        }

        if (pink_flg) {
            pinkY += pwalk;

            float pinkCenterX = pinkX + pink.getWidth() / 2;
            float pinkCenterY = pinkY + pink.getWidth() / 2;

            if (hitCheck(pinkCenterX, pinkCenterY)) {
                pinkY = frameHeight + 30;
                score += 30;
                // Change FrameWidth
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
                soundPlayer.playHitPinkSound();
            }

            if (pinkY > frameHeight) pink_flg = false;
            pink.setX(pinkX);
            pink.setY(pinkY);
        }

        // Black
            if(score>=0 & score<70) { bwalk = bwalk / 70; blackY += bwalk; }
            if(score>=70 & score<200) { bwalk = bwalk / 68; blackY += bwalk; }
            if(score>=200 & score<500) { bwalk = bwalk / 64; blackY += bwalk; }
            if(score>=500 & score<600) { bwalk = bwalk / 60; blackY += bwalk; }
            if(score>=600 & score<700) { bwalk = bwalk / 58; blackY += bwalk; }
            if(score>=700 & score<800)  { bwalk = bwalk / 56; blackY += bwalk; }
            if(score>=800)  { bwalk = bwalk / 56; blackY += bwalk; }


        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;

        if (hitCheck(blackCenterX, blackCenterY)) {
            blackY = frameHeight + 100;

            // Change FrameWidth
            frameWidth = frameWidth * 88 / 100;
            changeFrameWidth(frameWidth);
            soundPlayer.playHitBlackSound();
            if (frameWidth <= boxSize) {
                gameOver();
            }

        }

        if (blackY > frameHeight) {
            blackY = -100;
            blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
        }

        black.setX(blackX);
        black.setY(blackY);

        // Move Box

        int walk = startTheGame.getWidth();
        walk = walk/60;


        if (action_flg) {
            // Touching
            boxX += walk;
            box.setImageDrawable(imageBoxRight);
        } else {
            // Releasing
            boxX -= walk;
            box.setImageDrawable(imageBoxLeft);
        }

        // Check box position.
        if (boxX < 0) {
        //    boxX = 0;
        //    box.setX(tutBoxX);
            gameOver();
        }

        if (frameWidth - boxSize < boxX) {
        //    boxX = frameWidth - boxSize;
        //    box.setX(tutBoxX);
            gameOver();
        }

        box.setX(boxX);
        scoreLabel.setText("Score : " + score);

    }

    public boolean hitCheck(float x, float y) {
        if (boxX <= x && x <= boxX + boxSize &&
                boxY <= y && y <= frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver() {


        BugController = 1;
        // Stop timer.
        timer.cancel();
        timer = null;
        start_flg = false;
        action_flg = false;

        if(addTimer%5==0) {

            //REKLAM KODLARI...
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }


        }

        // Before showing startLayout, sleep 2 second.



        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                touchFor.setVisibility(View.VISIBLE);
                secondG.setVisibility(View.VISIBLE);


            }
        }.start();




        changeFrameWidth(initialFrameWidth);


        startLayout.setVisibility(View.VISIBLE);
     //   box.setVisibility(View.VISIBLE);
        black.setVisibility(View.INVISIBLE);
        orange.setVisibility(View.INVISIBLE);
        pink.setVisibility(View.INVISIBLE);



        boxX =  tutBoxX;
        boxY =  tutBoxY;


        // Update High Score
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score : " + highScore);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", highScore);
            editor.commit();
        }

        imageDown.setImageResource(R.drawable.dino);
        imageUp.setImageResource(R.drawable.rock);
        gameFrame.setBackgroundResource(R.drawable.gradients);
        startTheGame.setVisibility(View.VISIBLE);



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            }  else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
        return true;
    }

    public void startGame(final View view) {

        if (timerBugController == 1) {
            timerBugController = 0;

            startLayout.setVisibility(View.INVISIBLE);
            startTheGame.setVisibility(View.INVISIBLE);
            touchFor.setVisibility(View.INVISIBLE);

            new CountDownTimer(4200, 1000) {


                @Override
                public void onTick(long l) {


                    secondG.setText("3");


                    if (l < 3000 && l > 1900) {
                        secondG.setText("2");
                        soundPlayer.playHitOrangeSound();
                    }
                    if (l <= 1900) {
                        secondG.setText("1");
                        soundPlayer.playHitOrangeSound();
                    }

                }

                @Override
                public void onFinish() {

                    timerBugController = 1;
                    secondG.setText("");
                    secondG.setVisibility(View.INVISIBLE);


                    if (BugController == 1) {
                        BugController = 0;
                        addTimer++;
                        start_flg = true;
           //             startLayout.setVisibility(View.INVISIBLE);
           //             startTheGame.setVisibility(View.INVISIBLE);

                        if (frameHeight == 0) {
                            frameHeight = gameFrame.getHeight();
                            frameWidth = gameFrame.getWidth();
                            initialFrameWidth = frameWidth;

                            boxSize = box.getHeight();
                            boxX = box.getX();
                            boxY = box.getY();

                            tutBoxX = box.getX();
                            tutBoxY = box.getY();
                        }

                        frameWidth = initialFrameWidth;

                        box.setX(0.0f);
                        black.setY(3000.0f);
                        orange.setY(3000.0f);
                        pink.setY(3000.0f);

                        blackY = black.getY();
                        orangeY = orange.getY();
                        pinkY = pink.getY();

                        // box.setVisibility(View.VISIBLE);
                        black.setVisibility(View.VISIBLE);
                        orange.setVisibility(View.VISIBLE);
                        pink.setVisibility(View.VISIBLE);
                 //       touchFor.setVisibility(View.INVISIBLE);

                        timeCount = 0;
                        score = 0;
                        scoreLabel.setText("Score : 0");


                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (start_flg) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            changePos();
                                        }
                                    });
                                }
                            }
                        }, 0, 20);
                    }

                }
            }.start();

    }

    }

    public void GoTo() {



    }



    //oyunu kapat
    /*
    public void quitGame(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
    */

}
