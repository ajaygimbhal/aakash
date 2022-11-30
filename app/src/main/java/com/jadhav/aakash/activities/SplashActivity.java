package com.jadhav.aakash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.jadhav.aakash.databinding.ActivitySplashBinding;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.FullScreen;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    View view;
    FullScreen fullScreen;
    PrivateStorage privateStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        privateStorage = new PrivateStorage(this);


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
                    if (privateStorage.isUserLogin()) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                    finish();
                }
            }
        }.start();
    }

}