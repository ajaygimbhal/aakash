package com.jadhav.aakash.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.ProfileMemberCardViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int SAMPLE_MEMBER = 0;
    private final int FINAL_MEMBER = 2;
    ArrayList<ProfileMemberModel> memberModelArrayList;
    Context context;

    public ProfileMemberAdapter(ArrayList<ProfileMemberModel> memberModelArrayList, Context context) {
        this.memberModelArrayList = memberModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SAMPLE_MEMBER:
                return new SampleProfileMemberHolder(LayoutInflater.from(context).inflate(R.layout.sample_profile_member_card_view, parent, false));
            case FINAL_MEMBER:
                return new ProfileMemberHolder(LayoutInflater.from(context).inflate(R.layout.profile_member_card_view, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SAMPLE_MEMBER:
                break;
            case FINAL_MEMBER:
                ((ProfileMemberHolder) holder).setProfileMemberData(memberModelArrayList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (memberModelArrayList.isEmpty()) {
            return 10;
        } else {
            return memberModelArrayList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (memberModelArrayList.isEmpty()) {
            return SAMPLE_MEMBER;
        } else {
            return FINAL_MEMBER;
        }
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

    public class SampleProfileMemberHolder extends RecyclerView.ViewHolder {
        public SampleProfileMemberHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
