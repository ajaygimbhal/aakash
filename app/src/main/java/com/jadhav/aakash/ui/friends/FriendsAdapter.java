package com.jadhav.aakash.ui.friends;

import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.FriendCardViewBinding;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Toasty;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SAMPLE_VIEW = 0;
    private static final int REAL_VIEW = 2;
    ArrayList<FriendsModel> friendsModelArrayList;
    Context context;
    PrivateStorage privateStorage;

    public FriendsAdapter(ArrayList<FriendsModel> friendsModelArrayList, Context context) {
        this.friendsModelArrayList = friendsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SAMPLE_VIEW:
                return new SampleFriendsHolder(LayoutInflater.from(context).inflate(R.layout.sample_friend_card_view, parent, false));
            case REAL_VIEW:
                return new FriendsHolder(LayoutInflater.from(context).inflate(R.layout.friend_card_view, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SAMPLE_VIEW:
                break;
            case REAL_VIEW:
                ((FriendsHolder) holder).setFriendsData(friendsModelArrayList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (friendsModelArrayList.isEmpty()) {
            return 10;
        } else {
            return friendsModelArrayList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (friendsModelArrayList.isEmpty()) {
            return SAMPLE_VIEW;
        } else {
            return REAL_VIEW;
        }
    }

    public class FriendsHolder extends RecyclerView.ViewHolder {

        FriendCardViewBinding binding;
        FirebaseDatabase firebaseDatabase;

        public FriendsHolder(@NonNull View itemView) {
            super(itemView);
            binding = FriendCardViewBinding.bind(itemView);
            firebaseDatabase = FirebaseDatabase.getInstance();
            privateStorage = new PrivateStorage(context);
        }

        public void setFriendsData(FriendsModel friendsModel) {
            Picasso.get().load(friendsModel.getFriendIconUrl()).into(binding.friendsIcon);
            binding.friendsUsername.setText(friendsModel.getFriendUsername());
            String member = friendsModel.getFriendMemberCount() > 9 ? "Members: " + friendsModel.getFriendMemberCount() :
                    friendsModel.getFriendMemberCount() > 1 ? "Members: 0" + friendsModel.getFriendMemberCount() : "Member: 0" + friendsModel.getFriendMemberCount();

            binding.friendsMember.setText(member);

            firebaseDatabase.getReference("Users/" + friendsModel.getFriendId() + "/members/" + privateStorage.userDetail().put(USER_ID, null))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                binding.memberBtn.setText("Cancel");
                                binding.memberBtn.setTextColor(context.getResources().getColor(R.color.black));
                                binding.memberBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.de_active_button_bg));
                            } else {
                                binding.memberBtn.setText("Member");
                                binding.memberBtn.setTextColor(context.getResources().getColor(R.color.white));
                                binding.memberBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.active_button_bg));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            binding.memberBtn.setOnClickListener(view -> {

                if (privateStorage.isConnectedToInternet()) {
                    // member count //
                    firebaseDatabase.getReference("Users/" + friendsModel.getFriendId() + "/members/")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    long membersCount = snapshot.getChildrenCount();
                                    // exists member check
                                    firebaseDatabase.getReference("Users/" + friendsModel.getFriendId() + "/members/" + privateStorage.userDetail().put(USER_ID, null))
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {

                                                        firebaseDatabase.getReference("Users")
                                                                .child(friendsModel.getFriendId())
                                                                .child("members")
                                                                .child(privateStorage.userDetail().put(USER_ID, null))
                                                                .removeValue()
                                                                .addOnSuccessListener(unused -> {
                                                                    firebaseDatabase.getReference("Users")
                                                                            .child(friendsModel.getFriendId())
                                                                            .child("memberCount")
                                                                            .setValue(membersCount - 1)
                                                                            .addOnSuccessListener(unused1 -> {
                                                                                Toasty.Message(context, "Cancel by " + friendsModel.getFriendUsername() + " Successfully.");
                                                                            }).addOnFailureListener(e -> {
                                                                                Toasty.Message(context, "Cancel by " + friendsModel.getFriendUsername() + " Failed.");
                                                                            });
                                                                }).addOnFailureListener(e -> {
                                                                    Toasty.Message(context, "Failed");
                                                                });
                                                    } else {

                                                        String memberAt = String.valueOf(System.currentTimeMillis());
                                                        firebaseDatabase.getReference("Users")
                                                                .child(friendsModel.getFriendId())
                                                                .child("members")
                                                                .child(privateStorage.userDetail().put(USER_ID, null))
                                                                .child("memberAt")
                                                                .setValue(memberAt)
                                                                .addOnSuccessListener(unused -> {
                                                                    firebaseDatabase.getReference("Users")
                                                                            .child(friendsModel.getFriendId())
                                                                            .child("memberCount")
                                                                            .setValue(membersCount + 1)
                                                                            .addOnSuccessListener(unused1 -> {
                                                                                Toasty.Message(context, "Member by " + friendsModel.getFriendUsername() + " Successfully.");
                                                                            }).addOnFailureListener(e -> {
                                                                                Toasty.Message(context, "Member by " + friendsModel.getFriendUsername() + " Failed.");
                                                                            });
                                                                }).addOnFailureListener(e -> {
                                                                    Toasty.Message(context, "Failed");
                                                                });

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                } else {
                    Toasty.Message(context, "Enable Internet Connection.");
                }

            });
        }
    }

    public class SampleFriendsHolder extends RecyclerView.ViewHolder {
        public SampleFriendsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
