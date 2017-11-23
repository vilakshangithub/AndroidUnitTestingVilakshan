package com.android.vilakshansaxena.androidunittesting.espressocode.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.vilakshansaxena.androidunittesting.R;
import com.android.vilakshansaxena.androidunittesting.espressocode.utils.ConnectionUtils;
import com.android.vilakshansaxena.androidunittesting.espressocode.utils.DeviceUtils;
import com.android.vilakshansaxena.androidunittesting.espressocode.utils.Messagebar;
import com.android.vilakshansaxena.androidunittesting.espressocode.utils.PermissionUtils;
import com.android.vilakshansaxena.androidunittesting.espressocode.utils.SoftInputUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    private Messagebar mMessagebar;

    public boolean isActivityAlive() {
        if (DeviceUtils.hasJellyBeanMR1()) {
            return !(null == getActivity() || getActivity().isDestroyed());
        } else {
            return !(null == getActivity() || getActivity().isFinishing());
        }

    }

    public boolean isFragmentActive() {
        return isActivityAlive() && isAdded();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMessagebar(view);
    }

    protected void initMessagebar(View view) {
        mMessagebar = Messagebar.newInstance(view);
    }

    public void showWarnMessage(String warnMessage) {
        showWarnMessage(warnMessage, false);
    }

    protected void showWarnMessage(String warnMessage, boolean persistent) {
        showWarnMessage(warnMessage, null, null, persistent);
    }

    protected void showWarnMessage(String warnMessage, String buttonText,
                                   View.OnClickListener onClickListener, boolean persistent) {
        mMessagebar.showWarnMessage(warnMessage, buttonText, onClickListener, persistent);
    }

    public void showErrorMessage(String errorMessage) {
        showErrorMessage(errorMessage, false);
    }

    public void showErrorMessage(String errorMessage, boolean persistent) {
        showErrorMessage(errorMessage, null, null, persistent);
    }

    public void showErrorMessage(String errorMessage, @Nullable String buttonText,
                                 @Nullable View.OnClickListener onClickListener, boolean persistent) {
        mMessagebar.showErrorMessage(errorMessage, buttonText, onClickListener, persistent);
    }

    public void showMessage(String message) {
        showMessage(message, false);
    }

    public void showMessage(String message, boolean persistent) {
        showMessage(message, null, null, persistent);
    }

    public void showMessage(String message, @Nullable String buttonText,
                            @Nullable View.OnClickListener onClickListener, boolean persistent) {
        mMessagebar.showMessage(message, buttonText, onClickListener, persistent, -1);
    }

    public void hideMessage() {
        mMessagebar.hideMessage();
    }

    public boolean isInternetConnected() {
        return isInternetConnected(true);
    }

    /**
     * @param showMessageBar True if you wanna show a messagebar, false otherwise
     * @return true if connected, false otherwise
     */
    public boolean isInternetConnected(boolean showMessageBar) {
        return isInternetConnected(showMessageBar, false);
    }

    /**
     * @param showMessageBar true if you wanna show a messagebar, false otherwise
     * @param persistent     true if Message bar is persistent
     * @return true if connected, false otherwise
     */
    public boolean isInternetConnected(boolean showMessageBar, boolean persistent) {
        ConnectionUtils connectionUtils = new ConnectionUtils(getContext());
        if (connectionUtils.isNetConnected()) {
            return true;
        }
        if (isAdded() && showMessageBar) {
            showErrorMessage("No Internet", persistent);
        }
        return false;
    }

    public void hideKeyboard() {
        SoftInputUtils.hideKeyboard(getActivity());
    }

    public void initToolbarWithTitle(View view, String title) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((TextView) view.findViewById(R.id.toolbar_title)).setText(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backDrawable = VectorDrawableCompat.create(getResources(),
                R.drawable.vc_back_color_white, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(backDrawable);
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setHomeActionContentDescription("content desc");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void initToolbarWithButton(View view, String title, View.OnClickListener onClickListener) {
        initToolbarWithTitle(view, title);

        final Button doneButton = (Button) view.findViewById(R.id.toolbar_button);
        doneButton.setText("Save");
        doneButton.setOnClickListener(onClickListener);
    }


    protected void initToolbarWithCloseButton(View view, @Nullable final String title,
                                              @Nullable final View.OnClickListener toolbarButtonOnClickListener) {
        initToolbarWithButton(view, title, toolbarButtonOnClickListener);

        Drawable appsDrawable =
                VectorDrawableCompat.create(getResources(), R.drawable.vc_cross_color_white, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(appsDrawable);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_CODE_CALL_PHONE:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCall();
                } else {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        showPermissionRevokedMessage();
                    }
                }
                break;
        }
    }

    private void showPermissionRevokedMessage() {
        showErrorMessage("Permission Revoked", "Settings",
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        PermissionUtils.openSettingsScreen(getActivity());
                    }
                }, false);
    }

    public void startCall() {
        //override in sub class
    }

    public boolean hasPhonePermissionGranted() {
        return PermissionUtils.requestPermission(this, Manifest.permission.CALL_PHONE);
    }
}
