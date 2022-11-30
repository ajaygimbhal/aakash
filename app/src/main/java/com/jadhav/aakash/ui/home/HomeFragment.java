package com.jadhav.aakash.ui.home;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        filterModelArrayList.add(new HomePostFilterModel(1, "All"));
        filterModelArrayList.add(new HomePostFilterModel(2, "Today"));
        filterModelArrayList.add(new HomePostFilterModel(3, "Member Posts"));
        filterModelArrayList.add(new HomePostFilterModel(4, "Top like"));
        filterModelArrayList.add(new HomePostFilterModel(5, "Yesterday"));
        filterAdapter = new HomePostFilterAdapter(filterModelArrayList, getContext());
        binding.filterRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.filterRecycler.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();


        // post recycler view //
        postDataLoad("All");

        postAdapter = new HomePostAdapter(postModelArrayList, getContext());
        binding.postRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.postRecycler.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();


        return binding.getRoot();
    }

    private void postDataLoad(String type) {
        if (type == "All") {
            firebaseDatabase.getReference("Posts")
                    .limitToLast(loadLimit)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                postModelArrayList.clear();
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
                                binding.postNoExists.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}