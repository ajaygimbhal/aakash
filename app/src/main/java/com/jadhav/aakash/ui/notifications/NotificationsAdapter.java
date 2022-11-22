package com.jadhav.aakash.ui.notifications;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.NotificationCardViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsHolder> {

    ArrayList<NotificationsModel> notificationsModels;
    Context context;

    public NotificationsAdapter(ArrayList<NotificationsModel> notificationsModels, Context context) {
        this.notificationsModels = notificationsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationsHolder(LayoutInflater.from(context).inflate(R.layout.notification_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsHolder holder, int position) {
        holder.setNotificationData(notificationsModels.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationsModels.size();
    }

    public class NotificationsHolder extends RecyclerView.ViewHolder {

        NotificationCardViewBinding binding;

        public NotificationsHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationCardViewBinding.bind(itemView);
        }


        public void setNotificationData(NotificationsModel notificationsModel) {

            Picasso.get().load(notificationsModel.getNotifyUserIcon()).into(binding.nUserIcon);
            binding.nUsernameMessage.setText(HtmlCompat.fromHtml("<b>" + notificationsModel.getNotifyUsername() + "</b> " + notificationsModel.getNotifyType() + ": " + notificationsModel.getNotifyMessage(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            binding.nTime.setText("9 days ago");
            Picasso.get().load(notificationsModel.getNotifyPostImg()).into(binding.nPostThumbnail);

            if (notificationsModel.isNotifyRead()){
                binding.notificationMainItem.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }else{
                binding.notificationMainItem.setBackgroundColor(Color.parseColor("#CCC6C4"));
            }


        }
    }
}
