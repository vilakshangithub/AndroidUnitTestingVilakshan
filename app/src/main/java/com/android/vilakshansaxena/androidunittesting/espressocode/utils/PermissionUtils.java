package com.android.vilakshansaxena.androidunittesting.espressocode.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.vilakshansaxena.androidunittesting.R;


public class PermissionUtils {

    public static final int REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_CODE_SETTINGS = 3;
    public static final int REQUEST_CODE_CALL_PHONE = 4;
    private static final int REQUEST_READ_CALL_LOG = 5;
    public static final int REQUEST_READ_PHONE_STATE = 6;
    private static final int REQUEST_SYSTEM_ALERT_WINDOW = 7;
    public static final int REQUEST_CODE_OVERLAY_PERMISSION = 8;
    public static final int REQUEST_CODE_RECEIVE_SMS = 9;

    /**
     * Return true if the permission available, else starts permission request from user
     *
     * @param activity   Source Activity
     * @param permission Permission for which Snackbar has to be shown,
     *                   helps in deciding the message string for Snackbar
     */
    public static boolean requestPermission(@NonNull final Activity activity,
                                            @NonNull final String permission) {

        if (checkSelfPermission(activity, permission)) {
            return true;
        } else {
            int requestCode = getRequestCodeByPermission(permission);

            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);
            return false;
        }
    }

    /**
     * Return true if the permission available, else starts permission request from user
     *
     * @param fragment   Source Fragment
     * @param permission Permission for which Snackbar has to be shown,
     *                   helps in deciding the message string for Snackbar
     */
    public static boolean requestPermission(@NonNull final Fragment fragment,
                                            @NonNull final String permission) {
        if (checkSelfPermission(fragment.getActivity(), permission)) {
            return true;
        } else {
            int requestCode = getRequestCodeByPermission(permission);

            fragment.requestPermissions(
                    new String[]{permission},
                    requestCode);
            return false;
        }
    }


    private static boolean checkSelfPermission(@NonNull final Activity activity,
                                               @NonNull final String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @param permission Permission for which Snackbar has to be shown,
     *                   helps in deciding the message string for Snackbar
     */
    private static int getRequestCodeByPermission(final String permission) {
        switch (permission) {
            case Manifest.permission.CALL_PHONE:
                return REQUEST_CODE_CALL_PHONE;
            case Manifest.permission.READ_CALL_LOG:
                return REQUEST_READ_CALL_LOG;
            case Manifest.permission.READ_PHONE_STATE:
                return REQUEST_READ_PHONE_STATE;
            case Manifest.permission.RECEIVE_SMS:
                return REQUEST_CODE_RECEIVE_SMS;
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                return REQUEST_SYSTEM_ALERT_WINDOW;
            default:
                return REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE;
        }
    }

    /**
     * @param permission Permission for which Snackbar has to be shown,
     *                   helps in deciding the message string for Snackbar
     */
    private static int getPermissionDeclinedMessage(final String permission) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return R.string.app_name;
            case Manifest.permission.CALL_PHONE:
                return R.string.app_name;
            case Manifest.permission.READ_CALL_LOG:
                return R.string.app_name;
            case Manifest.permission.READ_PHONE_STATE:
                return R.string.app_name;
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                return R.string.app_name;
            default:
                return R.string.app_name;
        }
    }

    /**
     * @param activity Context where the Settings screen will open
     */
    public static void openSettingsScreen(final Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null); // NON-NLS
        intent.setData(uri);
        activity.startActivityForResult(intent, REQUEST_CODE_SETTINGS);
    }

    /**
     * @param activity   Context where the Snackbar will be shown
     * @param permission Permission for which Snackbar has to be shown,
     *                   helps in deciding the message string for Snackbar
     * @return snackbar snackbar instance which can be useful to set callbacks,if needed
     */
    public static Snackbar showPermissionDeclineMessage(@NonNull final Activity activity,
                                                        @NonNull final String permission) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content),
                getPermissionDeclinedMessage(permission), Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.app_name, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                openSettingsScreen(activity);
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.colorAccent)).show();
        return snackbar;
    }

//    public static void showOverlayPermissionDialog(final Activity activity,
//                                                   DialogInterface.OnClickListener negativeListener) {
//        new AlertDialogPlus.Builder(activity, R.style.AppTheme_Dialog_Alert)
//                .setCancelable(false)
//                .setMessage(activity.getString(R.string.enable_overlay_permissions))
//                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent =
//                                new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                                  Uri.parse("package:" + activity.getPackageName())); // NON-NLS
//                        activity.startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
//                    }
//                })
//                .setNegativeButton(R.string.dont_allow, negativeListener).show();
//    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean canDrawOverlays(Activity context) {
        return !DeviceUtils.hasMarshmallow() || Settings.canDrawOverlays(context);
    }

}

