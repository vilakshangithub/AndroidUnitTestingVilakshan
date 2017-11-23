package com.android.vilakshansaxena.androidunittesting.espressocode.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.vilakshansaxena.androidunittesting.R;
import com.android.vilakshansaxena.androidunittesting.espressocode.fragments.WidgetsFragment;

public class HomeActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener {

    public static final String BUNDLE_TAB_TYPE = "bundle_tab_type";
    private WidgetsFragment mWidgetsFragment;
    private int mRetryCount;
    private static final int MAX_RETRY_COUNT = 3;

    /**
     * Current selected tab type
     */
    public static final class TabType {
        /**
         * Tab type is summary screen
         */
        public static final int SUMMARY = 1;

        /**
         * Tab type is apps screen
         */
        public static final int APPS = 0;

        private TabType() {
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(starter);
    }

    public static void startAsNewTask(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * Start with the summary tab
     *
     * @param context Context
     */
    public static void startSummary(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle extras = new Bundle();
        extras.putInt(BUNDLE_TAB_TYPE, TabType.SUMMARY);
        starter.putExtras(extras);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewPager viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        setupViewPager(viewPager, true);
        Bundle extras = getIntent().getExtras();
        int defaultTab = TabType.APPS;

        if (null != extras) {
            defaultTab = extras.getInt(BUNDLE_TAB_TYPE, TabType.APPS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager, boolean isIndianUser) {
        //Handle set up view pager with adapter
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(final int position) {
        if (position == 0) {
            //Handle position 0
        } else if (position == 1) {
            if (mWidgetsFragment != null) {
                mWidgetsFragment.refreshWidgets(false);
            }
        }
    }

    @VisibleForTesting
    public WidgetsFragment getWidgetsFragment() {
        if (mWidgetsFragment == null) {
            mWidgetsFragment = new WidgetsFragment();
        }
        return mWidgetsFragment;
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
    }
}
