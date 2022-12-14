package com.jadhav.aakash.activities;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USERNAME;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.ActivityCommentBinding;
import com.jadhav.aakash.supports.Comment;
import com.jadhav.aakash.supports.Notification;
import com.jadhav.aakash.supports.Post;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Toasty;
import com.jadhav.aakash.supports.User;
import com.jadhav.aakash.ui.comment.CommentAdapter;
import com.jadhav.aakash.ui.comment.CommentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class CommentActivity extends AppCompatActivity {

    private static String postId;
    private final String TAG = "CommentActivity";
    ActivityCommentBinding binding;
    PrivateStorage privateStorage;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    ArrayList<CommentModel> commentModelList = new ArrayList<>();
    CommentAdapter commentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Comment Adding...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);


        privateStorage = new PrivateStorage(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        try {
            postId = getIntent().getStringExtra("postId");

            if (getIntent().getAction().equals(Intent.ACTION_VIEW) && getIntent().getData() != null) {
                String postUrl = String.valueOf(getIntent().getData());

                postId = postUrl.substring(postUrl.lastIndexOf("/") + 1);
                Log.d(TAG, "onCreate: " + postId + " "+ postUrl);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (privateStorage.isUserLogin()) {
            try {
                byte[] imageBytes = Base64.decode(privateStorage.userDetail().put(PROFILE_BITMAP_IMAGE, null), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.cUserIcon.setImageBitmap(decodedImage);
                binding.cUsername.setText(privateStorage.userDetail().put(USERNAME, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        postLoad();
        binding.cCommentBoxOpen.setOnClickListener(view -> {
            binding.cCommentBoxOpenView.setVisibility(View.VISIBLE);
            binding.cCommentInputBox.setVisibility(View.VISIBLE);
            binding.cComment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.cComment, InputMethodManager.SHOW_FORCED);
        });

        binding.cCommentBoxOpenView.setOnClickListener(view -> {
            binding.cCommentInputBox.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.cComment.getWindowToken(), 0);
            binding.cCommentBoxOpenView.setVisibility(View.GONE);
        });

        binding.cBoxClose.setOnClickListener(view -> {
            binding.cCommentInputBox.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.cComment.getWindowToken(), 0);
            super.onBackPressed();
            finish();

        });

        commentAdapter = new CommentAdapter(commentModelList, this, postId);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        binding.commentRecyclerView.setLayoutManager(manager);
        binding.commentRecyclerView.setAdapter(commentAdapter);
        binding.commentRecyclerView.setNestedScrollingEnabled(false);

        loadComments();


        binding.cCommentBtn.setOnClickListener(view -> {
            commentStore();
        });


        replyActivityRedirect();

    }

    private void replyActivityRedirect() {

        if (getIntent().getBooleanExtra("Reply", false)) {

            progressDialog.setMessage("Redirecting...");
            progressDialog.show();
            firebaseDatabase.getReference("Users").child(getIntent().getStringExtra("fromUserId"))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);

                                firebaseDatabase.getReference("Posts/" + postId + "/comments/" + getIntent().getStringExtra("commentId"))
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Comment comment = snapshot.getValue(Comment.class);

                                                    Intent intent = new Intent(getApplicationContext(), CommentReplyActivity.class);
                                                    intent.putExtra("postId", postId);
                                                    intent.putExtra("commentId", getIntent().getStringExtra("commentId"));
                                                    intent.putExtra("userId", getIntent().getStringExtra("fromUserId"));
                                                    intent.putExtra("username", user.getUsername());
                                                    intent.putExtra("profileImg", user.getProfileImg());
                                                    intent.putExtra("comment", comment.getComment());
                                                    intent.putExtra("cDate", comment.getCommentAt());
                                                    startActivity(intent);
                                                    progressDialog.dismiss();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        }


    }

    private void postLoad() {

        firebaseDatabase.getReference("Posts/" + postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Post post = snapshot.getValue(Post.class);
                            String postTitle = post.getPostTitle();
                            String postImage = post.getPostImageUrl();
                            if (postTitle != "") {
                                binding.cPostTitle.setVisibility(View.VISIBLE);
                                binding.cPostTitle.setText(postTitle);
                            }
                            Picasso.get().load(postImage).into(binding.cPostThumbnail);


                            // post like code below //
                            firebaseDatabase.getReference("Posts/" + postId + "/likes")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int likeCount = (int) snapshot.getChildrenCount();

                                            String likeString;
                                            if (likeCount > 1) {
                                                if (likeCount > 9) {
                                                    likeString = "Likes(" + likeCount + ")";
                                                } else {
                                                    likeString = "Likes(0" + likeCount + ")";
                                                }

                                            } else {
                                                likeString = "Like(0" + likeCount + ")";
                                            }
                                            binding.likeBtn.setText(likeString);

                                            firebaseDatabase.getReference("Posts/" + postId + "/likes/" + privateStorage.userDetail().put(USER_ID, null))
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                binding.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_fill, 0, 0, 0);

                                                                binding.likeBtn.setOnClickListener(view -> {
                                                                    if (privateStorage.isConnectedToInternet()) {

                                                                        firebaseDatabase.getReference("Posts/" + postId + "/likes")
                                                                                .child(privateStorage.userDetail().put(USER_ID, null))
                                                                                .removeValue()
                                                                                .addOnSuccessListener(unused -> {
                                                                                    firebaseDatabase.getReference("Posts/" + postId)
                                                                                            .child("likesCount")
                                                                                            .setValue(likeCount - 1);
                                                                                });
                                                                        postLoad();
                                                                    } else {
                                                                        Toasty.Message(getApplicationContext(), "Internet Connection Enable.");
                                                                    }
                                                                });

                                                            } else {
                                                                binding.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up, 0, 0, 0);

                                                                binding.likeBtn.setOnClickListener(view -> {

                                                                    if (privateStorage.isConnectedToInternet()) {
                                                                        firebaseDatabase.getReference("Posts/" + postId + "/likes")
                                                                                .child(privateStorage.userDetail().put(USER_ID, null))
                                                                                .child("likeAt")
                                                                                .setValue(System.currentTimeMillis())
                                                                                .addOnSuccessListener(unused -> {
                                                                                    firebaseDatabase.getReference("Posts/" + postId)
                                                                                            .child("likesCount")
                                                                                            .setValue(likeCount + 1);
                                                                                });
                                                                        postLoad();
                                                                    } else {
                                                                        Toasty.Message(getApplicationContext(), "Internet Connection Enable.");
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                            // post comment Count //
                            firebaseDatabase.getReference("Posts/" + postId + "/comments")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int commentCount = (int) snapshot.getChildrenCount();

                                            String commentString;
                                            if (commentCount > 1) {
                                                if (commentCount > 9) {
                                                    commentString = "Comments(" + commentCount + ")";
                                                } else {
                                                    commentString = "Comments(0" + commentCount + ")";
                                                }

                                            } else {
                                                commentString = "Comment(0" + commentCount + ")";
                                            }
                                            binding.commentBtn.setText(commentString);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                            // post share action //
                            binding.shareBtn.setOnClickListener(view -> {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.web_root_url) + "/post/" + postId);
                                    startActivity(Intent.createChooser(intent, "Choose One"));

                                } catch (Exception e) {
                                }

                            });

                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                            builder.setMessage("ID: " + postId + " Related Post Not Found.");
                            builder.setTitle("Message");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Back", (DialogInterface.OnClickListener) (dialog, which) -> {
                                finish();
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void loadComments() {
        firebaseDatabase.getReference("Posts/" + postId + "/comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            commentModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Comment comment = dataSnapshot.getValue(Comment.class);
                                String commentId = dataSnapshot.getKey();
                                String commentUserId = comment.getToUserId();
                                String commentTitle = comment.getComment();
                                String commentAt = comment.getCommentAt();

                                commentModelList.add(new CommentModel(commentId, commentUserId, commentTitle, commentAt));

                            }
                            Collections.reverse(commentModelList);
                            commentAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void commentStore() {
        if (privateStorage.isConnectedToInternet()) {

            if (privateStorage.isUserLogin()) {
                if (binding.cComment.getText().toString().length() > 0) {
                    progressDialog.setMessage("Comment Adding...");
                    progressDialog.show();
                    String commentId = firebaseDatabase.getReference("Posts/" + postId + "/comments/").push().getKey();
                    String userId = privateStorage.userDetail().put(USER_ID, null);
                    String commentText = binding.cComment.getText().toString();
                    String commentAt = privateStorage.dateTime(System.currentTimeMillis());
                    firebaseDatabase.getReference("Posts/" + postId + "/comments/")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int commentsCount = (int) snapshot.getChildrenCount();

                                    Comment comment = new Comment(userId, commentText, commentAt);
                                    firebaseDatabase.getReference("Posts/" + postId + "/comments/")
                                            .child(commentId)
                                            .setValue(comment)
                                            .addOnSuccessListener(unused -> {
                                                firebaseDatabase.getReference("Posts/" + postId + "/commentsCount")
                                                        .setValue(commentsCount + 1);
                                                binding.cCommentInputBox.setVisibility(View.GONE);
                                                binding.cComment.setText("");
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(binding.cComment.getWindowToken(), 0);
                                                binding.cCommentBoxOpenView.setVisibility(View.GONE);

                                                firebaseDatabase.getReference("Posts/" + postId + "/postUserId/")
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()) {
                                                                    String nId = firebaseDatabase.getReference("Notifications").push().getKey();
                                                                    String nType = "Comment";
                                                                    String nToUserId = userId;
                                                                    String nFromUserId = snapshot.getValue().toString();
                                                                    String nPostId = postId;
                                                                    String nMessage = commentText;
                                                                    String nDate = privateStorage.dateTime(System.currentTimeMillis());

                                                                    if (nFromUserId != nToUserId) {

                                                                        Notification notification = new Notification(nType, nToUserId, nFromUserId, nPostId, nMessage, "", nDate);
                                                                        firebaseDatabase.getReference("Notifications")
                                                                                .child(nId)
                                                                                .setValue(notification);

                                                                    }

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                progressDialog.dismiss();
                                                Toasty.Message(getApplicationContext(), "Comment Added Success.");

                                            }).addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                Toasty.Message(getApplicationContext(), "Comment Added Failed.");
                                            });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                    Toasty.Message(getApplicationContext(), "Connection Failed");
                                }
                            });
                } else {
                    Toasty.Message(this, "Comment Can't Be Empty.");
                }

            } else {
                Toasty.Message(this, "Please Login...");
            }
        } else {
            Toasty.Message(this, "Internet Connection Enable.");
        }
    }


}