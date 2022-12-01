package com.jadhav.aakash.ui.friends;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.FragmentFriendsBinding;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Toasty;
import com.jadhav.aakash.supports.User;

import java.util.ArrayList;
import java.util.Collections;


public class FriendsFragment extends Fragment {


    FirebaseDatabase firebaseDatabase;
    private FragmentFriendsBinding binding;
    private FriendsAdapter friendsAdapter;
    private ArrayList<FriendsModel> friendsModelArrayList = new ArrayList<>();
    private PrivateStorage privateStorage;

    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    private LinearLayoutManager manager;
    private int loadLimit = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        privateStorage = new PrivateStorage(getContext());

        firebaseDatabase = FirebaseDatabase.getInstance();

        // NAVBAR CODE //
        binding.navbar.title.setText(R.string.title_friends);
        if (privateStorage.isUserLogin()) {
            try {
                byte[] imageBytes = Base64.decode(privateStorage.userDetail().put(PROFILE_BITMAP_IMAGE, null), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.navbar.personIcon.setImageBitmap(decodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        friendsAdapter = new FriendsAdapter(friendsModelArrayList, getContext());
        manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.friendsRecycler.setLayoutManager(manager);
        binding.friendsRecycler.setAdapter(friendsAdapter);
        friendsAdapter.notifyDataSetChanged();

        binding.friendsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    loadFriends(loadLimit);
                }
            }
        });


        loadFriends(loadLimit);

        return binding.getRoot();
    }

    private void loadFriends(int limit) {

        firebaseDatabase.getReference("Users")
                .limitToLast(limit)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        friendsModelArrayList.clear();
                        if (snapshot.exists()) {
                            binding.friendNotFound.setVisibility(View.GONE);
                            binding.friendsRecycler.setVisibility(View.VISIBLE);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);
                                String friendId = dataSnapshot.getKey();
                                String username = user.getUsername();
                                String profileImg = user.getProfileImg();
                                int memberCount = user.getMemberCount();

                                if (!privateStorage.userDetail().put(USER_ID, null).equals(friendId)) {
                                    friendsModelArrayList.add(new FriendsModel(friendId, profileImg, username, memberCount));
                                }
                            }
                        } else {
                            binding.friendsRecycler.setVisibility(View.GONE);
                            binding.friendNotFound.setVisibility(View.VISIBLE);
                        }
                        loadLimit += 5;
                        isScrolling = false;
                        Collections.reverse(friendsModelArrayList);
                        friendsAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toasty.Message(getContext(), "Friends Show Failed.");
                    }
                });
    }
}