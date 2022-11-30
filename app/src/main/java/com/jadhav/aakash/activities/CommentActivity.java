package com.jadhav.aakash.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jadhav.aakash.databinding.ActivityCommentBinding;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
    }
}