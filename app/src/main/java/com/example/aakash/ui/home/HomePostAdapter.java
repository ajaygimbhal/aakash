package com.example.aakash.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aakash.CommentActivity;
import com.example.aakash.R;
import com.example.aakash.databinding.PostCardViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.HomePostHolder> {

    ArrayList<HomePostModel> modelArrayList;
    Context context;

    public HomePostAdapter(ArrayList<HomePostModel> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomePostHolder(LayoutInflater.from(context).inflate(R.layout.post_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostHolder holder, int position) {
        holder.setPostData(modelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class HomePostHolder extends RecyclerView.ViewHolder {

        PostCardViewBinding binding;

        public HomePostHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostCardViewBinding.bind(itemView);
        }

        @SuppressLint("ResourceType")
        public void setPostData(HomePostModel homePostModel) {

            binding.postTitle.setText(homePostModel.getPostTitle());

            Picasso.get().load(homePostModel.getPostUserIcon()).into(binding.postUserIcon);
            Picasso.get().load(homePostModel.getPostImg()).into(binding.postImg);

            binding.postUsername.setText(homePostModel.getPostUsername());

            binding.commentBtn.setOnClickListener(view -> {
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            });
        }
    }
}
