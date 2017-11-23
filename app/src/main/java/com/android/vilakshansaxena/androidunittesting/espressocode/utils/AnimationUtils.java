package com.android.vilakshansaxena.androidunittesting.espressocode.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

public class AnimationUtils extends android.view.animation.AnimationUtils {
    public static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

    private static final long REVEAL_ANIMATION_DURATION = 350;
    public static final String EXTRA_BUNDLE_XY = "extra_bundle_xy";
    private int mAppBarHeight;
    private Activity mActivity;

    public AnimationUtils(Activity activity) {
        mActivity = activity;
    }

    public static AnimationUtils newInstance(Activity activity) {
        return new AnimationUtils(activity);
    }

    /**
     * It returns a new Bundle() with EXTRA_BUNDLE_XY
     *
     * @param view
     * @return
     */
    public Bundle getCenterXYBundle(View view) {
        Bundle bundle = new Bundle();
        int[] center = new int[2];
        center[0] = (int) view.getX() + view.getWidth() / 2;
        center[1] = (int) view.getY() + view.getHeight() / 2 + mAppBarHeight;
        bundle.putIntArray(EXTRA_BUNDLE_XY, center);
        return bundle;
    }

    /**
     * Returns true if reveal animation is enabled and OS_Version is 21 and above
     *
     * @return
     */
    public boolean shouldReveal() {
        boolean isRevealEnabled = false;
        return isRevealEnabled && DeviceUtils.hasLollipop() && mActivity.getIntent().hasExtra(EXTRA_BUNDLE_XY);
    }

    public void initEntryCircularRevealActivity(final View rootLayout, final OnRevealAnimationListener
                                                                               onRevealAnimationListener) {
        ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    entryCircularRevealActivity(rootLayout, onRevealAnimationListener);
                    if (DeviceUtils.hasJellyBean()) {
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
    }

    private void entryCircularRevealActivity(View rootLayout, final OnRevealAnimationListener onRevealAnimationListener) {
        int[] cxy = mActivity.getIntent().getIntArrayExtra(EXTRA_BUNDLE_XY);
        if (cxy != null && DeviceUtils.hasLollipop()) {
            float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, cxy[0], cxy[1], 0, finalRadius);
            circularReveal.setDuration(REVEAL_ANIMATION_DURATION);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    if (onRevealAnimationListener != null) {
                        onRevealAnimationListener.onRevealAnimationEnd();
                    }
                }
            });
            circularReveal.start();
        }
    }

    public void initExitCircularRevealActivity(final View view) {
        int[] cxy = mActivity.getIntent().getIntArrayExtra(EXTRA_BUNDLE_XY);
        float initialRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cxy[0], cxy[1], initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
                mActivity.finish();
            }
        });
        anim.start();
    }

    public void calculateAppbarHeight(final View appbarView) {
        ViewTreeObserver viewTreeObserver = appbarView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mAppBarHeight = appbarView.getHeight();
                    if (DeviceUtils.hasJellyBean()) {
                        appbarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        appbarView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
    }

    public interface OnRevealAnimationListener {
        void onRevealAnimationEnd();
    }
}
