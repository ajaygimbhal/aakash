package com.jadhav.aakash.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jadhav.aakash.BuildConfig;
import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.ActivityMainBinding;
import com.jadhav.aakash.supports.PrivateStorage;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    FirebaseDatabase firebaseDatabase;
    PrivateStorage privateStorage;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        privateStorage = new PrivateStorage(this);
        firebaseDatabase = FirebaseDatabase.getInstance();


        try {
            // this friend redirect code write here
            if (getIntent().getAction().equals(Intent.ACTION_VIEW) && getIntent().getData() != null) {
                String friendUrl = String.valueOf(getIntent().getData());
                Log.d(TAG, "onCreate: " + friendUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_friends, R.id.navigation_add_post, R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        appUpdateCheck();
    }

    private void appUpdateCheck() {
        
        firebaseDatabase.getReference("versionCode")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int versionCode = Integer.parseInt(snapshot.getValue().toString());
                            if (versionCode > BuildConfig.VERSION_CODE) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Update App");
                                builder.setMessage("Please Update App On Your Mobile.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Update", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(getResources().getString(R.string.web_root_url)));
                                    startActivity(intent);
                                });
                                builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                Log.d(TAG, "onDataChange: working");

                            } else {
                                Log.d(TAG, "onDataChange: not working");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}