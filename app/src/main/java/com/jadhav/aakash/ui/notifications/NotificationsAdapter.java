package com.jadhav.aakash.ui.notifications;

import static com.jadhav.aakash.supports.PrivateStorage.convertTimeToAgo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.NotificationCardViewBinding;
import com.jadhav.aakash.supports.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SAMPLE_VIEW = 1;
    private static final int REAL_VIEW = 2;
    ArrayList<NotificationsModel> notificationsModels;
    Context context;
    FirebaseDatabase firebaseDatabase;

    public NotificationsAdapter(ArrayList<NotificationsModel> notificationsModels, Context context) {
        this.notificationsModels = notificationsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SAMPLE_VIEW:
                return new SampleNotificationHolder(LayoutInflater.from(context).inflate(R.layout.sample_notification_card_view, parent, false));
            case REAL_VIEW:
                return new NotificationsHolder(LayoutInflater.from(context).inflate(R.layout.notification_card_view, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SAMPLE_VIEW:
                break;
            case REAL_VIEW:
                ((NotificationsHolder) holder).setNotificationData(notificationsModels.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (notificationsModels.isEmpty()) {
            return 10;
        } else {
            return notificationsModels.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (notificationsModels.isEmpty()) {
            return SAMPLE_VIEW;
        } else {
            return REAL_VIEW;
        }
    }

    public class NotificationsHolder extends RecyclerView.ViewHolder {

        NotificationCardViewBinding binding;

        public NotificationsHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationCardViewBinding.bind(itemView);
            firebaseDatabase = FirebaseDatabase.getInstance();
        }


        public void setNotificationData(NotificationsModel notificationsModel) {

            firebaseDatabase.getReference("Users/" + notificationsModel.getNotifyToUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                binding.nUsernameMessage.setText(HtmlCompat.fromHtml("<b>" + user.getUsername() + "</b> " + notificationsModel.getNotifyType() + ": " + notificationsModel.getNotifyMessage(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                                Picasso.get().load(user.getProfileImg()).into(binding.nUserIcon);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            firebaseDatabase.getReference("Posts/" + notificationsModel.getNotifyPostId() + "/postImageUrl/")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String postImageUrl = snapshot.getValue().toString();
                                Picasso.get().load(postImageUrl).into(binding.nPostThumbnail);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            binding.nTime.setText(convertTimeToAgo(notificationsModel.getNotifyDate()));

            if (notificationsModel.isNotifyRead()) {
                binding.notificationMainItem.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            } else {
                binding.notificationMainItem.setBackgroundColor(Color.parseColor("#CCC6C4"));
            }


        }
    }

    public class SampleNotificationHolder extends RecyclerView.ViewHolder {
        public SampleNotificationHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
