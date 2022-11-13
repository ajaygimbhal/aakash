package com.example.aakash.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aakash.R;
import com.example.aakash.databinding.ProfileMemberCardViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileMemberAdapter extends RecyclerView.Adapter<ProfileMemberAdapter.ProfileMemberHolder> {

    ArrayList<ProfileMemberModel> memberModelArrayList;
    Context context;

    public ProfileMemberAdapter(ArrayList<ProfileMemberModel> memberModelArrayList, Context context) {
        this.memberModelArrayList = memberModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileMemberHolder(LayoutInflater.from(context).inflate(R.layout.profile_member_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileMemberHolder holder, int position) {
        holder.setProfileMemberData(memberModelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return memberModelArrayList.size();
    }

    public class ProfileMemberHolder extends RecyclerView.ViewHolder {

        ProfileMemberCardViewBinding binding;

        public ProfileMemberHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProfileMemberCardViewBinding.bind(itemView);
        }

        public void setProfileMemberData(ProfileMemberModel profileMemberModel) {
            Picasso.get().load(profileMemberModel.getMemberIcon()).into(binding.memberIcon);

        }
    }
}
