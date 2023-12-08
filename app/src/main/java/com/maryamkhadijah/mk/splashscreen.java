package com.maryamkhadijah.mk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {

    private static int SPLASH = 2000;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashscreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH);

        // Start the progress bar animation
        animateProgressBar();
    }

    private void animateProgressBar() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int progress = progressBar.getProgress();
                if (progress < 100) {
                    progress += 1;
                    progressBar.setProgress(progress);
                    animateProgressBar();
                }
            }
        }, 20);
    }
}