package com.jadhav.aakash.activities;

import static com.jadhav.aakash.supports.PrivateStorage.PROFILE_BITMAP_IMAGE;
import static com.jadhav.aakash.supports.PrivateStorage.USERNAME;
import static com.jadhav.aakash.supports.PrivateStorage.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.databinding.ActivityCommentBinding;
import com.jadhav.aakash.supports.Comment;
import com.jadhav.aakash.supports.Post;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Toasty;
import com.jadhav.aakash.ui.comment.CommentAdapter;
import com.jadhav.aakash.ui.comment.CommentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class CommentActivity extends AppCompatActivity {

    private static String postId;
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

                            // likes and comments count code write here ... //

                        } else {
                            Toasty.Message(getApplicationContext(), "Not Found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.cCommentBoxOpen.setOnClickListener(view -> {
            binding.cCommentInputBox.setVisibility(View.VISIBLE);
            binding.cComment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.cComment, InputMethodManager.SHOW_FORCED);
        });

        binding.cCommentBoxClose.setOnClickListener(view -> {
            binding.cCommentInputBox.setVisibility(View.GONE);
            if (this.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        binding.cBoxClose.setOnClickListener(view -> {
            binding.cCommentInputBox.setVisibility(View.GONE);
            if (this.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            super.onBackPressed();
            finish();

        });

        commentAdapter = new CommentAdapter(commentModelList, this, postId);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        binding.commentRecyclerView.setLayoutManager(manager);
        binding.commentRecyclerView.setAdapter(commentAdapter);

        loadComments();


        binding.cCommentBtn.setOnClickListener(view -> {
            commentStore();
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

                        } else {
                            Toasty.Message(getApplicationContext(), "Any Comment No Found.");
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
                                                if ((View) getCurrentFocus() != null) {
                                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(((View) getCurrentFocus()).getWindowToken(), 0);
                                                }

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