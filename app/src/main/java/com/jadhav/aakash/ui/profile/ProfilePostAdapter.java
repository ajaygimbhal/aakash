package com.jadhav.aakash.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.ProfilePostCardViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ProfilePostHolder> {

    ArrayList<ProfilePostModel> profilePostModels;
    Context context;

    public ProfilePostAdapter(ArrayList<ProfilePostModel> profilePostModels, Context context) {
        this.profilePostModels = profilePostModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfilePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfilePostHolder(LayoutInflater.from(context).inflate(R.layout.profile_post_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostHolder holder, int position) {
        holder.setProfilePostData(profilePostModels.get(position));
    }

    @Override
    public int getItemCount() {
        return profilePostModels.size();
    }

    public class ProfilePostHolder extends RecyclerView.ViewHolder {
        ProfilePostCardViewBinding binding;

        public ProfilePostHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProfilePostCardViewBinding.bind(itemView);
        }

        public void setProfilePostData(ProfilePostModel profilePostModel) {
            Picasso.get().load(profilePostModel.getPostImage()).into(binding.postImgUrl);
        }
    }
}
