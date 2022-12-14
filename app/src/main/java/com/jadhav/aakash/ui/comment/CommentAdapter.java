package com.jadhav.aakash.ui.comment;

import static com.jadhav.aakash.supports.PrivateStorage.convertTimeToAgo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.R;
import com.jadhav.aakash.activities.CommentReplyActivity;
import com.jadhav.aakash.databinding.CommentCardViewBinding;
import com.jadhav.aakash.supports.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    ArrayList<CommentModel> commentModels;
    Context context;
    String postId;
    FirebaseDatabase firebaseDatabase;

    public CommentAdapter(ArrayList<CommentModel> commentModels, Context context, String postId) {
        this.commentModels = commentModels;
        this.context = context;
        this.postId = postId;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(context).inflate(R.layout.comment_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.setCommentData(commentModels.get(position));
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        CommentCardViewBinding binding;
        String username;
        String profileImg;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentCardViewBinding.bind(itemView);
            firebaseDatabase = FirebaseDatabase.getInstance();
        }

        public void setCommentData(CommentModel commentModel) {


            firebaseDatabase.getReference("Users/" + commentModel.getcUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                username = user.getUsername();
                                profileImg = user.getProfileImg();

                                binding.cUsername.setText(username);
                                Picasso.get().load(profileImg).into(binding.cUserIcon);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            binding.cCommentShow.setText(commentModel.getComment());
            binding.cCommentDate.setText(convertTimeToAgo(commentModel.getcDate()));

            firebaseDatabase.getReference("Posts/" + postId + "/comments/" + commentModel.getCommentId() + "/replies")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int repliesCount = (int) snapshot.getChildrenCount();
                            if (repliesCount >= 1) {
                                binding.cReplyShow.setText("Replies(" + repliesCount + ")");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            binding.cReplyShow.setOnClickListener(view -> {
                Intent intent = new Intent(context, CommentReplyActivity.class);
                intent.putExtra("postId", postId);
                intent.putExtra("commentId", commentModel.getCommentId());
                intent.putExtra("userId", commentModel.getcUserId());
                intent.putExtra("username", username);
                intent.putExtra("profileImg", profileImg);
                intent.putExtra("comment", commentModel.getComment());
                intent.putExtra("cDate", commentModel.getcDate());
                context.getApplicationContext().startActivity(intent);
            });
        }
    }
}
