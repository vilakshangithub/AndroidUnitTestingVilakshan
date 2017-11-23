package com.android.vilakshansaxena.androidunittesting.espressocode.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseWidgetFragment extends BaseFragment {

    protected static final int RESULT_CODE_APPOINTMENT_WIDGET = 4001;
    protected static final int REQUEST_CODE_PROFILE_WIDGET = 4002;
    public static final int REQUEST_CODE_REACH_WIDGET = 4003;
    protected static final int REQUEST_CODE_HEALTHFEED_WIDGET = 4004;
    protected static final int REQUEST_CODE_CONSULT_WIDGET = 4005;
    protected static final int REQUEST_CODE_FEEDBACK_WIDGET = 4006;
    protected static final int REQUEST_CODE_EMAIL_VERIFICATION = 4007;


    public abstract void onRefresh(boolean isSwipeToRefresh);

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh(false);
    }
}
