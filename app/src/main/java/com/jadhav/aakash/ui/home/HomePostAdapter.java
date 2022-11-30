package com.jadhav.aakash.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.activities.CommentActivity;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.PostCardViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SAMPLE_POST = 0;
    private static final int REAL_POST = 2;
    ArrayList<HomePostModel> modelArrayList;
    Context context;


    public HomePostAdapter(ArrayList<HomePostModel> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SAMPLE_POST:
                return new SampleHomePostHolder(LayoutInflater.from(context).inflate(R.layout.sample_post_card_view, parent, false));
            case REAL_POST:
                return new HomePostHolder(LayoutInflater.from(context).inflate(R.layout.post_card_view, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SAMPLE_POST:
                break;
            case REAL_POST:
                ((HomePostHolder) holder).setPostData(modelArrayList.get(position));
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (modelArrayList.isEmpty()) {
            return 5;
        } else {
            return modelArrayList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (modelArrayList.isEmpty()) {
            return SAMPLE_POST;
        } else {
            return REAL_POST;
        }
    }

    public class SampleHomePostHolder extends RecyclerView.ViewHolder {
        public SampleHomePostHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class HomePostHolder extends RecyclerView.ViewHolder {

        PostCardViewBinding binding;

        public HomePostHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostCardViewBinding.bind(itemView);
        }

        @SuppressLint("ResourceType")
        public void setPostData(HomePostModel homePostModel) {

            if (!homePostModel.getPostTitle().equals("")) {
                binding.postTitle.setVisibility(View.VISIBLE);
                binding.postTitle.setText(homePostModel.getPostTitle());
            }

            Picasso.get().load(homePostModel.getPostUserIcon()).into(binding.postUserIcon);
            Picasso.get().load(homePostModel.getPostImg()).into(binding.postImg);

            binding.postUsername.setText(homePostModel.getPostUsername());

            binding.commentBtn.setOnClickListener(view -> {
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            });

            binding.postMoreBtn.setOnClickListener(view -> {
                View view1 = binding.moreMenuView;

                MenuBuilder menuBuilder = new MenuBuilder(context);

                MenuInflater inflater = new MenuInflater(context);
                inflater.inflate(R.menu.post_more_menu, menuBuilder);

                MenuPopupHelper menuPopupHelper = new MenuPopupHelper(context, menuBuilder, view1);
                menuBuilder.getItem(0).setTitle("Member Cancel");

                menuPopupHelper.setForceShowIcon(true);
                menuPopupHelper.setGravity(Gravity.BOTTOM);

                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        Toast.makeText(context, "You Clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });

                menuPopupHelper.show();
            });
        }

    }

}
