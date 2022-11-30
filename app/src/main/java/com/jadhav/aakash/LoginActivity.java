package com.jadhav.aakash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.databinding.ActivityLoginBinding;
import com.jadhav.aakash.supports.PrivateStorage;
import com.jadhav.aakash.supports.Toasty;
import com.jadhav.aakash.supports.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 999;
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    PrivateStorage privateStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        privateStorage = new PrivateStorage(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.signInButton.setSize(SignInButton.SIZE_STANDARD);
        binding.signInButton.setOnClickListener(view -> signIn());

    }

    private void signIn() {
        progressDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String id = database.getReference().child("Users").push().getKey();
            String username = account.getDisplayName();
            String email = account.getEmail();
            String profileImg = account.getPhotoUrl().toString();
            String timeStamp = String.valueOf(System.currentTimeMillis());
            User user = new User(username, email, profileImg, 0,timeStamp);
            Query queries = reference.orderByChild("email").equalTo(email);
            queries.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        reference.child(id).setValue(user)
                                .addOnCompleteListener(task -> {

                                    progressDialog.dismiss();

                                    privateStorage.userSessionManage(id, username, email, userPUrl(profileImg));

                                    try {
                                        Toasty.Message(getApplicationContext(), "Welcome to " + username.substring(0, username.lastIndexOf(' ')));
                                    } catch (Exception e) {
                                        Toasty.Message(getApplicationContext(), "Welcome to " + username);
                                    }


                                });

                    } else {
                        User user1 = snapshot.getChildren().iterator().next().getValue(User.class);
                        String userId = snapshot.getChildren().iterator().next().getKey();

                        privateStorage.userSessionManage(userId, user1.getUsername(), user1.getEmail(), userPUrl(user1.getProfileImg()));
                        progressDialog.dismiss();

                        try {
                            Toasty.Message(getApplicationContext(), "Welcome to come back " + user1.getUsername().substring(0, user1.getUsername().lastIndexOf(' ')));
                        } catch (Exception e) {
                            Toasty.Message(getApplicationContext(), "Welcome to come back " + user1.getUsername());
                        }

                    }
                    mGoogleSignInClient.signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toasty.Message(getApplicationContext(), "Try Again Failed.");
                    mGoogleSignInClient.signOut();
                }
            });


        } catch (ApiException e) {
            mGoogleSignInClient.signOut();
            Log.w(TAG, "signInResult:failed code= " + e.getStatusCode());
            Toasty.Message(this, "signInResult:failed code=" + e.getStatusCode());

        }
    }


    private String userPUrl(String url) {
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL uri = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}