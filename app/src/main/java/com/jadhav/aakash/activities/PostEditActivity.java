package com.jadhav.aakash.activities;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USERNAME;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.ActivityPostEditBinding;
import com.jadhav.aakash.supports.Post;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Toasty;
import com.jadhav.aakash.supports.User;
import com.squareup.picasso.Picasso;

public class PostEditActivity extends AppCompatActivity {


    private static final String TAG = "PostEditActivity";
    ActivityPostEditBinding binding;
    PrivateStorage privateStorage;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;
    private String postImageUrl = null;
    private String postId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        privateStorage = new PrivateStorage(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Post...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        if (getIntent().getStringExtra("postId") != null) {
            postId = getIntent().getStringExtra("postId");
            loadOldData();
        }


        if (privateStorage.isUserLogin()) {

            binding.editUsername.setText(privateStorage.userDetail().put(USERNAME, null));
            int totalMember = privateStorage.getUserMemberCount();

            binding.editMember.setText(totalMember > 9 ? "Members: " + totalMember : totalMember > 1 ? "Members: 0" + totalMember : "Member: 0" + totalMember);

            firebaseDatabase.getReference("Users/" + privateStorage.userDetail().put(USER_ID, null))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                int memberCount = user.getMemberCount();

                                privateStorage.setUserMemberCount(memberCount);
                                String member = memberCount > 9 ? "Members: " + memberCount : memberCount > 1 ? "Members: 0" + memberCount : "Member: 0" + memberCount;
                                binding.editMember.setText(member);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            try {
                byte[] imageBytes = Base64.decode(privateStorage.userDetail().put(PROFILE_BITMAP_IMAGE, null), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.editProfileIcon.setImageBitmap(decodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        binding.editPostSubmitBtn.setOnClickListener(view -> {
            if (privateStorage.isConnectedToInternet()) {
                editedUploadPost();
            } else {
                Toasty.Message(this, "Enable Internet Connection.");
            }
        });

        binding.editChooseImageBtn.setOnClickListener(view -> {
            Toasty.Message(this, "Sorry Image change don't Allowed.");
        });
    }

    private void loadOldData() {
        progressDialog.show();

        firebaseDatabase.getReference("Posts/" + postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Post post = snapshot.getValue(Post.class);
                            binding.editPostTitleText.setText(post.getPostTitle());
                            binding.editPostImagePrev.setVisibility(View.VISIBLE);
                            Picasso.get().load(post.getPostImageUrl()).into(binding.editPostImagePrev);

                            binding.editPostSubmitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.active_button_bg));
                            binding.editPostSubmitBtn.setEnabled(true);

                        } else {
                            Toasty.Message(getApplicationContext(), "Something went wrong.");
                        }

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toasty.Message(getApplicationContext(), "Post Load Failed");
                        progressDialog.dismiss();
                    }
                });
    }

    private void editedUploadPost() {
        String postTitle = binding.editPostTitleText.getText().toString();
        progressDialog.show();

        progressDialog.setMessage("Post Updating ...");
        firebaseDatabase.getReference("Posts/" + postId)
                .child("postTitle")
                .setValue(postTitle)
                .addOnSuccessListener(unused -> {
                    Toasty.Message(this, "Post Updated Successfully.");
                    progressDialog.dismiss();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toasty.Message(this, "Title Update Failed.");
                    progressDialog.dismiss();
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}