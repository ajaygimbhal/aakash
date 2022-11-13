package com.example.aakash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aakash.databinding.ActivityCommentReplyBinding;

public class CommentReplyActivity extends AppCompatActivity {

    ActivityCommentReplyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
    }
}