package com.jadhav.aakash.supports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.jadhav.aakash.activities.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public static String convertTimeToAgo(String date) {

        String convertTime = null;
        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date pasTime =  dateFormat.parse(date);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                if (second == 1) {
                    convertTime = second + " second " + suffix;
                } else {
                    convertTime = second + " seconds " + suffix;
                }
            } else if (minute < 60) {
                if (minute == 1) {
                    convertTime = minute + " minute " + suffix;
                } else {
                    convertTime = minute + " minutes " + suffix;
                }
            } else if (hour < 24) {
                if (hour == 1) {
                    convertTime = hour + " hour " + suffix;
                } else {
                    convertTime = hour + " hours " + suffix;
                }
            } else if (day >= 7) {
                if (day >= 365) {
                    long tempYear = day / 365;
                    if (tempYear == 1) {
                        convertTime = tempYear + " year " + suffix;
                    } else {
                        convertTime = tempYear + " years " + suffix;
                    }
                } else if (day >= 30) {
                    long tempMonth = day / 30;
                    if (tempMonth == 1) {
                        convertTime = (day / 30) + " month " + suffix;
                    } else {
                        convertTime = (day / 30) + " months " + suffix;
                    }
                } else {
                    long tempWeek = day / 7;
                    if (tempWeek == 1) {
                        convertTime = (day / 7) + " week " + suffix;
                    } else {
                        convertTime = (day / 7) + " weeks " + suffix;
                    }
                }
            } else {
                if (day == 1) {
                    convertTime = day + " day " + suffix;
                } else {
                    convertTime = day + " days " + suffix;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TimeAgo", e.getMessage() + "");
        }
        return convertTime;
    }

    public boolean isUserLogin() {
        return preferences.getBoolean(LOGIN, false);
    }

    public void userSessionManage(String id, String username, String email, String bitMapImg) {
        editor.putBoolean(LOGIN, true);
        editor.putString(USER_ID, id);
        editor.putString(USERNAME, username);
        editor.putString(EMAIL, email);
        editor.putString(PROFILE_BITMAP_IMAGE, bitMapImg);
        editor.apply();
    }

    public void userProfileImage(String bitMap) {
        editor.putString(PROFILE_BITMAP_IMAGE, bitMap);
        editor.commit();
    }

    public int getUserMemberCount() {
        return preferences.getInt(USER_TOTAL_MEMBER, 0);
    }

    public void setUserMemberCount(int member) {
        editor.putInt(USER_TOTAL_MEMBER, member);
        editor.commit();
    }

    public HashMap<String, String> userDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, preferences.getString(USER_ID, null));
        user.put(USERNAME, preferences.getString(USERNAME, null));
        user.put(EMAIL, preferences.getString(EMAIL, null));
        user.put(PROFILE_BITMAP_IMAGE, preferences.getString(PROFILE_BITMAP_IMAGE, null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        context.startActivity(new Intent(context, LoginActivity.class));
        // FirebaseMessaging.getInstance().deleteToken();
        Toasty.Message(context, "LogOut Successfully.");
        ((Activity) context).finishAffinity();
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public String dateTime(long nowTime) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        String strDate = sdfDate.format(nowTime);
        return strDate;
    }

}
