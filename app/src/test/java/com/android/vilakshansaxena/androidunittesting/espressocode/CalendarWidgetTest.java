//package com.android.vilakshansaxena.androidunittesting.espressocode;
//import android.Manifest;
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.preference.PreferenceManager;
//import android.provider.Settings;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.contrib.RecyclerViewActions;
//import android.support.test.espresso.intent.Intents;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.test.uiautomator.UiDevice;
//import android.support.test.uiautomator.UiObject;
//import android.support.test.uiautomator.UiObjectNotFoundException;
//import android.support.test.uiautomator.UiSelector;
//import android.support.v4.app.ActivityCompat;
//
//import com.android.vilakshansaxena.androidunittesting.espressocode.activity.HomeActivity;
//import com.android.vilakshansaxena.androidunittesting.espressocode.fragments.CalendarWidgetFragment;
//import com.practo.droid.DoctorApplication;
//import com.practo.droid.R;
//import com.practo.droid.account.utils.AccountPreferenceUtils;
//import com.practo.droid.common.utils.ConnectionUtils;
//import com.practo.droid.common.utils.ItemViewActions;
//import com.practo.droid.common.utils.PermissionUtils;
//import com.practo.droid.consult.utils.ConsultPreferenceUtils;
//import com.practo.droid.home.HomeActivity;
//import com.practo.droid.profile.utils.ProfilePreferenceUtils;
//import com.practo.droid.ray.calendar.AppointmentItem;
//import com.practo.droid.ray.calendar.CalendarItem;
//import com.practo.droid.ray.calendar.EventItem;
//import com.practo.droid.ray.home.RayHomeActivity;
//import com.practo.droid.ray.invoices.TreatmentSearchActivity;
//import com.practo.droid.ray.misc.PreferenceUtils;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.intent.Intents.intended;
//import static android.support.test.espresso.intent.Intents.intending;
//import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
//import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
//import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
//import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.allOf;
//
//
//@RunWith(AndroidJUnit4.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class CalendarWidgetTest {
//
//    @Rule
//    public ActivityTestRule<HomeActivity> activityTestRule = new ActivityTestRule<>(HomeActivity.class, false, false);
//    private Instrumentation mInstrumentation;
//    private boolean mIsLoggedIn;
//    private boolean mIsRayEnabled;
//    private boolean mHasRayFreeSubscription;
//    private String mProfileStatus;
//    private boolean mIsConsultEnabled;
//    private boolean mIsReachEnabled;
//    private boolean mIsHealthfeedEnabled;
//    private Set<String> mPracticeModules;
//    private HomeActivity mActivity;
//    private UiDevice mDevice;
//    private String mPatientLabel;
//    private CalendarWidgetFragment mCalendarWidgetFragment;
//    private AccountPreferenceUtils mAccountPreferenceUtils;
//    private List<CalendarItem> mTestCalendarItemList;
//    private static final String GROUP_CONCAT_DELIMITER_REGX = "\\`\\<\\|\\~";
//    private static final String SUMMARY_TAB_NAME = "Summary";
//    private static final String STATUS_CANCELLED = "Cancelled";
//    private static final String STATUS_SCHEDULED = "Scheduled";
//    private static final String APPOINTMENT_SOURCE_FABRIC = "Fabric";
//    private static final String CALL_PERMISSION = Manifest.permission.CALL_PHONE;
//    private static final String FILE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
//    private static final String PHONE_NUMBER = "1234567890";
//    private static final Uri INTENT_DATA_PHONE_NUMBER = Uri.parse("tel:" + PHONE_NUMBER);
//    private static final String EMAIL = "abc@ab.com";
//
//    @Before
//    public void setUp() {
//        mInstrumentation = InstrumentationRegistry.getInstrumentation();
//        mAccountPreferenceUtils = new AccountPreferenceUtils(mInstrumentation.getTargetContext());
//        mTestCalendarItemList = new ArrayList<>();
//        mDevice = UiDevice.getInstance(mInstrumentation);
//        getIsLoggedInPreference();
//        setIsLoggedInPreference(true);
//        getRayEnabledPreference();
//        setRayEnabledPreference(true);
//        getOtherProductsEnabledPreference();
//        setOtherProductsTestEnabledPreference();
//        getPracticeModulesPreference();
//        setPracticeModulesPreference(getTemporaryPracticeModules());
//        launchActivity();
//        mActivity = activityTestRule.getActivity();
//        mCalendarWidgetFragment = getRayCardInstance();
//    }
//
//    @Test
//    public void a_testRayContainerVisibility() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        onView(withId(R.id.widget_container_0)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void b_testRayCardActionButtonsLayout() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_add_appointment)).check(matches(withText("Add Appointment")));
//    }
//
//    @Test
//    public void c_testRayCardViewAllButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).perform(click());
//        onView(withId(R.id.toolbar_title)).check(matches(withText("Calendar")));
//    }
//
//    @Test
//    public void d_testRayCardAppointmentSelect() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", "1234567890", "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                click()));
//       onView(withText("Appointment Details")).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void e_testCallButtonForScheduledAppointment() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Call Patient")).perform(click());
//        onView(withId(R.id.edit_text_phone_number)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void f_testCallButtonForCancelledAppointment() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Call Patient")).perform(click());
//        onView(withId(R.id.edit_text_phone_number)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void g_testAddFilesButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Add file")).perform(click());
//        if (ActivityCompat.checkSelfPermission(mActivity, FILE_PERMISSION)
//                    != PackageManager.PERMISSION_GRANTED) {
//            allowPermissionsIfNeeded();
//        }
//        onView(withText("Folders")).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void h_testFollowupForScheduledAppointment() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        String testTreatmentId = "1" + GROUP_CONCAT_DELIMITER_REGX + "1" +
//                                         GROUP_CONCAT_DELIMITER_REGX + "2";
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", testTreatmentId, "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Followup")).perform(click());
//       onView(withText("Add Appointment")).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void i_testFollowupForCancelledAppointment() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Followup")).perform(click());
//        onView(withText("Add Appointment")).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void j_testAddBillWithValidPatientId() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 123);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        Intents.init();
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent());
//        intending(hasComponent(hasClassName(TreatmentSearchActivity.class.getName()))).respondWith(result);
//        onView(withText("Add Bill")).perform(click());
//        intended(hasComponent(hasClassName(TreatmentSearchActivity.class.getName())));
//        Intents.release();
//    }
//
//    @Test
//    public void k_testAddBillWithInValidPatientId() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Add Bill")).perform(click());
//        //Don't have checked for message bar as some times expresso is not able to judge it
//        onView(withId(R.id.view_all_appointments_button)).check(matches(withText("View All")));
//    }
//
//    @Test
//    public void l_testRayCardAddAppointmentClick() {
//        getPatientLabel();
//        setPatientLabel("Patient");
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        onView(withId(R.id.button_add_appointment)).perform(click());
//        onView(withId(R.id.button_add_appointment_with_new_patient)).check(matches(
//                withText("Add appointment for new Patient")));
//        setPatientLabel(mPatientLabel);
//    }
//
//    @Test
//    public void m_testAddPrescriptionButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Prescribe")).perform(click());
//        onView(withId(R.id.edit_text_search)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void n_testEditAppointmentButtonForEmptyState() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Edit")).perform(click());
//        onView(withText("Edit Appointment")).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void o_testEditAppointmentButtonForCheckInState() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "Check-In", "", "", PHONE_NUMBER, "", "", "", 0);
//        mCalendarWidgetFragment.getAdapter().setSelectedDoctorId(0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Edit")).perform(click());
//        //Don't have checked for message bar as some times expresso is not able to judge it
//        onView(withId(R.id.view_all_appointments_button)).check(matches(withText("View All")));
//    }
//
//    @Test
//    public void p_testCancelAppointmentBackButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Cancel")).perform(click());
//        onView(withText("Back")).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void q_testCancelAppointmentButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Cancel")).perform(click());
//        onView(withText("Confirm")).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void r_testNoShowBackButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, "", PHONE_NUMBER, "", "", APPOINTMENT_SOURCE_FABRIC, 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("No Show")).perform(click());
//        onView(withText("Back")).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void s_testNoShowButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", APPOINTMENT_SOURCE_FABRIC, 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("No Show")).perform(click());
//        onView(withText("Confirm")).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void t_testScheduleBackButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Schedule")).perform(click());
//        onView(withText("Back")).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void u_testScheduleButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Schedule")).perform(click());
//        onView(withText("OK")).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void v_testRayCardEventSelect() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
//                click()));
//        onView(withId(R.id.event_name_tv)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void w_testEditEventButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(2,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Edit Event")).perform(click());
//        if(new ConnectionUtils(mInstrumentation.getTargetContext()).isNetConnected()){
//            onView(withId(R.id.toolbar_title)).check(matches(withText("Block Calendar")));
//        }
//    }
//
//    @Test
//    public void x_testDeleteEventBackButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(2,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Delete Event")).perform(click());
//        onView(withText("Cancel")).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void y_testDeleteEventSuccess() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mCalendarWidgetFragment.getAdapter().onEventDelete(true);
//            }
//        });
//        //Don't have checked for message bar as some times expresso is not able to judge it
//        onView(withId(R.id.view_all_appointments_button)).check(matches(withText("View All")));
//    }
//
//    @Test
//    public void z_testDeleteEventFailure() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_CANCELLED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mCalendarWidgetFragment.getAdapter().onEventDelete(false);
//            }
//        });
//        //Don't have checked for message bar as some times expresso is not able to judge it
//        onView(withId(R.id.view_all_appointments_button)).check(matches(withText("View All")));
//    }
//
//    @Test
//    public void za_testDeleteEventDeleteButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(2,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Delete Event")).perform(click());
//        onView(withText("Delete")).perform(click());
//        onView(withId(R.id.view_all_appointments_button)).check(matches(withText("View All")));
//    }
//
//    @Test
//    public void zb_testEmptyView() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenDataForEmptyView();
//        onView(withId(R.id.text_view_no_appointments)).check(matches(withText("No upcoming appointments for today")));
//    }
//
//    @Test
//    public void zc_testPatientSmsCheckedView() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Cancel")).perform(click());
//        getRayFreeSubscriptionPreference();
//        setHasRayFreeSubscriptionPreference(false);
//        onView(withId(R.id.patient_sms)).perform(click());
//        onView(withId(R.id.patient_sms)).check(matches(isChecked()));
//        setHasRayFreeSubscriptionPreference(mHasRayFreeSubscription);
//    }
//
//    @Test
//    public void zd_testDoctorSmsCheckedView() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, "", PHONE_NUMBER, "", "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Cancel")).perform(click());
//        getRayFreeSubscriptionPreference();
//        setHasRayFreeSubscriptionPreference(false);
//        onView(withId(R.id.doctor_sms)).perform(click());
//        onView(withId(R.id.doctor_sms)).check(matches(isChecked()));
//        setHasRayFreeSubscriptionPreference(mHasRayFreeSubscription);
//    }
//
//    @Test
//    public void ze_testPatientEmailCheckedView() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, EMAIL, PHONE_NUMBER, EMAIL, "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Cancel")).perform(click());
//        getRayFreeSubscriptionPreference();
//        setHasRayFreeSubscriptionPreference(false);
//        onView(withId(R.id.patient_email)).perform(click());
//        onView(withId(R.id.patient_email)).check(matches(isChecked()));
//        setHasRayFreeSubscriptionPreference(mHasRayFreeSubscription);
//    }
//
//    @Test
//    public void zf_testDoctorEmailCheckedView() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, EMAIL, PHONE_NUMBER, EMAIL, "", "", 0);
//        onView(withId(R.id.recycler_view_event_appointments)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
//                ItemViewActions.clickChildViewWithId(R.id.image_button_menu)));
//        onView(withText("Cancel")).perform(click());
//        getRayFreeSubscriptionPreference();
//        setHasRayFreeSubscriptionPreference(false);
//        onView(withId(R.id.doctor_email)).perform(click());
//        onView(withId(R.id.doctor_email)).check(matches(isChecked()));
//        setHasRayFreeSubscriptionPreference(mHasRayFreeSubscription);
//    }
//
//    @Test
//    public void zg_testPullToRefreshOperation() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, EMAIL, PHONE_NUMBER, EMAIL, "", "", 0);
//        mInstrumentation.runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mCalendarWidgetFragment.onRefresh(true);
//            }
//        });
//        onView(withId(R.id.view_all_appointments_button)).check(matches(withText("View All")));
//    }
//
//    @Test
//    public void zh_testPermissionRevokedErrorMessage() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, EMAIL, PHONE_NUMBER, EMAIL, "", "", 0);
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mCalendarWidgetFragment.showPermissionErrorMessage("Test permission revoked");
//            }
//        });
//        onView(withText("Test permission revoked")).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void zi_testRayCardViewAllButtonResult() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        Intents.init();
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent());
//        intending(hasComponent(hasClassName(RayHomeActivity.class.getName()))).respondWith(result);
//        onView(withId(R.id.view_all_appointments_button)).perform(click());
//        intended(hasComponent(hasClassName(RayHomeActivity.class.getName())));
//        Intents.release();
//    }
//
//    @Test
//    public void zj_testRequestPermissionForAddFile() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, "", PHONE_NUMBER, "", "", "", 0);
//        mCalendarWidgetFragment.setSelectedAppointment(createTestAppointmentInstance());
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mCalendarWidgetFragment.onRequestPermissionsResult(2, new String[]{"Test Permission"},
//                        new int[]{PackageManager.PERMISSION_GRANTED});
//            }
//        });
//        onView(withText("Folders")).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void zk_testPermissionRevokedMessageButton() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", PHONE_NUMBER, EMAIL, PHONE_NUMBER, EMAIL, "", "", 0);
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mCalendarWidgetFragment.showPermissionErrorMessage("Test permission revoked");
//            }
//        });
//        Intents.init();
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent());
//        intending(allOf(hasAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS))).respondWith(result);
//        onView(withId(R.id.toolbar_message_action)).perform(click());
//        intended(allOf(hasAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)));
//        Intents.release();
//    }
//
//    @Test
//    public void zl_testRequestPermissionForCallPatient() {
//        onView(withText(SUMMARY_TAB_NAME)).perform(click());
//        setScreenData(STATUS_SCHEDULED, "", "", "", "", PHONE_NUMBER, "", "", "", 0);
//        mCalendarWidgetFragment.setSelectedAppointment(createTestAppointmentInstance());
//        Intents.init();
//        if (PermissionUtils.requestPermission(mCalendarWidgetFragment, CALL_PERMISSION)) {
//            InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//                @Override
//                public void run() {
//                    mCalendarWidgetFragment.onRequestPermissionsResult(4, new String[]{"Test Permission"},
//                            new int[]{PackageManager.PERMISSION_GRANTED});
//                }
//            });
//        } else {
//
//            allowPermissionsIfNeeded();
//        }
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent());
//        intending(allOf(hasAction(Intent.ACTION_CALL), hasData(INTENT_DATA_PHONE_NUMBER))).respondWith(result);
//        intended(allOf(hasAction(Intent.ACTION_CALL), hasData(INTENT_DATA_PHONE_NUMBER)));
//        Intents.release();
//        endCall();
//    }
//
//    @After
//    public void tearDown() {
//        setIsLoggedInPreference(mIsLoggedIn);
//        setRayEnabledPreference(mIsRayEnabled);
//        setOtherProductsEnabledPreference();
//        setPracticeModulesPreference(mPracticeModules);
//        finishActivity();
//    }
//
//    private void launchActivity() {
//        activityTestRule.launchActivity(new Intent(mInstrumentation.getTargetContext(), HomeActivity.class));
//    }
//
//    private void finishActivity() {
//        activityTestRule.getActivity().finish();
//    }
//
//    private void getIsLoggedInPreference() {
//        mIsLoggedIn = mAccountPreferenceUtils.getBooleanPrefs("is_logged_in");
//    }
//
//    private void setIsLoggedInPreference(boolean isLoggedIn) {
//        mAccountPreferenceUtils.set("is_logged_in", isLoggedIn);
//        mAccountPreferenceUtils.set("is_remote_login", isLoggedIn);
//    }
//
//    private void getRayEnabledPreference() {
//        mIsRayEnabled = mAccountPreferenceUtils.getBooleanPrefs("service_enabled_ray");
//    }
//
//    private void setRayEnabledPreference(boolean isRayEnabled) {
//        mAccountPreferenceUtils.set("service_enabled_ray", isRayEnabled);
//    }
//
//    private void getOtherProductsEnabledPreference() {
//        mProfileStatus = new ProfilePreferenceUtils(mInstrumentation.getTargetContext()).getStringPrefs(
//                "doctor_publish_status");
//        mIsConsultEnabled = new ConsultPreferenceUtils
//                                    (mInstrumentation.getTargetContext()).getBooleanPrefs(
//                "is_consult_consent_given");
//        mIsReachEnabled = mAccountPreferenceUtils.getBooleanPrefs("service_enabled_reach");
//        mIsHealthfeedEnabled = mAccountPreferenceUtils.getBooleanPrefs("service_enabled_healthfeed");
//
//    }
//
//    private void setOtherProductsTestEnabledPreference() {
//        new ProfilePreferenceUtils(mInstrumentation.getTargetContext()).set(
//                "doctor_publish_status", "1");
//        new ConsultPreferenceUtils
//                (mInstrumentation.getTargetContext()).set("is_consult_consent_given",
//                false);
//        mAccountPreferenceUtils.set("service_enabled_reach", false);
//        mAccountPreferenceUtils.set("service_enabled_healthfeed", false);
//    }
//
//    private void setOtherProductsEnabledPreference() {
//        new ProfilePreferenceUtils(mInstrumentation.getTargetContext()).set(
//                "doctor_publish_status", mProfileStatus);
//        new ConsultPreferenceUtils
//                (mInstrumentation.getTargetContext()).set("is_consult_consent_given",
//                mIsConsultEnabled);
//        mAccountPreferenceUtils.set("service_enabled_reach", mIsReachEnabled);
//        mAccountPreferenceUtils.set("service_enabled_healthfeed", mIsHealthfeedEnabled);
//    }
//
//    private void getPracticeModulesPreference() {
//        mPracticeModules = PreferenceManager.getDefaultSharedPreferences(
//                mInstrumentation.getTargetContext()).getStringSet(
//                "current_practice_modules", new HashSet<String>());
//    }
//
//    private void setPracticeModulesPreference(Object targetSet) {
//        PreferenceManager.getDefaultSharedPreferences(mInstrumentation.getTargetContext())
//                .edit().putStringSet("current_practice_modules", (HashSet) targetSet)
//                .commit();
//    }
//
//    private void getRayFreeSubscriptionPreference() {
//        mHasRayFreeSubscription = PreferenceUtils.getBooleanPrefs(mInstrumentation.getTargetContext(),
//                "current_has_free");
//    }
//
//    private void setHasRayFreeSubscriptionPreference(boolean hasRayFreeSubscription) {
//        PreferenceUtils.set(mInstrumentation.getTargetContext(), "current_has_free",
//                hasRayFreeSubscription);
//    }
//
//    private HashSet<String> getTemporaryPracticeModules() {
//        HashSet<String> temporarySet = new HashSet<>();
//        temporarySet.add("Billing");
//        temporarySet.add("GenericEMR");
//        return temporarySet;
//    }
//
//    private void getPatientLabel(){
//        mPatientLabel = PreferenceUtils.getStringPrefs(mInstrumentation.getTargetContext(),
//                "current_patient_label", "");
//    }
//
//    private void setPatientLabel(String patientLabel){
//        PreferenceUtils.set(mInstrumentation.getTargetContext(), "current_patient_label", patientLabel);
//    }
//
//    private void createTestDataSet(String appointmentStatus, String cancelledReason, String state,
//                                   String patientMobile, String patientEmail, String doctorMobile,
//                                   String doctorEmail, String treatmentId, String source, int patientPractoId) {
//        //Create all day event calendar item
//        EventItem allDayEventItem = new EventItem();
//        allDayEventItem.id = 0;
//        allDayEventItem.scheduledAt = "2016-09-10 00:00:00";
//        allDayEventItem.scheduledTill = "2016-09-12 23:59:59";
//        allDayEventItem.doctorName = "Test Doctor";
//        allDayEventItem.itemType = 0;
//        allDayEventItem.doctorColor = "";
//        allDayEventItem.eventPractoId = 0;
//        allDayEventItem.title = "Test All Day Event";
//        allDayEventItem.allDayEvent = true;
//        allDayEventItem.doctorAvailable = false;
//        mTestCalendarItemList.add(allDayEventItem);
//
//        //Create appointment calendar item
//        AppointmentItem appointmentItem = new AppointmentItem();
//        appointmentItem.id = 0;
//        Calendar targetDate = Calendar.getInstance(DoctorApplication.getInstance().getLocale());
//        targetDate.add(Calendar.HOUR_OF_DAY, 1);
//        appointmentItem.scheduledAt = getDateString(targetDate.getTime());
//        targetDate.add(Calendar.HOUR_OF_DAY, 2);
//        appointmentItem.scheduledTill = getDateString(targetDate.getTime());
//        appointmentItem.doctorName = "Test Doctor";
//        appointmentItem.itemType = 1;
//        appointmentItem.doctorColor = "#F0FF00,#000000";
//        appointmentItem.appointmentPractoId = 0;
//        appointmentItem.status = appointmentStatus;
//        appointmentItem.cancelledReason = cancelledReason;
//        appointmentItem.state = state;
//        appointmentItem.patientName = "Test Patient";
//        appointmentItem.patientPrimaryMobile = patientMobile;
//        appointmentItem.patientId = 0;
//        appointmentItem.patientPractoId = patientPractoId;
//        appointmentItem.patientPrimaryEmail = patientEmail;
//        appointmentItem.doctorMobile = doctorMobile;
//        appointmentItem.doctorEmail = doctorEmail;
//        appointmentItem.treatmentId = treatmentId;
//        appointmentItem.treatmentName = "Bleaching" + GROUP_CONCAT_DELIMITER_REGX + "Bleaching" +
//                                                GROUP_CONCAT_DELIMITER_REGX + "Cephalogram";
//        appointmentItem.treatmentQuantity = "1" + GROUP_CONCAT_DELIMITER_REGX + "1" +
//                                                    GROUP_CONCAT_DELIMITER_REGX + "1";
//        appointmentItem.source = source;
//        mTestCalendarItemList.add(appointmentItem);
//
//        //Create partial event calendar item
//        EventItem partialEventItem = new EventItem();
//        partialEventItem.id = 0;
//        partialEventItem.scheduledAt = "2016-09-10 00:00:00";
//        partialEventItem.scheduledTill = "2016-09-11 23:59:59";
//        partialEventItem.doctorName = "Test Doctor";
//        partialEventItem.itemType = 0;
//        partialEventItem.doctorColor = "";
//        partialEventItem.eventPractoId = 0;
//        partialEventItem.title = "Test Partial Day Event";
//        partialEventItem.allDayEvent = false;
//        partialEventItem.doctorAvailable = true;
//        mTestCalendarItemList.add(partialEventItem);
//    }
//
//    private AppointmentItem createTestAppointmentInstance() {
//        AppointmentItem appointmentItem = new AppointmentItem();
//        appointmentItem.id = 0;
//        Calendar targetDate = Calendar.getInstance(DoctorApplication.getInstance().getLocale());
//        targetDate.add(Calendar.HOUR_OF_DAY, 1);
//        appointmentItem.scheduledAt = getDateString(targetDate.getTime());
//        targetDate.add(Calendar.HOUR_OF_DAY, 2);
//        appointmentItem.scheduledTill = getDateString(targetDate.getTime());
//        appointmentItem.doctorName = "Test Doctor";
//        appointmentItem.itemType = 1;
//        appointmentItem.doctorColor = "#F0FF00,#000000";
//        appointmentItem.appointmentPractoId = 0;
//        appointmentItem.status = STATUS_SCHEDULED;
//        appointmentItem.cancelledReason = "";
//        appointmentItem.state = "";
//        appointmentItem.patientName = "Test Patient";
//        appointmentItem.patientPrimaryMobile = PHONE_NUMBER;
//        appointmentItem.patientId = 0;
//        appointmentItem.patientPractoId = 0;
//        appointmentItem.patientPrimaryEmail = "";
//        appointmentItem.doctorMobile = "";
//        appointmentItem.doctorEmail = "";
//        appointmentItem.treatmentId = "";
//        appointmentItem.treatmentName = "";
//        appointmentItem.treatmentQuantity = "";
//        appointmentItem.source = "";
//
//        return appointmentItem;
//    }
//
//    private CalendarWidgetFragment getRayCardInstance() {
//        return mActivity.getWidgetsFragment().getRayCard();
//    }
//
//    private void setScreenDataForEmptyView() {
//        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mCalendarWidgetFragment.changeDataSet(mTestCalendarItemList);
//            }
//        });
//    }
//
//    private void setScreenData(final String appointmentStatus, final String cancelledReason, final String state,
//                               final String patientMobile, final String patientEmail, final String doctorMobile,
//                               final String doctorEmail, final String treatmentId, final String source,
//                               final int patientPractoId) {
//        createTestDataSet(appointmentStatus, cancelledReason, state,
//                patientMobile, patientEmail, doctorMobile, doctorEmail, treatmentId, source, patientPractoId);
//        mInstrumentation.runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mCalendarWidgetFragment.changeDataSet(mTestCalendarItemList);
//            }
//        });
//    }
//
//    private String getDateString(Date date) {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return dateFormat.format(date);
//    }
//
//    private void allowPermissionsIfNeeded() {
//        mDevice = UiDevice.getInstance(mInstrumentation);
//        UiObject allowPermissions = mDevice.findObject(new UiSelector().text("Allow"));
//        if (allowPermissions.exists()) {
//            try {
//                allowPermissions.click();
//            } catch (UiObjectNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void endCall() {
//        UiObject endButton = mDevice.findObject(new UiSelector().resourceId(
//                "com.android.dialer:id/floating_end_call_action_button"));
//        if (endButton.exists()) {
//            try {
//                endButton.click();
//            } catch (UiObjectNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
