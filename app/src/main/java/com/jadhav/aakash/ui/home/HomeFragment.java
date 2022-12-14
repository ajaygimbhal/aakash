package com.jadhav.aakash.ui.home;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;

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
import androidx.core.widget.NestedScrollView;
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
import com.jadhav.aakash.supports.Toasty;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static String postType;
    private final String POST_ALL = "All";
    private final String POST_TODAY = "Today";
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
    private int totalPostCount = 10;

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
        filterModelArrayList.add(new HomePostFilterModel("Top like", false));
        filterModelArrayList.add(new HomePostFilterModel("Yesterday", false));
        filterAdapter = new HomePostFilterAdapter(filterModelArrayList, getContext(), new HomePostFilterAdapter.OnClickListener() {
            @Override
            public void OnClick(int position, String type) {
                filterModelArrayList.clear();
                filterModelArrayList.add(new HomePostFilterModel("All", false));
                filterModelArrayList.add(new HomePostFilterModel("Today", false));
                filterModelArrayList.add(new HomePostFilterModel("Top like", false));
                filterModelArrayList.add(new HomePostFilterModel("Yesterday", false));

                filterModelArrayList.get(position).setSelected(true);
                filterAdapter.notifyDataSetChanged();

                loadLimit = 5;
                postModelArrayList.clear();
                postAdapter.notifyDataSetChanged();
                postType = type;
                postDataLoad();

            }
        });
        binding.filterRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.filterRecycler.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();


        // post recycler view //
        postType = POST_ALL;
        postCount();
        postDataLoad();

        postAdapter = new HomePostAdapter(postModelArrayList, getContext());
        binding.postRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.postRecycler.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

        binding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                    if (totalPostCount >= loadLimit) {
                        postDataLoad();
                    } else {
                        Toasty.Message(getContext(), "No More Post.");
                        Log.d(TAG, "onCreateView: " + totalPostCount + " load: " + loadLimit);
                    }
                }
            }
        });


        return binding.getRoot();
    }

    private void postCount() {

        if (postType == POST_ALL) {
            firebaseDatabase.getReference("Posts")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            totalPostCount = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } else if (postType == POST_TODAY) {
            String todayDate = privateStorage.dateTime(System.currentTimeMillis());
            firebaseDatabase.getReference("Posts")
                    .orderByChild("postDate").equalTo(todayDate.substring(0, todayDate.lastIndexOf(" ")))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            totalPostCount = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } else if (postType == POST_TOP_LIKE) {

        } else if (postType == POST_YESTERDAY) {
            String yesterdayDate = privateStorage.dateTime(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
            firebaseDatabase.getReference("Posts")
                    .orderByChild("postDate").equalTo(yesterdayDate.substring(0, yesterdayDate.lastIndexOf(" ")))
                    .limitToLast(loadLimit)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            totalPostCount = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void postDataLoad() {

        if (postType == POST_ALL) {
            firebaseDatabase.getReference("Posts")
                    .limitToLast(loadLimit)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public synchronized void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                postModelArrayList.clear();
                                postNoExists.setVisibility(View.GONE);
                                postRecycler.setVisibility(View.VISIBLE);

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Post post = dataSnapshot.getValue(Post.class);
                                    String postId = dataSnapshot.getKey();
                                    String postUserId = post.getPostUserId();

                                    postModelArrayList.add(new HomePostModel(postId, postUserId, post.getPostTitle(), post.getPostImageUrl()));
                                }
                                loadLimit += 5;
                                Collections.reverse(postModelArrayList);
                                postAdapter.notifyDataSetChanged();
                            } else {
                                postNoExists.setVisibility(View.VISIBLE);
                                postRecycler.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } else if (postType == POST_TODAY) {

            String todayDate = privateStorage.dateTime(System.currentTimeMillis());
            firebaseDatabase.getReference("Posts")
                    .orderByChild("postDate").equalTo(todayDate.substring(0, todayDate.lastIndexOf(" ")))
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


                                    postModelArrayList.add(new HomePostModel(postId, postUserId, post.getPostTitle(), post.getPostImageUrl()));


                                }
                                loadLimit += 5;
                                postAdapter.notifyDataSetChanged();
                            } else {
                                postNoExists.setVisibility(View.VISIBLE);
                                postRecycler.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } else if (postType == POST_TOP_LIKE) {

            firebaseDatabase.getReference("Posts")
                    .orderByChild("likesCount").limitToLast(loadLimit)
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
                                    String postTitle = post.getPostTitle();
                                    String postImageUrl = post.getPostImageUrl();
                                    postModelArrayList.add(new HomePostModel(postId, postUserId, postTitle, postImageUrl));
                                }
                                loadLimit += 5;
                                Collections.reverse(postModelArrayList);
                                postAdapter.notifyDataSetChanged();
                            } else {
                                postNoExists.setVisibility(View.VISIBLE);
                                postRecycler.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        } else if (postType == POST_YESTERDAY) {


            String yesterdayDate = privateStorage.dateTime(System.currentTimeMillis() - 24 * 60 * 60 * 1000);

            firebaseDatabase.getReference("Posts")
                    .orderByChild("postDate").equalTo(yesterdayDate.substring(0, yesterdayDate.lastIndexOf(" ")))
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

                                    postModelArrayList.add(new HomePostModel(postId, postUserId, post.getPostTitle(), post.getPostImageUrl()));


                                }
                                loadLimit += 5;
                                postAdapter.notifyDataSetChanged();
                            } else {
                                postNoExists.setVisibility(View.VISIBLE);
                                postRecycler.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

        if (totalPostCount < postModelArrayList.size()){
            loadLimit =- 5;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}