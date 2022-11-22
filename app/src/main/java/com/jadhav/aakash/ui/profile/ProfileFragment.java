package com.jadhav.aakash.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.databinding.FragmentProfileBinding;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    ArrayList<ProfileMemberModel> memberModels = new ArrayList<>();
    ProfileMemberAdapter memberAdapter;

    ArrayList<ProfilePostModel> profilePostModels = new ArrayList<>();
    ProfilePostAdapter postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Member Configuration
        memberModels.add(new ProfileMemberModel("123", "https://images.pexels.com/photos/15286/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=600"));
        memberModels.add(new ProfileMemberModel("123", "https://images.pexels.com/photos/1591373/pexels-photo-1591373.jpeg?auto=compress&cs=tinysrgb&w=600"));
        memberModels.add(new ProfileMemberModel("123", "https://images.pexels.com/photos/235621/pexels-photo-235621.jpeg?auto=compress&cs=tinysrgb&w=600"));
        memberModels.add(new ProfileMemberModel("123", "https://images.pexels.com/photos/3408744/pexels-photo-3408744.jpeg?auto=compress&cs=tinysrgb&w=600"));
        memberModels.add(new ProfileMemberModel("123", "https://images.pexels.com/photos/15286/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=600"));
        memberModels.add(new ProfileMemberModel("123", "https://images.pexels.com/photos/1591373/pexels-photo-1591373.jpeg?auto=compress&cs=tinysrgb&w=600"));
        memberModels.add(new ProfileMemberModel("123", "https://images.pexels.com/photos/235621/pexels-photo-235621.jpeg?auto=compress&cs=tinysrgb&w=600"));
        memberModels.add(new ProfileMemberModel("123", "https://images.pexels.com/photos/3408744/pexels-photo-3408744.jpeg?auto=compress&cs=tinysrgb&w=600"));

        memberAdapter = new ProfileMemberAdapter(memberModels, getContext());
        binding.memberRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.memberRecycler.setAdapter(memberAdapter);
        memberAdapter.notifyDataSetChanged();

        // Post Configuration
        profilePostModels.add(new ProfilePostModel("12", "https://images.pexels.com/photos/3484061/pexels-photo-3484061.jpeg?auto=compress&cs=tinysrgb&w=600"));
        profilePostModels.add(new ProfilePostModel("12", "https://images.pexels.com/photos/7604425/pexels-photo-7604425.jpeg?auto=compress&cs=tinysrgb&w=600"));
        profilePostModels.add(new ProfilePostModel("12", "https://images.pexels.com/photos/1292115/pexels-photo-1292115.jpeg?auto=compress&cs=tinysrgb&w=600"));
        profilePostModels.add(new ProfilePostModel("12", "https://images.pexels.com/photos/3484061/pexels-photo-3484061.jpeg?auto=compress&cs=tinysrgb&w=600"));
        profilePostModels.add(new ProfilePostModel("12", "https://images.pexels.com/photos/7604425/pexels-photo-7604425.jpeg?auto=compress&cs=tinysrgb&w=600"));
        profilePostModels.add(new ProfilePostModel("12", "https://images.pexels.com/photos/1292115/pexels-photo-1292115.jpeg?auto=compress&cs=tinysrgb&w=600"));

        postAdapter = new ProfilePostAdapter(profilePostModels, getContext());
        binding.postRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        binding.postRecycler.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

        return binding.getRoot();
    }
}