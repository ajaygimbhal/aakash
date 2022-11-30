package com.jadhav.aakash.activities;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USERNAME;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.ActivityPostEditBinding;
import com.jadhav.aakash.supports.CompressImage;
import com.jadhav.aakash.supports.Post;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Toasty;
import com.jadhav.aakash.supports.User;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PostEditActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_REQUEST_CODE = 84233;
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

            Dexter.withContext(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                if (privateStorage.isConnectedToInternet()) {
                                    Intent i = new Intent();
                                    i.setType("image/*");
                                    i.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(i, "Select Picture"), IMAGE_PICK_REQUEST_CODE);
                                } else {
                                    Toasty.Message(getApplicationContext(), "Enable Internet Connection.");
                                }
                            } else {
                                Toasty.Message(getApplicationContext(), "Storage Read Write Permission Required.");
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
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
        if (postImageUrl != null) {

            String imageName = "post-" + postId + ".jpeg";
            Log.d(TAG, "editUploadPost: " + imageName);

            StorageReference riversRef = firebaseStorage.getReference().child("PostImage/" + imageName);
            UploadTask uploadTask = riversRef.putFile(Uri.fromFile(new File(new CompressImage(this).compressImages(postImageUrl))));
            uploadTask.addOnSuccessListener(taskSnapshot -> {

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

            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toasty.Message(this, "Post Image Upload Failed.");

            }).addOnProgressListener(snapshot -> {
                int percentage = (int) (100.0 * (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()));
                progressDialog.setMessage("Post Updating " + percentage + "%");
            });

        } else {
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
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            postImageUrl = getImagePathString(data.getData());

            binding.editPostImagePrev.setVisibility(View.VISIBLE);
            Picasso.get().load(new File(postImageUrl)).into(binding.editPostImagePrev);
            binding.editPostSubmitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.active_button_bg));
            binding.editPostSubmitBtn.setEnabled(true);
            Log.d(TAG, "onActivityResult: " + postImageUrl);

        }
    }

    public String getImagePathString(Uri uri) {
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            if (document_id != null) {
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();
                cursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                cursor.moveToFirst();
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                return path;
            } else
                return getPath(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return getPath(uri);
        }
    }

    private String getPath(Uri uri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            return document_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}