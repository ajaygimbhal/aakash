package com.jadhav.aakash.ui.comment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.activities.CommentReplyActivity;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.CommentCardViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder>{

    ArrayList<CommentModel> commentModels;
    Context context;
    String videoId;

    public CommentAdapter(ArrayList<CommentModel> commentModels, Context context, String videoId) {
        this.commentModels = commentModels;
        this.context = context;
        this.videoId = videoId;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(context).inflate(R.layout.comment_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.setCommentData(commentModels.get(position), context);
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        CommentCardViewBinding binding;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentCardViewBinding.bind(itemView);
        }

        public void setCommentData(CommentModel commentModel, Context context) {

            Picasso.get().load(commentModel.getImageUrl()).into(binding.cUserIcon);
            binding.cUsername.setText(commentModel.getUsername());
            binding.cCommentShow.setText(commentModel.getComment());
            //binding.cCommentDate.setText(convertTimeToAgo(commentModel.getcDate()));

            if (!commentModel.getCountComment().equals("0")){
                binding.cReplyShow.setText("Replies("+ commentModel.getCountComment()+ ")");
            }

            binding.cReplyShow.setOnClickListener(view -> {
                Intent intent = new Intent(context.getApplicationContext(), CommentReplyActivity.class);
                intent.putExtra("videoId", videoId);
                intent.putExtra("commentId", commentModel.getCommentId());
                intent.putExtra("userId", commentModel.getcUserId());
                intent.putExtra("username", commentModel.getUsername());
                intent.putExtra("image", commentModel.getImageUrl());
                intent.putExtra("comment", commentModel.getComment());
                intent.putExtra("cDate", commentModel.getcDate());
                context.getApplicationContext().startActivity(intent);
            });
        }
    }
}
