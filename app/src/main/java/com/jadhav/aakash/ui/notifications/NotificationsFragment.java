package com.jadhav.aakash.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private ArrayList<NotificationsModel> notificationsModels = new ArrayList<>();
    private NotificationsAdapter notificationsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        binding.navbar.title.setText(R.string.title_notifications);

        notificationsModels.add(new NotificationsModel("1", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("2", "New Post", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("3", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("4", "Like", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("5", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("6", "Like", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", false));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice Choice Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", true));
        notificationsModels.add(new NotificationsModel("7", "Comment", "https://i.stack.imgur.com/pXpMt.jpg?s=64&g=1", "Ajay g", "Best For Choice Choice Choice", "2323", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg", "98", false));

        notificationsAdapter = new NotificationsAdapter(notificationsModels, getContext());
        binding.notifyRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.notifyRecycler.setAdapter(notificationsAdapter);
        notificationsAdapter.notifyDataSetChanged();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}