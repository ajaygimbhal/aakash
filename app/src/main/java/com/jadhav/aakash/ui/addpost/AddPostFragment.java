package com.jadhav.aakash.ui.addpost;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USERNAME;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;
import static com.jadhav.aakash.supports.PrivateStorage.USER_TOTAL_MEMBER;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.FragmentAddPostBinding;
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


public class AddPostFragment extends Fragment {


    private static final int IMAGE_PICK_REQUEST_CODE = 8323;
    private static final String TAG = "AddPostFragment";
    FragmentAddPostBinding binding;
    PrivateStorage privateStorage;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;
    private String postImageUrl = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        privateStorage = new PrivateStorage(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Post Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        if (privateStorage.isUserLogin()) {

            binding.username.setText(privateStorage.userDetail().put(USERNAME, null));
            int totalMember = privateStorage.getUserMemberCount();

            binding.member.setText(totalMember > 9 ? "Members: " + totalMember : totalMember > 1 ? "Members: 0" + totalMember : "Member: 0" + totalMember);

            firebaseDatabase.getReference("Users/" + privateStorage.userDetail().put(USER_ID, null))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                int memberCount = user.getMemberCount();

                                privateStorage.setUserMemberCount(memberCount);
                                String member = memberCount > 9 ? "Members: " + memberCount : memberCount > 1 ? "Members: 0" + memberCount : "Member: 0" + memberCount;
                                binding.member.setText(member);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            try {
                byte[] imageBytes = Base64.decode(privateStorage.userDetail().put(PROFILE_BITMAP_IMAGE, null), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.profileIcon.setImageBitmap(decodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        binding.postSubmitBtn.setOnClickListener(view -> {
            if (privateStorage.isConnectedToInternet()) {
                uploadPost();
            } else {
                Toasty.Message(getContext(), "Enable Internet Connection.");
            }
        });


        binding.chooseImageBtn.setOnClickListener(view -> {

            Dexter.withContext(getContext())
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
                                    Toasty.Message(getContext(), "Enable Internet Connection.");
                                }
                            } else {
                                Toasty.Message(getContext(), "Storage Read Write Permission Required.");
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        });


        return binding.getRoot();
    }

    private void uploadPost() {
        if (postImageUrl != null) {

            progressDialog.show();
            String postId = firebaseDatabase.getReference().child("Posts").push().getKey();
            String imageNameGen = "post-" + postId + ".jpg";
            Log.d(TAG, "uploadPost: " + imageNameGen);

            StorageReference riversRef = firebaseStorage.getReference().child("PostImage/" + imageNameGen);
            UploadTask uploadTask = riversRef.putFile(Uri.fromFile(new File(new CompressImage(getContext()).compressImages(postImageUrl))));
            uploadTask.addOnSuccessListener(taskSnapshot -> {

                String postTitle = binding.postTitleText.getText().toString();
                String postUserId = privateStorage.userDetail().put(USER_ID, null);
                String postDatetime = privateStorage.dateTime(System.currentTimeMillis());
                firebaseStorage.getReference().child("PostImage/" + imageNameGen).getDownloadUrl().addOnSuccessListener(uri -> {

                    Post post = new Post(postTitle, postUserId, uri.toString(), postDatetime.substring(0, postDatetime.lastIndexOf(" ")));
                    firebaseDatabase.getReference("Posts")
                            .child(postId)
                            .setValue(post)
                            .addOnSuccessListener(unused -> {

                                binding.postImagePrev.setVisibility(View.GONE);
                                binding.postTitleText.setText("");
                                binding.postSubmitBtn.setEnabled(false);
                                binding.postSubmitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.de_active_button_bg));
                                postImageUrl = null;

                                progressDialog.dismiss();
                                Toasty.Message(getContext(), "Post Upload Successfully.");
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toasty.Message(getContext(), "Post Upload Failed.");
                            });
                });


            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toasty.Message(getContext(), "Post Image Upload Failed.");

            }).addOnProgressListener(snapshot -> {
                int percentage = (int) (100.0 * (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()));
                progressDialog.setMessage("Post Uploading " + percentage + "%");
            });


        } else {
            Toasty.Message(getContext(), "Can't Be Selected Image");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            postImageUrl = getImagePathString(data.getData());


            if (postImageUrl != null) {
                binding.postImagePrev.setVisibility(View.VISIBLE);
                Picasso.get().load(new File(postImageUrl)).into(binding.postImagePrev);
                binding.postSubmitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.active_button_bg));
                binding.postSubmitBtn.setEnabled(true);
                Log.d(TAG, "onActivityResult: " + postImageUrl);
            } else {
                binding.postImagePrev.setVisibility(View.GONE);
                binding.postSubmitBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.de_active_button_bg));
                binding.postSubmitBtn.setEnabled(false);
            }

        }
    }

    public String getImagePathString(Uri uri) {
        try {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            if (document_id != null) {
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();
                cursor = getContext().getContentResolver().query(
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
            Cursor cursor = getContext().getContentResolver().query(uri, proj, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            return document_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}