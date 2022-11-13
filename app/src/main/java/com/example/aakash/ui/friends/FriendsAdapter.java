package com.example.aakash.ui.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aakash.R;
import com.example.aakash.databinding.FriendCardViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsHolder> {

    ArrayList<FriendsModel> friendsModelArrayList;
    Context context;

    public FriendsAdapter(ArrayList<FriendsModel> friendsModelArrayList, Context context) {
        this.friendsModelArrayList = friendsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsHolder(LayoutInflater.from(context).inflate(R.layout.friend_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsHolder holder, int position) {
         holder.setFriendsData(friendsModelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsModelArrayList.size();
    }

    public class FriendsHolder extends RecyclerView.ViewHolder{

        FriendCardViewBinding binding;
        public FriendsHolder(@NonNull View itemView) {
            super(itemView);
            binding = FriendCardViewBinding.bind(itemView);
        }

        public void setFriendsData(FriendsModel friendsModel) {
            Picasso.get().load(friendsModel.getFriendIconUrl()).into(binding.friendsIcon);
            binding.friendsUsername.setText(friendsModel.getFriendUsername());
            binding.friendsMember.setText("Member: "+ friendsModel.getFriendMembers());
        }
    }
}
