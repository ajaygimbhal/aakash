package com.jadhav.aakash.ui.comment;

import static com.jadhav.aakash.supports.PrivateStorage.convertTimeToAgo;

import android.content.Context;
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
import com.jadhav.aakash.databinding.CommentCardViewBinding;
import com.jadhav.aakash.supports.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.CommentReplyHolder> {

    public OnClickListener clickListener;
    ArrayList<CommentReplyModel> commentReplyModelList;
    Context context;
    FirebaseDatabase firebaseDatabase;

    public CommentReplyAdapter(ArrayList<CommentReplyModel> commentReplyModelList, Context context, OnClickListener clickListener) {
        this.commentReplyModelList = commentReplyModelList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CommentReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentReplyHolder(LayoutInflater.from(context).inflate(R.layout.comment_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentReplyHolder holder, int position) {
        holder.setCommentReplyData(commentReplyModelList.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return commentReplyModelList.size();
    }

    public interface OnClickListener {
        void OnClick(String commentId, String userId, String username);
    }

    public class CommentReplyHolder extends RecyclerView.ViewHolder {

        CommentCardViewBinding binding;

        public CommentReplyHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentCardViewBinding.bind(itemView);
            firebaseDatabase = FirebaseDatabase.getInstance();

        }

        public void setCommentReplyData(CommentReplyModel commentReplyModel, OnClickListener clickListener) {


            firebaseDatabase.getReference("Users/" + commentReplyModel.getcRUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                String username = user.getUsername();
                                String profileImg = user.getProfileImg();

                                commentReplyModel.setcRUsername(username);
                                commentReplyModel.setcRUserImg(profileImg);

                                Picasso.get().load(commentReplyModel.getcRUserImg()).into(binding.cUserIcon);
                                binding.cUsername.setText(commentReplyModel.getcRUsername());


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            binding.cCommentShow.setText(HtmlCompat.fromHtml(commentReplyModel.getcRComment(), HtmlCompat.FROM_HTML_MODE_LEGACY));

            binding.cCommentDate.setText(convertTimeToAgo(commentReplyModel.getcRCDate()));

            binding.cReplyShow.setOnClickListener(view -> {

                String commentId = commentReplyModel.getcRCommentId();
                String cUserId = commentReplyModel.getcRUserId();
                String cUsername = commentReplyModel.getcRUsername();
                clickListener.OnClick(commentId, cUserId, cUsername);

            });

        }
    }
}
