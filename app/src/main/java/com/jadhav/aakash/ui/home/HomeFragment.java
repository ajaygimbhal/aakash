package com.jadhav.aakash.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.FragmentHomeBinding;

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
        postModelArrayList.add(new HomePostModel("1", "23", "Ajay g", "https://st2.depositphotos.com/1017228/6910/i/600/depositphotos_69109583-stock-photo-laughing-businesswoman-making-selfie-photo.jpg", "this is first post title", "https://images.pexels.com/photos/2662116/pexels-photo-2662116.jpeg?auto=compress&cs=tinysrgb&w=600", "2342", "983"));
        postModelArrayList.add(new HomePostModel("2", "23", "mangesh van", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcToivD_pxwcQ5I344L6uEkmg9SXhMIglYso8g&usqp=CAU", "this is second post title", "https://images.pexels.com/photos/573302/pexels-photo-573302.jpeg?auto=compress&cs=tinysrgb&w=600", "2sd342", "983"));
        postModelArrayList.add(new HomePostModel("3", "23", "mangesh komb", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcToivD_pxwcQ5I344L6uEkmg9SXhMIglYso8g&usqp=CAU", "this is second post title", "https://images.pexels.com/photos/2387418/pexels-photo-2387418.jpeg?auto=compress&cs=tinysrgb&w=600", "2sd342", "983"));
        postModelArrayList.add(new HomePostModel("3", "23", "sandip vam", "https://images.pexels.com/photos/2923595/pexels-photo-2923595.jpeg?auto=compress&cs=tinysrgb&w=600", "this is second post title", "https://images.pexels.com/photos/321552/pexels-photo-321552.jpeg?auto=compress&cs=tinysrgb&w=600", "2sd342", "983"));
        postModelArrayList.add(new HomePostModel("3", "23", "sandip vam", "https://images.pexels.com/photos/2923595/pexels-photo-2923595.jpeg?auto=compress&cs=tinysrgb&w=600", "this is second post title", "https://images.pexels.com/photos/1770809/pexels-photo-1770809.jpeg?auto=compress&cs=tinysrgb&w=600", "2sd342", "983"));
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