package com.example.aakash.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aakash.R;
import com.example.aakash.databinding.FragmentFriendsBinding;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {


    private FragmentFriendsBinding binding;

    private FriendsAdapter friendsAdapter;
    private ArrayList<FriendsModel> friendsModelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        binding.navbar.title.setText(R.string.title_friends);

        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "Ajay Gimbhal", "2323"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Devgan", "3234"));
        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "Ajay Bendaga", "2323"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Bendaga", "23234"));

        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "vijay Gimbhal", "2323"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Devgan", "yes"));
        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "Asdjay Bendaga", "32342"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Bendaga last per", "2342"));

        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "vijay Gimbhal", "2"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Devgan", "239238"));
        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "Asdjay Bendaga", "23"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Bendaga last per", "2234"));

        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "vijay Gimbhal", "24"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Devgan", "239238"));
        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "Asdjay Bendaga", "42"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Bendaga last per", "239238"));

        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "vijay Gimbhal", "32"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Devgan", "239238"));
        friendsModelArrayList.add(new FriendsModel("1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "Asdjay Bendaga", "2"));
        friendsModelArrayList.add(new FriendsModel("1", "https://cdn.pixabay.com/photo/2017/05/31/08/37/single-2359491_960_720.jpg", "Ajay Bendaga last per", "239238"));


        friendsAdapter = new FriendsAdapter(friendsModelArrayList, getContext());
        binding.friendsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.friendsRecycler.setAdapter(friendsAdapter);
        friendsAdapter.notifyDataSetChanged();


        return binding.getRoot();
    }
}