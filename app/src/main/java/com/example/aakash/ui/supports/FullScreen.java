package com.example.aakash.ui.supports;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreen extends AppCompatActivity {

    public static View decorView;
    public static Window window;

    public FullScreen(View decorView, Window window) {
        this.decorView = decorView;
        this.window = window;
    }

    // window navigation show hide function start

    public  void transparentStatusBarAndNavigation(){
        if (Build.VERSION.SDK_INT>=19 && Build.VERSION.SDK_INT<21){
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT>=19){
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT>=21){
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(int i, boolean b) {
        Window win = window;
        WindowManager.LayoutParams winParams = win.getAttributes();

        if (b){
            winParams.flags |= i;
        }else{
            winParams.flags &= ~i;
        }
        win.setAttributes(winParams);

    }

    public void autoHiddenNavigationBar(){

        decorView = window.getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if (i==0){
                    decorView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });

    }

    private int hideSystemBar() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hideSystemBar());
        }
    }

}
