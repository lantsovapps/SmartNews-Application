package com.lantsovapps.newsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    private TextView tvLogo;
    private String text;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tvLogo = (TextView) findViewById(R.id.tVLogo);
        text = "<font color=\"#E4841D\">Smart</font>News";
        tvLogo.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        //Animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_animation);
        tvLogo.startAnimation(animation);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(1500);
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
