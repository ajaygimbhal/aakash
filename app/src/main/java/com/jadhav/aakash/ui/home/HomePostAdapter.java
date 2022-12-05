package com.jadhav.aakash.ui.home;

import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.R;
import com.jadhav.aakash.activities.CommentActivity;
import com.jadhav.aakash.databinding.PostCardViewBinding;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Toasty;
import com.jadhav.aakash.supports.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SAMPLE_POST = 0;
    private static final int REAL_POST = 2;
    ArrayList<HomePostModel> modelArrayList;
    Context context;
    FirebaseDatabase firebaseDatabase;
    PrivateStorage privateStorage;


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
                ((HomePostHolder) holder).setPostData(modelArrayList.get(position), position);
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
            firebaseDatabase = FirebaseDatabase.getInstance();
            privateStorage = new PrivateStorage(context);
        }

        @SuppressLint("ResourceType")
        public void setPostData(HomePostModel homePostModel, int position) {

            if (!homePostModel.getPostTitle().equals("")) {
                binding.postTitle.setVisibility(View.VISIBLE);
                binding.postTitle.setText(homePostModel.getPostTitle());
            }

            firebaseDatabase.getReference("Users/" + homePostModel.getPostUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public synchronized void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                String postUserName = user.getUsername();
                                String postUserIcon = user.getProfileImg();
                                modelArrayList.get(position).setPostUsername(postUserName);
                                modelArrayList.get(position).setPostUserIcon(postUserIcon);
                                Picasso.get().load(homePostModel.getPostUserIcon()).into(binding.postUserIcon);
                                binding.postUsername.setText(homePostModel.getPostUsername());

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            Picasso.get().load(homePostModel.getPostImg()).into(binding.postImg);


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


                firebaseDatabase.getReference("Users/" + homePostModel.getPostUserId() + "/members/" + privateStorage.userDetail().put(USER_ID, null))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    menuBuilder.getItem(0).setTitle("Cancel Member");
                                } else if (homePostModel.getPostUserId().equals(privateStorage.userDetail().put(USER_ID, null))) {
                                    menuBuilder.getItem(0).setTitle("Self Member");
                                } else {
                                    menuBuilder.getItem(0).setTitle("Join Member");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                menuPopupHelper.setForceShowIcon(true);
                menuPopupHelper.setGravity(Gravity.BOTTOM);

                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.memberOptBtn:
                                if (privateStorage.isConnectedToInternet()) {
                                    // member count //
                                    firebaseDatabase.getReference("Users/" + homePostModel.getPostUserId() + "/members/")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    long membersCount = snapshot.getChildrenCount();
                                                    // exists member check
                                                    firebaseDatabase.getReference("Users/" + homePostModel.getPostUserId() + "/members/" + privateStorage.userDetail().put(USER_ID, null))
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()) {

                                                                        firebaseDatabase.getReference("Users")
                                                                                .child(homePostModel.getPostUserId())
                                                                                .child("members")
                                                                                .child(privateStorage.userDetail().put(USER_ID, null))
                                                                                .removeValue()
                                                                                .addOnSuccessListener(unused -> {
                                                                                    firebaseDatabase.getReference("Users")
                                                                                            .child(homePostModel.getPostUserId())
                                                                                            .child("memberCount")
                                                                                            .setValue(membersCount - 1)
                                                                                            .addOnSuccessListener(unused1 -> {
                                                                                                Toasty.Message(context, "Cancel by " + homePostModel.getPostUsername() + " Successfully.");
                                                                                            }).addOnFailureListener(e -> {
                                                                                                Toasty.Message(context, "Cancel by " + homePostModel.getPostUsername() + " Failed.");
                                                                                            });
                                                                                }).addOnFailureListener(e -> {
                                                                                    Toasty.Message(context, "Failed");
                                                                                });
                                                                    } else if (homePostModel.getPostUserId().equals(privateStorage.userDetail().put(USER_ID, null))) {
                                                                        Toasty.Message(context, "Sorry Your Are Self Post Member");
                                                                    } else {

                                                                        String memberAt = String.valueOf(System.currentTimeMillis());
                                                                        firebaseDatabase.getReference("Users")
                                                                                .child(homePostModel.getPostUserId())
                                                                                .child("members")
                                                                                .child(privateStorage.userDetail().put(USER_ID, null))
                                                                                .child("memberAt")
                                                                                .setValue(memberAt)
                                                                                .addOnSuccessListener(unused -> {
                                                                                    firebaseDatabase.getReference("Users")
                                                                                            .child(homePostModel.getPostUserId())
                                                                                            .child("memberCount")
                                                                                            .setValue(membersCount + 1)
                                                                                            .addOnSuccessListener(unused1 -> {
                                                                                                Toasty.Message(context, "Member by " + homePostModel.getPostUsername() + " Successfully.");
                                                                                            }).addOnFailureListener(e -> {
                                                                                                Toasty.Message(context, "Member by " + homePostModel.getPostUsername() + " Failed.");
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

                                return true;
                        }
                        return false;
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });

                menuPopupHelper.show();
            });

            firebaseDatabase.getReference("Posts/" + homePostModel.getPostId() + "/likes")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int likeCount = (int) snapshot.getChildrenCount();
                            String likeString;
                            if (likeCount > 1) {
                                if (likeCount > 9) {
                                    likeString = "Likes(" + likeCount + ")";
                                } else {
                                    likeString = "Likes(0" + likeCount + ")";
                                }

                            } else {
                                likeString = "Like(0" + likeCount + ")";
                            }
                            binding.likeBtn.setText(likeString);

                            firebaseDatabase.getReference("Posts/" + homePostModel.getPostId() + "/likes/" + privateStorage.userDetail().put(USER_ID, null))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                binding.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_fill, 0, 0, 0);

                                                binding.likeBtn.setOnClickListener(view -> {
                                                    firebaseDatabase.getReference("Posts/" + homePostModel.getPostId() + "/likes")
                                                            .child(privateStorage.userDetail().put(USER_ID, null))
                                                            .removeValue();
                                                });

                                            } else {
                                                binding.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up, 0, 0, 0);

                                                binding.likeBtn.setOnClickListener(view -> {
                                                    firebaseDatabase.getReference("Posts/" + homePostModel.getPostId() + "/likes")
                                                            .child(privateStorage.userDetail().put(USER_ID, null))
                                                            .child("likeAt")
                                                            .setValue(System.currentTimeMillis());
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

        }

    }

}
