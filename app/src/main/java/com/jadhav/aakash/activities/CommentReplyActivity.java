package com.jadhav.aakash.activities;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;
import static com.jadhav.aakash.supports.PrivateStorage.convertTimeToAgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.databinding.ActivityCommentReplyBinding;
import com.jadhav.aakash.supports.Notification;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Replies;
import com.jadhav.aakash.supports.Toasty;
import com.jadhav.aakash.ui.comment.CommentReplyAdapter;
import com.jadhav.aakash.ui.comment.CommentReplyModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentReplyActivity extends AppCompatActivity {

    private final static String TAG = "CommentReplyActivity";
    private static String oldCommentId, oldUserId, oldUsername;
    ActivityCommentReplyBinding binding;
    PrivateStorage privateStorage;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    ArrayList<CommentReplyModel> replyModelArrayList = new ArrayList<>();
    CommentReplyAdapter replyAdapter;
    private String postId, commentId, userId, username, profileImg, comment, cDate;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Reply Adding...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        privateStorage = new PrivateStorage(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (privateStorage.isUserLogin()) {
            try {
                byte[] imageBytes = Base64.decode(privateStorage.userDetail().put(PROFILE_BITMAP_IMAGE, null), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.cReplyUserImg.setImageBitmap(decodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            postId = getIntent().getStringExtra("postId");
            commentId = getIntent().getStringExtra("commentId");
            userId = getIntent().getStringExtra("userId");
            username = getIntent().getStringExtra("username");
            profileImg = getIntent().getStringExtra("profileImg");
            comment = getIntent().getStringExtra("comment");
            cDate = getIntent().getStringExtra("cDate");

        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.cUserComment.setText(username + " \"" + comment + '"');
        Picasso.get().load(profileImg).into(binding.cReplyUserIcon);
        binding.cUserCommentDate.setText(convertTimeToAgo(cDate));


        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        replyAdapter = new CommentReplyAdapter(replyModelArrayList, this, new CommentReplyAdapter.OnClickListener() {
            @Override
            public void OnClick(String aCommentId, String aUserId, String aUsername) {
                oldCommentId = aCommentId;
                oldUserId = aUserId;
                oldUsername = aUsername;

                binding.cReplyOldUsername.setText(oldUsername);
                binding.boxCloseView.setVisibility(View.VISIBLE);
                binding.cReplyCommentInputBox.setVisibility(View.VISIBLE);
                binding.cReplyComment.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.cReplyComment, InputMethodManager.SHOW_FORCED);

            }
        });
        binding.cReplyCommentRecyclerView.setLayoutManager(manager);
        binding.cReplyCommentRecyclerView.setAdapter(replyAdapter);
        replyAdapter.notifyDataSetChanged();
        loadReplies();


        binding.cReplyCommentBoxOpen.setOnClickListener(view -> {

            oldCommentId = commentId;
            oldUserId = userId;
            oldUsername = username;

            binding.cReplyOldUsername.setText(oldUsername);
            binding.boxCloseView.setVisibility(View.VISIBLE);
            binding.cReplyCommentInputBox.setVisibility(View.VISIBLE);
            binding.cReplyComment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.cReplyComment, InputMethodManager.SHOW_FORCED);

        });

        binding.boxCloseView.setOnClickListener(view -> {
            binding.cReplyComment.setText("");
            binding.cReplyCommentInputBox.setVisibility(View.GONE);
            binding.boxCloseView.setVisibility(View.GONE);
            if ((View) this.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        binding.cReplyBoxClose.setOnClickListener(view -> {
            binding.cReplyCommentInputBox.setVisibility(View.INVISIBLE);
            if ((View) this.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            super.onBackPressed();
            finish();
        });


        binding.cReplyCommentBtn.setOnClickListener(view -> {
            replyStore();
        });

    }

    private void loadReplies() {

        firebaseDatabase.getReference("Posts/" + postId + "/comments/" + commentId + "/replies/")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            replyModelArrayList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Replies replies = dataSnapshot.getValue(Replies.class);
                                String rReplyId = dataSnapshot.getKey();
                                String rParentId = replies.getParentId();
                                String rToUserId = replies.getToUserId();
                                String rMessage = replies.getMessage();
                                String rDate = replies.getDate();
                                if (commentId.equals(rParentId)) {
                                    replyModelArrayList.add(new CommentReplyModel(rReplyId, rToUserId, rMessage, rDate));
                                    loadMoreReplies(snapshot, rReplyId);
                                }
                            }

                            replyAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadMoreReplies(DataSnapshot snapshot, String rReplyId) {
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Replies replies = dataSnapshot.getValue(Replies.class);
            String reReplyId = dataSnapshot.getKey();
            String reParentId = replies.getParentId();
            String reToUserId = replies.getToUserId();
            String reFromUserId = replies.getFromUserId();
            String reMessage = replies.getMessage();
            String reDate = replies.getDate();

            if (rReplyId.equals(reParentId)) {

                firebaseDatabase.getReference("Users/" + reFromUserId + "/username/")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String username = snapshot.getValue().toString();

                                replyModelArrayList.add(new CommentReplyModel(
                                        reReplyId,
                                        reToUserId,
                                        "<span style=\"color:#125FBC;\">"+username+"</span> " + reMessage,
                                        reDate
                                ));
                                loadMoreReplies(snapshot, reReplyId);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        }
        replyAdapter.notifyDataSetChanged();
    }


    private void replyStore() {
        if (privateStorage.isUserLogin()) {
            if (privateStorage.isConnectedToInternet()) {
                if (binding.cReplyComment.getText().toString().length() > 0) {
                    progressDialog.show();
                    String replyId = firebaseDatabase.getReference("Posts/" + postId + "/comments/" + commentId + "/replies/").push().getKey();
                    String toUserId = privateStorage.userDetail().put(USER_ID, null);
                    String fromUserId = oldUserId;
                    String message = binding.cReplyComment.getText().toString();
                    String rDate = privateStorage.dateTime(System.currentTimeMillis());

                    Replies replies = new Replies(oldCommentId, toUserId, fromUserId, message, rDate);
                    firebaseDatabase.getReference("Posts/" + postId + "/comments/" + commentId + "/replies/")
                            .child(replyId)
                            .setValue(replies)
                            .addOnSuccessListener(unused -> {

                                binding.cReplyComment.setText("");
                                binding.cReplyCommentInputBox.setVisibility(View.GONE);
                                binding.boxCloseView.setVisibility(View.GONE);

                                InputMethodManager imm = (InputMethodManager) getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.cReplyComment.getWindowToken(), 0);

                                String nId = firebaseDatabase.getReference("Notifications").push().getKey();
                                String nType = "Reply";
                                String nToUserId = privateStorage.userDetail().put(USER_ID, null);
                                String nFromUserId = fromUserId;
                                String nPostId = postId;
                                String nMessage = message;
                                String nCommentId = commentId;
                                String nDate = privateStorage.dateTime(System.currentTimeMillis());

                                if (!nFromUserId.equals(nToUserId)){

                                    Notification notification = new Notification(nType, nToUserId, nFromUserId, nPostId, nMessage, nCommentId, nDate);
                                    firebaseDatabase.getReference("Notifications")
                                            .child(nId)
                                            .setValue(notification);
                                    Log.d(TAG, "replyStore: notification");
                                }else{
                                    Log.d(TAG, "replyStore: " + nFromUserId + " toUserId "+ nToUserId);
                                }

                                progressDialog.dismiss();
                                Toasty.Message(this, oldUsername + " Reply Successfully.");

                            }).addOnFailureListener(e -> {
                                Toasty.Message(this, "Reply Adding Failed.");
                                progressDialog.dismiss();
                            });

                } else {
                    Toasty.Message(this, "Reply Can't Be Empty.");
                }
            } else {
                Toasty.Message(this, "Internet Connection Enable.");
            }
        } else {
            Toasty.Message(this, "Please Login...");
        }
    }
}