package com.android.vilakshansaxena.androidunittesting.espressocode.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vilakshansaxena.androidunittesting.R;
import com.android.vilakshansaxena.androidunittesting.espressocode.model.AppointmentItem;
import com.android.vilakshansaxena.androidunittesting.espressocode.model.CalendarItem;
import com.android.vilakshansaxena.androidunittesting.espressocode.model.EventItem;
import com.android.vilakshansaxena.androidunittesting.espressocode.model.PracticeItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private static final String HYPHEN = " - ";
    private Context mContext;
    private List<CalendarItem> mCalendarItemList;
    private int mSelectedDoctorId = 123;
    private final String mDefaultProcedure;
    private boolean mIsWellnessPractice;
    private boolean mHasBilling;
    private boolean mHasGenericEMR;
    private AppointmentListAdapterHandler mAppointmentListAdapterHandler;
    private Calendar mSelectedDate;
    private ProgressDialog mProgressDialog;
    private final String mPatientLabel;
    private int mLoaderMode;

    private static final int VIEW_TYPE_EVENT = 1;
    private static final int VIEW_TYPE_SCHEDULED_APPOINTMENT = 2;
    private static final int VIEW_TYPE_CANCELLED_APPOINTMENT = 3;
    private static final int SCHEDULED_APPOINTMENT_COLUMN_COUNT = 4;
    private static final int CANCELLED_APPOINTMENT_COLUMN_COUNT = 3;

    public CalendarAdapter(Context context, final Set<String> practiceModules,
                           AppointmentListAdapterHandler appointmentListAdapterHandler, int selectedDoctorId,
                           int loaderMode) {
        mContext = context;
        mCalendarItemList = new ArrayList<>();
        mDefaultProcedure = "Default procedure";
        mSelectedDoctorId = selectedDoctorId;
        mAppointmentListAdapterHandler = appointmentListAdapterHandler;
        initPracticeData(practiceModules);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mPatientLabel = "Current patient label";
        mLoaderMode = loaderMode;
    }

    public void changeDataSet(List<CalendarItem> calendarItemList) {
        if (mCalendarItemList != null) {
            mCalendarItemList.clear();
            if (calendarItemList != null) {
                mCalendarItemList.addAll(calendarItemList);
            }
        }
        notifyDataSetChanged();
    }

    public void initPracticeData(final Set<String> practiceModules) {
        mHasBilling = true;
        mIsWellnessPractice = true;
        mHasGenericEMR = false;
    }

    @Override
    public int getItemViewType(final int position) {
        CalendarItem item = mCalendarItemList.get(position);
        if (isItemTypeEvent(item)) {
            return VIEW_TYPE_EVENT;
        } else {
            if (isAppointmentCancelled((AppointmentItem) item)) {
                return VIEW_TYPE_CANCELLED_APPOINTMENT;
            } else {
                return VIEW_TYPE_SCHEDULED_APPOINTMENT;
            }
        }
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemView;
        switch (viewType) {
            case VIEW_TYPE_EVENT:
            case VIEW_TYPE_SCHEDULED_APPOINTMENT:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_widget_calendar, parent, false);
                break;
            case VIEW_TYPE_CANCELLED_APPOINTMENT:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_widget_appointment, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_widget_appointment_no_ray, parent, false);
        }
        return new CalendarViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(final CalendarViewHolder viewHolder, final int position) {
        viewHolder.calendarItem = mCalendarItemList.get(position);
        switch (viewHolder.calendarViewType) {
            case VIEW_TYPE_EVENT:
                EventItem eventItem = (EventItem) viewHolder.calendarItem;
                String scheduleAtText;
                String scheduleTillText;

                if (eventItem.allDayEvent) {
                    scheduleAtText = "Scheduled At";
                    scheduleTillText = "";
                } else {
                    scheduleAtText = "10:00 AM";
                    scheduleTillText = "11:00 AM";
                }

                viewHolder.scheduledAtTextView.setText(scheduleAtText.toLowerCase());
                viewHolder.scheduledTillTextView.setText(scheduleTillText.toLowerCase());
                viewHolder.titleTextView.setText(eventItem.title);
                viewHolder.subTitleTextView.setText(eventItem.doctorName);
                viewHolder.setDoctor(eventItem.doctorName, eventItem.doctorColor);
                viewHolder.prepaidTextView.setVisibility(View.GONE);
                break;
            case VIEW_TYPE_CANCELLED_APPOINTMENT:
            case VIEW_TYPE_SCHEDULED_APPOINTMENT:
                AppointmentItem appointmentItem = (AppointmentItem) viewHolder.calendarItem;
                viewHolder.scheduledAtTextView.setText("10:00 AM");
                viewHolder.scheduledTillTextView.setText("");
                viewHolder.titleTextView.setText(appointmentItem.patientName);
                if (mSelectedDoctorId == 123) {
                    viewHolder.setDoctor(appointmentItem.doctorName, appointmentItem.doctorColor);
                } else {
                    viewHolder.subTitleTextView.setText("Proc 1, Proc 2");
                }
                if (viewHolder.calendarViewType == VIEW_TYPE_CANCELLED_APPOINTMENT) {
                    if ("No Show".equalsIgnoreCase(appointmentItem.cancelledReason)) {
                        viewHolder.cancelTypeTextView.setText("No Show");
                    } else {
                        viewHolder.cancelTypeTextView.setText("Cancelled");
                    }
                    viewHolder.prepaidTextView.setVisibility(View.GONE);
                } else {
                    if ("Fabric".equalsIgnoreCase(appointmentItem.source)) {
                        if (appointmentItem.prePaymentId != 0) {
                            viewHolder.prepaidTextView.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.prepaidTextView.setVisibility(View.GONE);
                        }
                    } else {
                        viewHolder.prepaidTextView.setVisibility(View.GONE);
                    }
                }
                break;

        }
    }

    private VectorDrawableCompat createVectorForImage(int resourceId) {
        return VectorDrawableCompat.create(mContext.getResources(), resourceId, null);
    }

    @Override
    public int getItemCount() {
        return mCalendarItemList.size();
    }

    private boolean isItemTypeEvent(final CalendarItem calendarItem) {
        return calendarItem.itemType == CalendarItem.ITEM_TYPE_EVENT;
    }

    private boolean isAppointmentCancelled(final AppointmentItem appointmentItem) {
        return "Cancelled".equalsIgnoreCase(appointmentItem.status);
    }

    private void showBottomSheet(final CalendarItem calendarItem) {
        //Handle show bottom shhet
    }

    private List<PracticeItem> createItems(boolean isCancelled) {
        //Handle create bottom sheet items
        return new ArrayList<>();
    }

    private void onAppointmentSheetItemSelected(String title, AppointmentItem appointmentItem) {

        if (title.equalsIgnoreCase("Add File")) {
            handleAddFile(appointmentItem);
        } else if (title.equalsIgnoreCase("Add Bill")) {
            addBill(appointmentItem);
        } else if (title.equalsIgnoreCase("Call Patient")) {
            callPatient(appointmentItem);
        } else if (title.equalsIgnoreCase("Cancel")) {
            handleCancelAppointment();
        } else if (title.equalsIgnoreCase("Schedule")) {
            handleScheduleAppointment(appointmentItem);
        } else if (title.equalsIgnoreCase("No Show")) {
            handleNoShow();
        } else if ((title.equalsIgnoreCase("Prescribe"))) {
            addPrescription(appointmentItem);
        } else if (title.equalsIgnoreCase("Edit")) {
            editAppointment(appointmentItem);
        } else if (title.equalsIgnoreCase("Followup")) {
            addAppointment(appointmentItem);
        }
    }

    private void addBill(final AppointmentItem appointmentItem) {
        if (appointmentItem.patientPractoId == 0) {
            mAppointmentListAdapterHandler.showMessageBar("Patient not synced", true);
        } else {
            //Handle treatment activity
        }
    }

    private void callPatient(final AppointmentItem appointmentItem) {
        if (TextUtils.isEmpty(appointmentItem.patientPrimaryMobile)) {
            //Handle patient profile activity
        } else {
            mAppointmentListAdapterHandler.callPatient(appointmentItem);
        }
    }

    private void addPrescription(final AppointmentItem appointmentItem) {
        //Handle prescription activity
    }

    public void setAppointmentDefaultDate(Calendar selectedDate) {
        mSelectedDate = selectedDate;
    }

    public void setSelectedDoctorId(int selectedDoctorId) {
        mSelectedDoctorId = selectedDoctorId;
    }

    private void addAppointment(final AppointmentItem appointmentItem) {
        //Handle add appointment
    }

    private void handleCancelAppointment() {
        //Handle cancel dialog
    }

    private void handleNoShow() {
        //Handle cancel schedule dialog
    }

    private void handleAddFile(AppointmentItem appointmentItem) {
        //Handle add file
    }

    private void handleScheduleAppointment(AppointmentItem appointmentItem) {
        //Handle schedule appointment
    }

    private void editAppointment(final AppointmentItem appointmentItem) {
        //Handle edit appointment
    }

    private void onAppointmentSelected(final AppointmentItem appointmentItem) {
        //Handle appointment edit / detail
    }


    private void onEventSelected(final EventItem eventItem) {
        //Handle event detail
    }

    public void onEditEventSelected(final long eventId) {
        if (true) {
            //Handle event add edit
        } else {
            mAppointmentListAdapterHandler.showMessageBar("No internet", true);
        }
    }

    public void onDeleteEventSelected(final long eventId) {
        if (true) {
            //Handle async task
        } else {
            mAppointmentListAdapterHandler.showMessageBar("No internet", true);
        }
    }

    private void showProgressBar(String message) {
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onEventDelete(final boolean isSuccess) {
        hideProgressBar();
        if (isSuccess) {
            mAppointmentListAdapterHandler.showMessageBar("Event deleted", false);
        } else {
            mAppointmentListAdapterHandler.showMessageBar("Failed to delete event", true);
        }
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView scheduledAtTextView;
        public TextView scheduledTillTextView;
        public TextView titleTextView;
        public TextView subTitleTextView;
        public TextView cancelTypeTextView;
        public TextView prepaidTextView;
        public int calendarViewType;
        public CalendarItem calendarItem;

        public CalendarViewHolder(View itemView, int viewType) {
            super(itemView);
            itemView.setOnClickListener(this);
            scheduledAtTextView = (TextView) itemView.findViewById(R.id.text_view_no_appointments);
            scheduledTillTextView = (TextView) itemView.findViewById(R.id.text_view_no_appointments);
            titleTextView = (TextView) itemView.findViewById(R.id.text_view_no_appointments);
            subTitleTextView = (TextView) itemView.findViewById(R.id.text_view_no_appointments);
            cancelTypeTextView = (TextView) itemView.findViewById(R.id.text_view_no_appointments);
            prepaidTextView = (TextView) itemView.findViewById(R.id.text_view_no_appointments);
            itemView.findViewById(R.id.itemName).setOnClickListener(this);
            calendarViewType = viewType;
        }

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.text_view_no_appointments:
                    showBottomSheet(calendarItem);
                    break;
                default:
                    if (calendarViewType == VIEW_TYPE_EVENT) {
                        onEventSelected((EventItem) calendarItem);
                    } else {
                        onAppointmentSelected((AppointmentItem) calendarItem);
                    }
                    break;
            }
        }

        private void setDoctor(String doctorName, String doctorColor) {
            //Handle select docor
        }
    }

    public interface AppointmentListAdapterHandler {
        void addFile(AppointmentItem appointmentItem);

        void callPatient(AppointmentItem appointmentItem);

        void showMessageBar(String message, boolean isErrorMsg);
    }
}
