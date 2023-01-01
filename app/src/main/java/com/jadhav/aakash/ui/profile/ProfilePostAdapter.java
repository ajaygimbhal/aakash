package com.jadhav.aakash.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jadhav.aakash.R;
import com.jadhav.aakash.activities.CommentActivity;
import com.jadhav.aakash.activities.PostEditActivity;
import com.jadhav.aakash.databinding.ProfilePostCardViewBinding;
import com.jadhav.aakash.supports.Toasty;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int REAL_POST = 0;
    private static final int SAMPLE_POST = 1;
    ArrayList<ProfilePostModel> profilePostModels;
    Context context;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;

    public ProfilePostAdapter(ArrayList<ProfilePostModel> profilePostModels, Context context) {
        this.profilePostModels = profilePostModels;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case REAL_POST:
                return new ProfilePostHolder(LayoutInflater.from(context).inflate(R.layout.profile_post_card_view, parent, false));
            case SAMPLE_POST:
                return new SampleProfilePostHolder(LayoutInflater.from(context).inflate(R.layout.sample_profile_post_card_view, parent, false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case REAL_POST:
                ((ProfilePostHolder) holder).setProfilePostData(profilePostModels.get(position));
                break;
            case SAMPLE_POST:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (profilePostModels.isEmpty()) {
            return 4;
        } else {
            return profilePostModels.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (profilePostModels.isEmpty()) {
            return SAMPLE_POST;
        } else {
            return REAL_POST;
        }
    }

    public class ProfilePostHolder extends RecyclerView.ViewHolder {
        ProfilePostCardViewBinding binding;

        public ProfilePostHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProfilePostCardViewBinding.bind(itemView);
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Post Deleting...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        }

        public void setProfilePostData(ProfilePostModel profilePostModel) {
            binding.postTitle.setText(profilePostModel.getPostTitle());
            Picasso.get().load(profilePostModel.getPostImage()).into(binding.postImgUrl);
            // post edit button
            binding.postEditBtn.setOnClickListener(view -> {
                Intent intent = new Intent(context, PostEditActivity.class);
                intent.putExtra("postId", profilePostModel.getPostId());
                context.startActivity(intent);
            });
            // post delete button
            binding.postDelBtn.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage(profilePostModel.getPostTitle());
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    progressDialog.show();
                    firebaseDatabase.getReference("Posts")
                            .child(profilePostModel.getPostId())
                            .removeValue().addOnSuccessListener(unused -> {
                                firebaseStorage.getReferenceFromUrl(profilePostModel.getPostImage()).delete();

                                firebaseDatabase.getReference("Notifications")
                                        .orderByChild("nPostId").equalTo(profilePostModel.getPostId())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        firebaseDatabase.getReference("Notifications/" + dataSnapshot.getKey()).removeValue();
                                                    }
                                                    progressDialog.dismiss();
                                                    Toasty.Message(context, "Post Delete Success");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toasty.Message(context, "Post Delete Failed");
                            });

                });
                builder.setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            });

            // single post open
            binding.postImgUrl.setOnClickListener(view -> {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", profilePostModel.getPostId());
                context.startActivity(intent);
            });
        }
    }

    public class SampleProfilePostHolder extends RecyclerView.ViewHolder {
        public SampleProfilePostHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
