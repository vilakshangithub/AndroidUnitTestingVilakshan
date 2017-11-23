package com.android.vilakshansaxena.androidunittesting.espressocode.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.vilakshansaxena.androidunittesting.R;
import com.android.vilakshansaxena.androidunittesting.espressocode.adapters.CalendarAdapter;
import com.android.vilakshansaxena.androidunittesting.espressocode.model.AppointmentItem;
import com.android.vilakshansaxena.androidunittesting.espressocode.model.CalendarItem;
import com.android.vilakshansaxena.androidunittesting.espressocode.model.EventItem;
import com.android.vilakshansaxena.androidunittesting.espressocode.model.PracticeItem;
import com.android.vilakshansaxena.androidunittesting.espressocode.utils.DeviceUtils;
import com.android.vilakshansaxena.androidunittesting.espressocode.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarWidgetFragment extends BaseWidgetFragment implements
        LoaderManager.LoaderCallbacks<List<CalendarItem>>, View.OnClickListener,
        CalendarAdapter.AppointmentListAdapterHandler {

    private static final String ANALYTICS_CATEGORY = "appointments_widget";
    private static final int LOADER_ID = 9001;
    public static final char COLON = ':';
    private View mRayView;
    private View mNoRayView;
    private View mEmptyView;
    private View mRayActiveView;
    private View mRayInactiveView;
    private View mRayActiveFooterView;
    private View mRayInactiveFooterView;
    private TextView mCardInactiveHeaderView;
    private TextView mCardInactivePracticeNameView;
    private TextView mCardInactiveBodyView;
    private RecyclerView mEventAppointmentRecycler;
    private CalendarAdapter mEventAppointmentAdapter;
    private String mCurrentPracticeId;
    public static final int SINGLE_WIDGET_ITEM = 1;
    public static final int WIDGET_APPOINTMENT_COUNT = 2;
    private AppointmentItem mSelectedAppointment;
    private int mSelectedDoctorId;
    private TextView mRayLeadGenSuccess;
    private TextView mRayLeadGenFail;
    private TextView mRayRequested;
    private View mRequestingProgressLayout;
    private Button mRayInactiveAction;
    private Button mStartTrialButton;
    private TextView mRayPromoText;

    private int mRayCardInactiveMode;
    private static final int CALL_PATIENT = 0;
    private static final int CALL_SUPPORT = 1;
    private int mCallMode;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getContext();
        mCurrentPracticeId = "1234";
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_widget_calendar, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRayView = view.findViewById(R.id.ray_widget_view);
        mNoRayView = view.findViewById(R.id.no_ray_widget_view);
        mRayActiveView = view.findViewById(R.id.relative_layout_card_active);
        mRayInactiveView = view.findViewById(R.id.linear_layout_card_inactive);
        mRayActiveFooterView = view.findViewById(R.id.relative_layout_card_active_footer);
        mRayInactiveFooterView = view.findViewById(R.id.relative_layout_card_inactive_footer);
        mCardInactiveHeaderView = (TextView) view.findViewById(R.id.tv_label_card_inactive_header);
        mCardInactivePracticeNameView = (TextView) view.findViewById(R.id.tv_label_card_inactive_practice_name);
        mCardInactiveBodyView = (TextView) view.findViewById(R.id.tv_label_card_inactive_body);
        mRayInactiveAction = (Button) view.findViewById(R.id.button_card_inactive);

        mEventAppointmentRecycler = (RecyclerView) view.findViewById(R.id.recycler_view_event_appointments);
        mEventAppointmentRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                                                                                  false));
        mEventAppointmentRecycler.setMotionEventSplittingEnabled(false);

        mEmptyView = view.findViewById(R.id.text_view_no_appointments);
        Set<String> practiceModules = PreferenceManager.getDefaultSharedPreferences(
                getContext()).getStringSet("current_practice_modules",
                new HashSet<String>());
        mEventAppointmentAdapter = new CalendarAdapter(getContext(), practiceModules, this, -1, 1);
        mEventAppointmentRecycler.setAdapter(mEventAppointmentAdapter);

        view.findViewById(R.id.view_all_appointments_button).setOnClickListener(this);
        view.findViewById(R.id.button_add_appointment).setOnClickListener(this);
        mRayInactiveAction.setOnClickListener(this);
        mStartTrialButton = (Button) view.findViewById(R.id.tv_label_get_ray);
        mStartTrialButton.setOnClickListener(this);
        if (areAllPracticesBlocked(getContext())) {
            mStartTrialButton.setText("Get Ray");
        } else {
            mStartTrialButton.setText("Start Trial");
        }

        mRayPromoText = (TextView) view.findViewById(R.id.tv_label_no_ray);
        mRayLeadGenSuccess = (TextView) view.findViewById(R.id.tv_label_get_ray_request_sent);
        mRayLeadGenFail = (TextView) view.findViewById(R.id.tv_label_get_ray_request_fail);
        mRayRequested = (TextView) view.findViewById(R.id.tv_label_ray_requested);
        mRequestingProgressLayout = view.findViewById(R.id.promo_widget_progress_bar);
        mRequestingProgressLayout.setVisibility(View.GONE);

        assignVectorDrawable();
    }

    public void showStatusOnWidget(Context context, boolean success) {
        mRequestingProgressLayout.setVisibility(View.GONE);
        if (success) {
            mRayPromoText.setText("Request Sent");
            setRayRequested(true);
            mRayLeadGenSuccess.setVisibility(View.VISIBLE);
            mStartTrialButton.setVisibility(View.GONE);
            mRayLeadGenFail.setVisibility(View.GONE);
        } else {
            setRayPromoStatusMessage("Request Fail");
            setRayRequested(false);
        }
    }

    private void setRayPromoStatusMessage(String message) {
        mRayLeadGenFail.setText(message);
        mRayLeadGenFail.setVisibility(View.VISIBLE);
        mStartTrialButton.setVisibility(View.VISIBLE);
    }

    private void setRayRequested(boolean isRayRequested) {
        if (isRayRequested) {
            mRayRequested.setVisibility(View.VISIBLE);
        } else {
            mRayRequested.setVisibility(View.GONE);
        }
    }

    private void assignVectorDrawable() {
        VectorDrawableCompat vectorTick = VectorDrawableCompat.create(getResources(),
                R.drawable.vc_back_color_white, null);
        VectorDrawableCompat vectorCross =
                VectorDrawableCompat.create(getResources(), R.drawable.vc_cross_color_white, null);
        mRayLeadGenSuccess.setCompoundDrawablesWithIntrinsicBounds(vectorTick, null, null, null);
        mRayLeadGenFail.setCompoundDrawablesWithIntrinsicBounds(vectorCross, null, null, null);
    }

    private boolean isRayEnabled(Context context) {
        return true;
    }

    private boolean areAllPracticesBlocked(Context context) {
        return false;
    }

    private void triggerLoading() {
        if (isFragmentActive()) {
            if (isRayEnabled(getContext()) && !areAllPracticesBlocked(getContext())) {
                hideSummaryView();
                getLoaderManager().restartLoader(LOADER_ID, null, this);
            } else {
                showSummaryView();
            }
        }
    }

    private void showSummaryView() {
        mRayPromoText.setText("Welcome");
        mNoRayView.setVisibility(View.VISIBLE);
        mRayView.setVisibility(View.GONE);
        mStartTrialButton.setVisibility(View.VISIBLE);
        setRayRequested(true);
        mRayLeadGenSuccess.setVisibility(View.GONE);
        mRayLeadGenFail.setVisibility(View.GONE);
    }

    private void hideSummaryView() {
        mNoRayView.setVisibility(View.GONE);
        mRayView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh(boolean isSwipeToRefresh) {
        if (isSwipeToRefresh) {
            if (isInternetConnected(true)) {
                //Sync Data
            } else {
                triggerLoading();
            }
        } else {
            String currentPracticeId = "1234";
            if (!mCurrentPracticeId.equals(currentPracticeId)) {
                //If in onResume preference practice has changed from onCreate backup practice id then
                //restart loader
                mCurrentPracticeId = currentPracticeId;
                triggerLoading();
            } else {
                //If current practice is not changed then init the loader
                //this is called in onResume as when user gets Ray subscription the card should be changed
                //the instant this screen is resumed
                triggerLoading();
            }
        }
    }

    private Date getTodayDate() {
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarToday.set(Calendar.MINUTE, 0);
        calendarToday.set(Calendar.SECOND, 0);
        calendarToday.set(Calendar.MILLISECOND, 0);
        return calendarToday.getTime();
    }

    @VisibleForTesting
    public CalendarAdapter getAdapter() {
        return mEventAppointmentAdapter;
    }


    @Override
    public Loader<List<CalendarItem>> onCreateLoader(final int id, final Bundle args) {
        String practiceId = "1234";
        mSelectedDoctorId = 123;
        //Return new instance of loader
        return null;
    }

    @VisibleForTesting
    public void changeDataSet(final List<CalendarItem> data) {
        mRayActiveView.setVisibility(View.VISIBLE);
        mRayInactiveView.setVisibility(View.GONE);
        mRayActiveFooterView.setVisibility(View.VISIBLE);
        mRayInactiveFooterView.setVisibility(View.GONE);
        int index = 0;
        for (CalendarItem item : data) {
            if (item instanceof EventItem) {
                EventItem eventItem = (EventItem) item;
                if (!eventItem.allDayEvent) {
                    break;
                } else {
                    ++index;
                }
            } else {
                break;
            }
        }
        if (index > 0 && index != data.size()) {
            List<Integer> positionsList = new ArrayList<>(1);
            positionsList.add(index - 1);
        }

        setSelectedDoctorId(mSelectedDoctorId);
        mEventAppointmentAdapter.changeDataSet(data);
    }

    private void changeDataSetForExpiredPractice(final List<CalendarItem> data) {
        mRayActiveView.setVisibility(View.GONE);
        mRayInactiveView.setVisibility(View.VISIBLE);
        mRayActiveFooterView.setVisibility(View.GONE);
        mRayInactiveFooterView.setVisibility(View.VISIBLE);
        PracticeItem practiceItem = (PracticeItem) data.get(0);
        mCardInactiveHeaderView.setText("Subscription Expired");
        mCardInactivePracticeNameView.setVisibility(View.VISIBLE);
        mCardInactivePracticeNameView.setText("Subscription for item expired");
        mCardInactiveBodyView.setText("No action required");
        mRayInactiveAction.setText("Contact Us");
        mRayInactiveAction.setEnabled(true);
        mRayCardInactiveMode = 1;
    }

    private void changeDataSetForAppOutOfSync() {
        mRayActiveView.setVisibility(View.GONE);
        mRayInactiveView.setVisibility(View.VISIBLE);
        mRayActiveFooterView.setVisibility(View.GONE);
        mRayInactiveFooterView.setVisibility(View.VISIBLE);
        mCardInactiveHeaderView.setText("Out Of Sync");
        mCardInactivePracticeNameView.setVisibility(View.GONE);
        mCardInactivePracticeNameView.setText("");
        mCardInactiveBodyView.setText("Out Of Sync");
        setSyncActionLayout();
        mRayCardInactiveMode = 2;
    }

    private void setSyncActionLayout() {
        //Set sync layout
    }

    @VisibleForTesting
    public void setSelectedDoctorId(int doctorId) {
        mEventAppointmentAdapter.setSelectedDoctorId(doctorId);
    }

    @Override
    public void onLoadFinished(final Loader<List<CalendarItem>> loader, final List<CalendarItem> data) {
        if (true) {
            changeDataSetForAppOutOfSync();
        } else {
            if (data != null) {
                if (data.size() == SINGLE_WIDGET_ITEM && data.get(0) instanceof PracticeItem) {
                    changeDataSetForExpiredPractice(data);
                } else {
                    changeDataSet(data);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(final Loader<List<CalendarItem>> loader) {
        mEventAppointmentAdapter.changeDataSet(null);
        setSelectedDoctorId(mSelectedDoctorId);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.view_all_appointments_button:
                openRayCalendar();
                break;
            case R.id.button_add_appointment:
                addAppointment();
                break;
            case R.id.tv_label_get_ray:
                handlePromoCardTap();
                break;
            case R.id.button_card_inactive:
                if (mRayCardInactiveMode == 1) {
                    callSupport();
                } else {
                    //Force Sync
                    setSyncActionLayout();
                }
                break;
        }
    }

    private void handlePromoCardTap() {
        if (isInternetConnected(false)) {
            if (areAllPracticesBlocked(getContext())) {
                handleDirectLeadGen();
            } else {
                if (true) {
                    startTrial();
                } else {
                    handleDirectLeadGen();
                }
            }
        } else {
            setRayRequested(false);
            setRayPromoStatusMessage("No internet");
        }
    }

    private void handleDirectLeadGen() {
        String emailAddress = "email@123";
        if (TextUtils.isEmpty(emailAddress)) {
            //Start email activity
        } else {
            requestRaySubscription();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String verifiedEmail = "email@123";
        if (TextUtils.isEmpty(verifiedEmail)) {
            mRequestingProgressLayout.setVisibility(View.VISIBLE);
            mStartTrialButton.setVisibility(View.GONE);
        }
    }

    public void onAccountUpdated() {
        mRequestingProgressLayout.setVisibility(View.GONE);
        mStartTrialButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void requestRaySubscription() {
        mRequestingProgressLayout.setVisibility(View.VISIBLE);
        mStartTrialButton.setVisibility(View.GONE);
       //Post new request
    }

    private void addAppointment() {
        //Start select patient activity
    }

    private void openRayCalendar() {
        Bundle bundle = new Bundle();
        //Start appointment activity
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case RESULT_CODE_APPOINTMENT_WIDGET:
                onRefresh(false);
                break;
            case REQUEST_CODE_EMAIL_VERIFICATION:
                if (Activity.RESULT_OK == resultCode) {
                    requestRaySubscription();
                }
                break;
            case 1001:
                if (Activity.RESULT_OK == resultCode && data != null) {
                    Bundle resultBundle = data.getExtras();
                    if (resultBundle != null) {
                        int screenFinishMode = 3;
                        if (screenFinishMode == 3) {
                            //Start sign up activity
                        } else {
                            showStatusOnWidget(getContext(), true);
                        }
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void addFile(final AppointmentItem appointmentItem) {
        mSelectedAppointment = appointmentItem;
        if (DeviceUtils.hasMarshmallow()) {
            if (PermissionUtils.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Start add file activity
            }
        } else {
            //Start add file activity
        }
    }

    public void callPatient(final AppointmentItem appointmentItem) {
        mCallMode = CALL_PATIENT;
        mSelectedAppointment = appointmentItem;
        handleCallFunctionality("1234567890");
    }

    private void callSupport() {
        mCallMode = CALL_SUPPORT;
        handleCallFunctionality("1234567890");
    }

    private void handleCallFunctionality(String phoneNumber) {
        if (DeviceUtils.hasMarshmallow()) {
            if (PermissionUtils.requestPermission(this, Manifest.permission.CALL_PHONE)) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(
                        DeviceUtils.CALL_INTENT_ACTION_URI + phoneNumber));
                startActivity(callIntent);
            }
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(
                    DeviceUtils.CALL_INTENT_ACTION_URI + phoneNumber));
            startActivity(callIntent);
        }
    }

    public void showMessageBar(String message, boolean isErrorMsg) {
        if (isFragmentActive()) {
            if (isErrorMsg) {
                showErrorMessage(message);
            } else {
                showMessage(message);
            }
        }
    }

    @VisibleForTesting
    public void showPermissionErrorMessage(final String message) {
        showErrorMessage(message,
                "Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        PermissionUtils.openSettingsScreen(getActivity());
                    }
                }, false);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_CODE_CALL_PHONE:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mCallMode == CALL_PATIENT) {
                        handleCallFunctionality("1234567890");
                    } else if (mCallMode == CALL_SUPPORT) {
                        handleCallFunctionality("1234567890");
                    }
                } else {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        showPermissionErrorMessage("Permission Revoked");
                    }
                }
                break;
            case PermissionUtils.REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addFile(mSelectedAppointment);
                } else {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        showPermissionErrorMessage("Permission Revoked");
                    }
                }
                break;
        }
    }

    private void startTrial() {
        //Handle logic for start trial
    }
}
