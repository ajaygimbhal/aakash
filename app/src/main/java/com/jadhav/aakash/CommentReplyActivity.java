package com.jadhav.aakash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jadhav.aakash.databinding.ActivityCommentReplyBinding;

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