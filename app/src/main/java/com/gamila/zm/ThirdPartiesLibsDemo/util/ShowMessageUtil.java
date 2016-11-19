package com.gamila.zm.ThirdPartiesLibsDemo.util;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zeinab on 4/30/2016.
 */
public class ShowMessageUtil {


    /**
     * handle error using snackbar
     */
    public static void showSnackBar(View view, String messageString, int messageTextColor) {

        Snackbar snackbar = Snackbar
                .make(view, messageString, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(messageTextColor);
        snackbar.show();
    }

    public static void showSnackBarWithAction(View view, String messageString, int messageTextColor,
                                              String actionString, View.OnClickListener actionOnClickListener,
                                              int actionTextColor) {
        Snackbar snackbar = Snackbar
                .make(view, messageString, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(messageTextColor);
        if (!actionString.isEmpty() && actionOnClickListener != null) {
            snackbar.setAction(actionString, actionOnClickListener);
            snackbar.setActionTextColor(actionTextColor);
        }
        snackbar.show();
    }
}
