package com.android.vilakshansaxena.androidunittesting.espressocode.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftInputUtils {

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            hideKeyboard(activity, activity.getCurrentFocus());
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        if (activity != null) {
            InputMethodManager mInputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null && view != null) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            if (view != null) {
                view.clearFocus();
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            InputMethodManager mInputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null) {
                mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }
}
