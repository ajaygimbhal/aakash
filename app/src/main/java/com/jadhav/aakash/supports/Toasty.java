package com.jadhav.aakash.supports;

import android.content.Context;
import android.widget.Toast;

public class Toasty {

    public static void Message(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show();
    }
}
