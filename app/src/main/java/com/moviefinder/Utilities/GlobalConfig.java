package com.moviefinder.Utilities;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.moviefinder.R;

/**
 * Created by training on 18/04/17.
 */

public class GlobalConfig {

    public static String BASE_URL = "http://www.omdbapi.com/";

    //    ----------------------------Customer Section API----------------------------
    public static String LOGIN_METHOD = "/customers/authenticate";
    public static String FB_LOGIN_METHOD = "/customers/registerViaFacebook";

    public static void showSnackBar(String message, CoordinatorLayout coordinatorLayout) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    public static boolean checkConnection(Context context, CoordinatorLayout coordinatorLayout) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            showSnack(context, false, coordinatorLayout);
            return false;
        } else {
            return true;
        }
    }

    public static void showSnack(Context context, boolean isConnected, CoordinatorLayout coordinatorLayout) {
        String message = "";
        int color = ContextCompat.getColor(context, R.color.colorPrimary);
        if (!isConnected) {
            message = "Sorry! Not connected to internet";
        } else {
            message = "Connected to internet";
        }
        final Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}

