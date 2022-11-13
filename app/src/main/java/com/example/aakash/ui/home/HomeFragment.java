package com.example.aakash.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aakash.R;
import com.example.aakash.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private HomePostFilterAdapter filterAdapter;
    private ArrayList<HomePostFilterModel> filterModelArrayList = new ArrayList<>();

    private HomePostAdapter postAdapter;
    private ArrayList<HomePostModel> postModelArrayList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.navbar.title.setText(R.string.app_name);

        // filter post recycler view //
        filterModelArrayList.clear();
        filterModelArrayList.add(new HomePostFilterModel(1, "All"));
        filterModelArrayList.add(new HomePostFilterModel(2, "Today"));
        filterModelArrayList.add(new HomePostFilterModel(3, "Top Like"));
        filterModelArrayList.add(new HomePostFilterModel(4, "Yesterday"));
        filterAdapter = new HomePostFilterAdapter(filterModelArrayList, getContext());
        binding.filterRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.filterRecycler.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();


        // post show recycler view //
        postModelArrayList.clear();
        postModelArrayList.add(new HomePostModel("1", "23", "Ajay g", "https://st2.depositphotos.com/1017228/6910/i/600/depositphotos_69109583-stock-photo-laughing-businesswoman-making-selfie-photo.jpg", "this is first post title", "https://www.stockvault.net/data/2011/05/31/124348/thumb16.jpg", "2342", "983"));
        postModelArrayList.add(new HomePostModel("2", "23", "mangesh komb", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcToivD_pxwcQ5I344L6uEkmg9SXhMIglYso8g&usqp=CAU", "this is second post title", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9EAeCm3s6J-Lm20Y-20NgIkfeZ0-9pd1moQ&usqp=CAU", "2sd342", "983"));
        postModelArrayList.add(new HomePostModel("3", "23", "mangesh komb", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcToivD_pxwcQ5I344L6uEkmg9SXhMIglYso8g&usqp=CAU", "this is second post title", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4qTg6fFDyOZqICjlmXpRpHZvBH-ugax9FwjYYfWuJ&s", "2sd342", "983"));
        postAdapter = new HomePostAdapter(postModelArrayList, getContext());
        binding.postRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.postRecycler.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}