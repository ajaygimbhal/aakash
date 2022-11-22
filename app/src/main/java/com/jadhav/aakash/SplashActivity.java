package com.jadhav.aakash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.jadhav.aakash.databinding.ActivitySplashBinding;
import com.jadhav.aakash.ui.supports.FullScreen;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    View view;
    FullScreen fullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        fullScreen = new FullScreen(view, getWindow());

        fullScreen.transparentStatusBarAndNavigation();


        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        }.start();
    }

}