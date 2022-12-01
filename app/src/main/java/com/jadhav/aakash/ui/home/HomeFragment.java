package com.jadhav.aakash.ui.home;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.FragmentHomeBinding;
import com.jadhav.aakash.supports.Post;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.User;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private final String POST_ALL = "All";
    private final String POST_TODAY = "Today";
    private final String POST_MEMBERS = "Member Posts";
    private final String POST_TOP_LIKE = "Top like";
    private final String POST_YESTERDAY = "Yesterday";
    public TextView postNoExists;
    public RecyclerView postRecycler;
    private FragmentHomeBinding binding;
    private HomePostFilterAdapter filterAdapter;
    private ArrayList<HomePostFilterModel> filterModelArrayList = new ArrayList<>();
    private HomePostAdapter postAdapter;
    private ArrayList<HomePostModel> postModelArrayList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private PrivateStorage privateStorage;
    private int loadLimit = 5;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        firebaseDatabase = FirebaseDatabase.getInstance();
        privateStorage = new PrivateStorage(getContext());


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        postNoExists = binding.postNoExists;
        postRecycler = binding.postRecycler;

        // NAVBAR CODE //
        binding.navbar.title.setText(R.string.app_name);
        if (privateStorage.isUserLogin()) {
            try {
                byte[] imageBytes = Base64.decode(privateStorage.userDetail().put(PROFILE_BITMAP_IMAGE, null), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.navbar.personIcon.setImageBitmap(decodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // filter post recycler view //
        filterModelArrayList.clear();
        filterModelArrayList.add(new HomePostFilterModel("All", true));
        filterModelArrayList.add(new HomePostFilterModel("Today", false));
        filterModelArrayList.add(new HomePostFilterModel("Member Posts", false));
        filterModelArrayList.add(new HomePostFilterModel("Top like", false));
        filterModelArrayList.add(new HomePostFilterModel("Yesterday", false));
        filterAdapter = new HomePostFilterAdapter(filterModelArrayList, getContext(), new HomePostFilterAdapter.OnClickListener() {
            @Override
            public void OnClick(int position, String type) {
                filterModelArrayList.clear();
                filterModelArrayList.add(new HomePostFilterModel("All", false));
                filterModelArrayList.add(new HomePostFilterModel("Today", false));
                filterModelArrayList.add(new HomePostFilterModel("Member Posts", false));
                filterModelArrayList.add(new HomePostFilterModel("Top like", false));
                filterModelArrayList.add(new HomePostFilterModel("Yesterday", false));

                filterModelArrayList.get(position).setSelected(true);
                filterAdapter.notifyDataSetChanged();

                loadLimit = 5;
                postModelArrayList.clear();
                postAdapter.notifyDataSetChanged();
                postDataLoad(type);
            }
        });
        binding.filterRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.filterRecycler.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();


        // post recycler view //
        postDataLoad(POST_ALL);

        postAdapter = new HomePostAdapter(postModelArrayList, getContext());
        binding.postRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.postRecycler.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();


        return binding.getRoot();
    }

    private void postDataLoad(String type) {

        switch (type) {
            case POST_ALL:
                firebaseDatabase.getReference("Posts")
                        .limitToLast(loadLimit)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    postModelArrayList.clear();
                                    postNoExists.setVisibility(View.GONE);
                                    postRecycler.setVisibility(View.VISIBLE);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Post post = dataSnapshot.getValue(Post.class);
                                        String postId = dataSnapshot.getKey();
                                        String postUserId = post.getPostUserId();
                                        firebaseDatabase.getReference("Users/" + postUserId)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            User user = snapshot.getValue(User.class);
                                                            String postUserName = user.getUsername();
                                                            String postUserIcon = user.getProfileImg();

                                                            postModelArrayList.add(new HomePostModel(postId, postUserId, postUserName, postUserIcon, post.getPostTitle(), post.getPostImageUrl(), "2342", "983"));

                                                            postAdapter.notifyDataSetChanged();
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                    }
                                    Collections.reverse(postModelArrayList);
                                } else {
                                    postNoExists.setVisibility(View.VISIBLE);
                                    postRecycler.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                break;

            case POST_TODAY:
                firebaseDatabase.getReference("Posts")
                        .orderByChild("postDate").startAt(System.currentTimeMillis())
                        .limitToLast(loadLimit)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    postModelArrayList.clear();
                                    postRecycler.setVisibility(View.VISIBLE);
                                    postNoExists.setVisibility(View.GONE);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Post post = dataSnapshot.getValue(Post.class);
                                        String postId = dataSnapshot.getKey();
                                        String postUserId = post.getPostUserId();
                                        firebaseDatabase.getReference("Users/" + postUserId)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            User user = snapshot.getValue(User.class);
                                                            String postUserName = user.getUsername();
                                                            String postUserIcon = user.getProfileImg();

                                                            postModelArrayList.add(new HomePostModel(postId, postUserId, postUserName, postUserIcon, post.getPostTitle(), post.getPostImageUrl(), "2342", "983"));

                                                            postAdapter.notifyDataSetChanged();
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                    }
                                } else {
                                    postNoExists.setVisibility(View.VISIBLE);
                                    postRecycler.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                break;
            case POST_MEMBERS:

                firebaseDatabase.getReference("Users")
                        .orderByChild("members").equalTo(privateStorage.userDetail().put(USER_ID, null))
                        .limitToLast(loadLimit)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    postModelArrayList.clear();
                                    postRecycler.setVisibility(View.VISIBLE);
                                    postNoExists.setVisibility(View.GONE);
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        User user = dataSnapshot.getValue(User.class);
                                        String userId = dataSnapshot.getKey();
                                        String postUserName = user.getUsername();
                                        String postUserIcon = user.getProfileImg();


                                        firebaseDatabase.getReference("Posts")
                                                .orderByChild("postUserId").equalTo(userId)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            Post post = snapshot.getValue(Post.class);
                                                            String postId = snapshot.getKey();
                                                            String postTitle = post.getPostTitle();
                                                            String postImageUrl = post.getPostImageUrl();

                                                            postModelArrayList.add(new HomePostModel(postId, userId, postUserName, postUserIcon, postTitle, postImageUrl, "00", "00"));

                                                            postAdapter.notifyDataSetChanged();

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                    }
                                } else {
                                    postNoExists.setVisibility(View.VISIBLE);
                                    postRecycler.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                break;

            case POST_TOP_LIKE:

                break;

            case POST_YESTERDAY:

                break;

            default:
                break;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}