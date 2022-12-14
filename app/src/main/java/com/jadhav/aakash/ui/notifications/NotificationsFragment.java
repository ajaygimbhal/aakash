package com.jadhav.aakash.ui.notifications;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

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
import com.jadhav.aakash.databinding.FragmentNotificationsBinding;
import com.jadhav.aakash.supports.Notification;
import com.jadhav.aakash.supports.PrivateStorage;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    private FragmentNotificationsBinding binding;
    private ArrayList<NotificationsModel> notificationsModels = new ArrayList<>();
    private NotificationsAdapter notificationsAdapter;
    private PrivateStorage privateStorage;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        privateStorage = new PrivateStorage(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();

        // NAVBAR CODE //
        binding.navbar.title.setText(R.string.title_notifications);
        if (privateStorage.isUserLogin()) {
            try {
                byte[] imageBytes = Base64.decode(privateStorage.userDetail().put(PROFILE_BITMAP_IMAGE, null), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.navbar.personIcon.setImageBitmap(decodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        loadNotificationData();
        notificationsAdapter = new NotificationsAdapter(notificationsModels, getContext());
        binding.notifyRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.notifyRecycler.setAdapter(notificationsAdapter);
        notificationsAdapter.notifyDataSetChanged();

        return binding.getRoot();
    }

    private void loadNotificationData() {
        firebaseDatabase.getReference("Notifications")
                .orderByChild("nFromUserId").equalTo(privateStorage.userDetail().put(USER_ID, null))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            notificationsModels.clear();
                            binding.notifyRecycler.setVisibility(View.VISIBLE);
                            binding.notifyNotFound.setVisibility(View.GONE);

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Notification notification = dataSnapshot.getValue(Notification.class);

                                String nId = dataSnapshot.getKey();
                                String nType = notification.getnType();
                                String nPostId = notification.getnPostId();
                                String nMessage = notification.getnMessage();
                                String nToUserId = notification.getnToUserId();
                                String nFromUserId = notification.getnFromUserId();
                                String nDate = notification.getnDate();
                                boolean nRead = notification.isnRead();
                                notificationsModels.add(new NotificationsModel(nId, nType, nMessage, nToUserId, nFromUserId, nPostId, nDate, nRead));

                            }

                            notificationsAdapter.notifyDataSetChanged();

                        } else {
                            binding.notifyNotFound.setVisibility(View.VISIBLE);
                            binding.notifyRecycler.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}