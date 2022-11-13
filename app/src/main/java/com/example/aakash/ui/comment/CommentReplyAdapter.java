package com.example.aakash.ui.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aakash.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.CommentReplyHolder> {

    ArrayList<CommentReplyModel> commentReplyModelList;
    LinearLayout replyInputBox;
    TextView replyOldUsername;
    EditText commentInputReply;
    Context context;
    CommentHelpModel commentHelpModel = new CommentHelpModel();

    public CommentReplyAdapter(ArrayList<CommentReplyModel> commentReplyModelList, LinearLayout replyInputBox, TextView replyOldUsername, EditText commentInputReply, Context context) {
        this.commentReplyModelList = commentReplyModelList;
        this.replyInputBox = replyInputBox;
        this.replyOldUsername = replyOldUsername;
        this.commentInputReply = commentInputReply;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentReplyHolder(LayoutInflater.from(context).inflate(R.layout.comment_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentReplyHolder holder, int position) {
        holder.setCommentReplyData(commentReplyModelList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return commentReplyModelList.size();
    }

    public class CommentReplyHolder extends RecyclerView.ViewHolder {

        CircleImageView cUserIcon;
        TextView cUsername, cCommentShow, cCommentDate, cReplyShow;

        public CommentReplyHolder(@NonNull View itemView) {
            super(itemView);
            cUserIcon = itemView.findViewById(R.id.cUserIcon);
            cUsername = itemView.findViewById(R.id.cUsername);
            cCommentShow = itemView.findViewById(R.id.cCommentShow);
            cCommentDate = itemView.findViewById(R.id.cCommentDate);
            cReplyShow = itemView.findViewById(R.id.cReplyShow);
        }

        public void setCommentReplyData(CommentReplyModel commentReplyModel, Context context) {
            Picasso.get().load(commentReplyModel.getcRUserImg()).into(cUserIcon);
            cUsername.setText(commentReplyModel.getcRUsername());
            cCommentShow.setText(HtmlCompat.fromHtml(commentReplyModel.getcRComment(), HtmlCompat.FROM_HTML_MODE_LEGACY));

            //cCommentDate.setText(convertTimeToAgo(commentReplyModel.getcRCDate()));

            cReplyShow.setOnClickListener(view -> {

                commentHelpModel.setOldUsersId(commentReplyModel.getcRUser_id());
                commentHelpModel.setOldUsername(commentReplyModel.getcRUsername());
                commentHelpModel.setOldCommentId(commentReplyModel.getcRComment_id());
                replyInputBox.setVisibility(View.VISIBLE);
                replyOldUsername.setText(commentHelpModel.getOldUsername());

                commentInputReply.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.getApplicationContext().INPUT_METHOD_SERVICE);
                imm.showSoftInput(commentInputReply, InputMethodManager.SHOW_FORCED);
            });

        }
    }
}
