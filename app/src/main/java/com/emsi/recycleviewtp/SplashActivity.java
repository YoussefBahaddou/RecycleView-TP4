package com.emsi.recycleviewtp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DURATION = 6000; // 6 seconds total for the animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            ImageView logo = findViewById(R.id.logo);

            if (logo != null) {
                // Enable hardware acceleration for the ImageView
                logo.setLayerType(ImageView.LAYER_TYPE_HARDWARE, null);

                // Sequence of animations with different durations
                logo.animate().rotation(360f).setDuration(2000);
                logo.animate().scaleX(0.5f).scaleY(0.5f).setDuration(3000);
                logo.animate().translationYBy(1000f).setDuration(2000);
                logo.animate().alpha(0f).setDuration(6000);

                Log.d(TAG, "Animation sequence started");
            } else {
                Log.e(TAG, "Logo ImageView not found");
            }

            // Transition to the ListActivity after the animation
            // Using 6000ms (6 seconds) to match the longest animation duration
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, ListActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }, SPLASH_DURATION);

        } catch (Exception e) {
            Log.e(TAG, "Error in splash screen: " + e.getMessage());
            // If there's an error, still proceed to the main activity
            startActivity(new Intent(SplashActivity.this, ListActivity.class));
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Clear any animations to prevent memory leaks
        try {
            ImageView logo = findViewById(R.id.logo);
            if (logo != null) {
                logo.clearAnimation();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error clearing animation: " + e.getMessage());
        }
    }
}
