package com.jadhav.aakash.ui.profile;

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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jadhav.aakash.databinding.FragmentProfileBinding;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int IMAGE_PICK_REQUEST_CODE = 909;
    private static final String TAG = "ProfileFragment";
    FragmentProfileBinding binding;
    ArrayList<ProfileMemberModel> memberModels = new ArrayList<>();
    ProfileMemberAdapter memberAdapter;

    ArrayList<ProfilePostModel> profilePostModels = new ArrayList<>();
    ProfilePostAdapter postAdapter;
    ProgressDialog progressDialog;
    FirebaseStorage firebaseStorage;
    PrivateStorage privateStorage;
    FirebaseDatabase firebaseDatabase;
    private int loadLimit = 4;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        privateStorage = new PrivateStorage(getContext());

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Profile Updating...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        binding.profileImgBtn.setOnClickListener(view -> {


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

        binding.logout.setOnClickListener(view -> privateStorage.logout());

        selfProfile();

        // Member Configuration
        loadMemberData();

        // Post Configuration
        loadPostData();

        // Post Scroll Load More Data
        binding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                    loadPostData();

                }
            }
        });

        return binding.getRoot();
    }


    private void selfProfile() {
        if (privateStorage.isUserLogin()) {

            binding.userProfileName.setText(privateStorage.userDetail().put(USERNAME, null));
            try {
                byte[] imageBytes = Base64.decode(privateStorage.userDetail().put(PROFILE_BITMAP_IMAGE, null), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.userProfileImg.setImageBitmap(decodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }


            int totalMember = privateStorage.getUserMemberCount();
            String memberText = totalMember > 9 ? "Members: " + totalMember : totalMember > 1 ? "Members: 0" + totalMember : "Member: 0" + totalMember;
            binding.userProfileMember.setText(memberText);

            firebaseDatabase.getReference("Users/" + privateStorage.userDetail().put(USER_ID, null))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                int memberCount = user.getMemberCount();
                                privateStorage.setUserMemberCount(memberCount);

                                String member = memberCount > 9 ? "Members: " + memberCount : memberCount > 1 ? "Members: 0" + memberCount : "Member: 0" + memberCount;
                                binding.userProfileMember.setText(member);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }

    private void loadMemberData() {
        // only recent 10 members load //
        if (privateStorage.isUserLogin()) {
            int totalMember = privateStorage.getUserMemberCount();

            if (totalMember >= 1) {
                if (totalMember > 1) {
                    binding.recentMember.setText("Recent Members");
                } else {
                    binding.recentMember.setText("Recent Member");
                }
                binding.recentMember.setVisibility(View.VISIBLE);
                binding.memberRecycler.setVisibility(View.VISIBLE);
            } else {
                binding.recentMember.setVisibility(View.GONE);
                binding.memberRecycler.setVisibility(View.GONE);
            }
        }

        memberAdapter = new ProfileMemberAdapter(memberModels, getContext());
        binding.memberRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.memberRecycler.setAdapter(memberAdapter);
        memberAdapter.notifyDataSetChanged();

        firebaseDatabase.getReference("Users/" + privateStorage.userDetail().put(USER_ID, null) + "/members")
                .limitToLast(10)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            long totalMember = snapshot.getChildrenCount();
                            if (totalMember > 1) {
                                binding.recentMember.setText("Recent Members");
                            } else {
                                binding.recentMember.setText("Recent Member");
                            }
                            binding.recentMember.setVisibility(View.VISIBLE);
                            binding.memberRecycler.setVisibility(View.VISIBLE);
                            memberModels.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String friendId = dataSnapshot.getKey();
                                firebaseDatabase.getReference("Users/" + friendId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    User user = snapshot.getValue(User.class);
                                                    String memberId = friendId;
                                                    String memberIcon = user.getProfileImg();
                                                    memberModels.add(new ProfileMemberModel(memberId, memberIcon));

                                                }
                                                Collections.reverse(memberModels);
                                                memberAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            }
                        } else {
                            binding.recentMember.setVisibility(View.GONE);
                            binding.memberRecycler.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadPostData() {

        if (privateStorage.isUserLogin()) {

            postAdapter = new ProfilePostAdapter(profilePostModels, getContext());
            binding.postRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
            binding.postRecycler.setAdapter(postAdapter);
            postAdapter.notifyDataSetChanged();

            firebaseDatabase.getReference("Posts")
                    .orderByChild("postUserId")
                    .equalTo(privateStorage.userDetail().put(USER_ID, null))
                    .limitToLast(loadLimit)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                binding.postNotFound.setVisibility(View.GONE);
                                binding.postRecycler.setVisibility(View.VISIBLE);
                                profilePostModels.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Post post = dataSnapshot.getValue(Post.class);
                                    String postId = dataSnapshot.getKey();
                                    String postTitle = post.getPostTitle();
                                    String postImage = post.getPostImageUrl();

                                    profilePostModels.add(new ProfilePostModel(postId, postTitle, postImage));
                                }
                                Collections.reverse(profilePostModels);
                                loadLimit += 10;
                                postAdapter.notifyDataSetChanged();
                            } else {

                                binding.postRecycler.setVisibility(View.GONE);
                                binding.postNotFound.setVisibility(View.VISIBLE);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            String selectedImage = getImagePathString(data.getData());
            if (selectedImage != null) {

                progressDialog.show();

                StorageReference imagesRef = firebaseStorage.getReference().child("ProfileImg/" + privateStorage.userDetail().put(USER_ID, null) + ".jpeg");

                Bitmap bitmap = BitmapFactory.decodeFile(new CompressImage(getContext()).compressImages(selectedImage));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] bytes = baos.toByteArray();
                UploadTask uploadTask = imagesRef.putBytes(bytes);
                uploadTask.addOnSuccessListener(taskSnapshot -> {

                    firebaseStorage.getReference().child("ProfileImg/" + privateStorage.userDetail().put(USER_ID, null) + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firebaseDatabase.getReference("Users/" + privateStorage.userDetail().put(USER_ID, null))
                                    .child("profileImg")
                                    .setValue(uri.toString())
                                    .addOnCompleteListener(task -> {

                                        privateStorage.userProfileImage(userPUrl(uri.toString()));
                                        selfProfile();
                                        progressDialog.dismiss();
                                        Toasty.Message(getContext(), "Profile Update Success.");

                                    }).addOnCanceledListener(() -> {
                                        progressDialog.dismiss();
                                        Toasty.Message(getContext(), "Profile Update Cancel.");
                                    });

                        }
                    });


                }).addOnCanceledListener(() -> {
                    progressDialog.dismiss();
                    Toasty.Message(getContext(), "Profile Update Cancel.");
                }).addOnProgressListener(snapshot -> {
                    int percentage = (int) (100.0 * (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()));
                    progressDialog.setMessage("Profile Updating " + percentage + "%");
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toasty.Message(getContext(), "Profile Update Failed.");
                });

            } else {
                Toasty.Message(getContext(), "Profile Image can't update.");
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

    private String userPUrl(String url) {
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL uri = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}