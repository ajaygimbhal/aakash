package com.jadhav.aakash.ui.notifications;

import static com.jadhav.aakash.supports.PrivateStorage.convertTimeToAgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.jadhav.aakash.R;
import com.jadhav.aakash.activities.CommentActivity;
import com.jadhav.aakash.databinding.NotificationCardViewBinding;
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

            binding.nUsernameMessage.setText(HtmlCompat.fromHtml("<b>" + notificationsModel.getNotifyToUsername() + "</b> " + notificationsModel.getNotifyType() + ": " + notificationsModel.getNotifyMessage(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            Picasso.get().load(notificationsModel.getNotifyToProfileImg()).into(binding.nUserIcon);

            Picasso.get().load(notificationsModel.getNotifyPostImg()).into(binding.nPostThumbnail);

            binding.nTime.setText(convertTimeToAgo(notificationsModel.getNotifyDate()));

            if (notificationsModel.isNotifyRead()) {
                binding.notificationMainItem.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            } else {
                binding.notificationMainItem.setBackgroundColor(Color.parseColor("#CCC6C4"));
            }


            binding.nPostThumbnail.setOnClickListener(view -> {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", notificationsModel.getNotifyPostId());
                context.startActivity(intent);
            });

            binding.notificationMainItem.setOnClickListener(view -> {
                Intent intent = new Intent(context, CommentActivity.class);

                switch (notificationsModel.getNotifyType()) {

                    case "Comment":
                        intent.putExtra("Comment", true);
                        intent.putExtra("postId", notificationsModel.getNotifyPostId());
                        break;
                    case "Reply":
                        intent.putExtra("Reply", true);
                        intent.putExtra("postId", notificationsModel.getNotifyPostId());
                        intent.putExtra("commentId", notificationsModel.getNotifyCommentId());
                        intent.putExtra("fromUserId", notificationsModel.getNotifyFromUserId());
                        break;
                    default:
                        intent.putExtra("Post", true);
                        intent.putExtra("postId", notificationsModel.getNotifyPostId());
                        break;
                }


                context.startActivity(intent);
            });


        }
    }

    public class SampleNotificationHolder extends RecyclerView.ViewHolder {
        public SampleNotificationHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
