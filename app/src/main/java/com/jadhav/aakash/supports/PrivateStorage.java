package com.jadhav.aakash.supports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jadhav.aakash.activities.LoginActivity;

import java.util.HashMap;

public class PrivateStorage {
    public static final String PREF_NAME = "private_storage_aa";
    public static final String LOGIN = "is_login";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PROFILE_BITMAP_IMAGE = "profile_bitmap_image";
    public static final String USER_TOTAL_MEMBER = "user_total_member";
    private SharedPreferences preferences;
    private Context context;
    private SharedPreferences.Editor editor;


    public PrivateStorage(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public boolean isUserLogin(){
        return preferences.getBoolean(LOGIN, false);
    }

    public void userSessionManage(String id, String username, String email,  String bitMapImg){
        editor.putBoolean(LOGIN, true);
        editor.putString(USER_ID, id);
        editor.putString(USERNAME, username);
        editor.putString(EMAIL, email);
        editor.putString(PROFILE_BITMAP_IMAGE, bitMapImg);
        editor.apply();
    }

    public void userProfileImage(String bitMap){
        editor.putString(PROFILE_BITMAP_IMAGE, bitMap);
        editor.commit();
    }

    public void setUserMemberCount(int member){
        editor.putInt(USER_TOTAL_MEMBER, member);
        editor.commit();
    }

    public int getUserMemberCount(){
        return preferences.getInt(USER_TOTAL_MEMBER, 0);
    }


    public HashMap<String, String> userDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, preferences.getString(USER_ID, null));
        user.put(USERNAME, preferences.getString(USERNAME, null));
        user.put(EMAIL, preferences.getString(EMAIL, null));
        user.put(PROFILE_BITMAP_IMAGE, preferences.getString(PROFILE_BITMAP_IMAGE, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        context.startActivity(new Intent(context, LoginActivity.class));
        // FirebaseMessaging.getInstance().deleteToken();
        Toasty.Message(context, "LogOut Successfully.");
        ((Activity)context).finishAffinity();
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
