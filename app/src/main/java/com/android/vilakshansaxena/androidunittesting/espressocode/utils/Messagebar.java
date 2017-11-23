package com.android.vilakshansaxena.androidunittesting.espressocode.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vilakshansaxena.androidunittesting.R;

public class Messagebar {

    public static final int MESSAGE_DELAY_MILLIS_LONG = 5000;
    private static final int MESSAGE_DELAY_MILLIS = 2000;
    private static final int ANIMATION_DURATION = 500;
    private View mToolbarMessageView;
    private View mContainerView;
    private Button mMessageButton;
    private TextView mMessageTextView;
    private Resources mResources;
    private boolean mWasPersistent;
    private String mPersistentText;
    private String mPersistentActionText;
    private View.OnClickListener mPersistentOnClickListener;
    private int mPersistentBackgroundColor;
    private int mPersistentTextColor;
    private ImageView mMessageBarDrawable;
    private int mPersistentMessageBarDrawableId;
    private boolean mIsErrorMessage;

    public Messagebar(Activity activity) {
        mMessageTextView = (TextView) activity.findViewById(R.id.toolbar_message_text);
        mMessageButton = (Button) activity.findViewById(R.id.toolbar_message_action);
        mToolbarMessageView = activity.findViewById(R.id.toolbar_message);
        mMessageBarDrawable = (ImageView) activity.findViewById(R.id.toolbar_message_icon);
        mResources = activity.getResources();
    }

    public Messagebar(View view) {
        mMessageTextView = (TextView) view.findViewById(R.id.toolbar_message_text);
        mMessageButton = (Button) view.findViewById(R.id.toolbar_message_action);
        mToolbarMessageView = view.findViewById(R.id.toolbar_message);
        mMessageBarDrawable = (ImageView) view.findViewById(R.id.toolbar_message_icon);
        mContainerView = view.findViewById(R.id.addNotes);
        mResources = view.getResources();
    }

    public static Messagebar newInstance(View view) {
        return new Messagebar(view);
    }

    private boolean isContentSame(String newMessageText, String newButtonText) {
        CharSequence currentMessageText = mMessageTextView.getText();
        CharSequence currentButtonText = mMessageButton.getText();
        return (null != currentMessageText
                && !TextUtils.isEmpty(currentMessageText.toString())
                && currentMessageText.equals(newMessageText))
                && (null != currentButtonText
                && !TextUtils.isEmpty(currentButtonText.toString())
                && currentButtonText.equals(newButtonText));

    }

    private boolean isContentSame(String newMessageText, String newButtonText, boolean isErrorMessage) {
        return isContentSame(newMessageText, newButtonText) && isErrorMessage == mIsErrorMessage;
    }

    public void showWarnMessage(String warnMessage, @Nullable String buttonText,
                                @Nullable View.OnClickListener onClickListener, boolean persistent) {
        int backgroundColor = ResourcesCompat.getColor(mResources, R.color.colorPrimary, null);
        int textColor = ResourcesCompat.getColor(mResources, R.color.colorAccent, null);
        if (isMessageBarShowing() && isContentSame(warnMessage, buttonText, false)) {
            return;
        }
        int messageBarDrawableId = R.drawable.vc_warn_circle;
        createMessage(warnMessage, buttonText, onClickListener, backgroundColor, textColor, persistent, -1,
                messageBarDrawableId);
        mIsErrorMessage = false;
    }

    public void showErrorMessage(final String errorMessage, @Nullable final String buttonText,
                                 @Nullable final View.OnClickListener onClickListener, final boolean persistent) {
        if (isMessageBarShowing() && isContentSame(errorMessage, buttonText, true)) {
            return;
        }
        int backgroundColor = ResourcesCompat.getColor(mResources, R.color.colorPrimary, null);
        int messageBarDrawableId = R.drawable.vc_error_circle;
        int textColor = ResourcesCompat.getColor(mResources, R.color.colorAccent, null);
        createMessage(errorMessage, buttonText, onClickListener, backgroundColor, textColor, persistent,
                -1, messageBarDrawableId);
        mIsErrorMessage = true;
    }

    public void showMessage(final String message, @Nullable final String buttonText,
                            @Nullable final View.OnClickListener onClickListener,
                            final boolean persistent, int messageBarDuration) {
        if (isMessageBarShowing() && isContentSame(message, buttonText)) {
            return;
        }
        int backgroundColor = ResourcesCompat.getColor(mResources, R.color.colorPrimary, null);
        int textColor = ResourcesCompat.getColor(mResources, R.color.colorAccent, null);
        int messageBarDrawableId = R.drawable.vc_success_circle;
        createMessage(message, buttonText, onClickListener, backgroundColor, textColor, persistent,
                messageBarDuration, messageBarDrawableId);
    }

    public void showMessage(final String message, @Nullable final String buttonText,
                            @Nullable final View.OnClickListener onClickListener,
                            final boolean persistent, int messageBarDuration, boolean hideMessageDrawable) {
        if (isMessageBarShowing() && isContentSame(message, buttonText)) {
            return;
        }
        int backgroundColor = ResourcesCompat.getColor(mResources, R.color.colorPrimary, null);
        int textColor = ResourcesCompat.getColor(mResources, R.color.colorAccent, null);
        createMessage(message, buttonText, onClickListener, backgroundColor, textColor, persistent,
                messageBarDuration, -1);
    }

    private void createMessage(final String message, @Nullable final String buttonText,
                               @Nullable final View.OnClickListener onClickListener,
                               int backgroundColor, int textColor, boolean persistent,
                               int messageBarDuration, int messageBarDrawableId) {
        View getParentView = mToolbarMessageView;
        TextView textView = mMessageTextView;
        Button actionView = mMessageButton;
        ImageView messageBarDrawable = mMessageBarDrawable;

        if (null != getParentView) {
            if (!TextUtils.isEmpty(message)) {
                if (persistent) {
                    mPersistentText = message;
                }
                textView.setText(message);
            }

            if (null != onClickListener) {
                if (persistent) {
                    mPersistentActionText = buttonText;
                    mPersistentOnClickListener = onClickListener;
                }
                actionView.setVisibility(View.VISIBLE);
                actionView.setText(buttonText);
                actionView.setOnClickListener(onClickListener);
            } else {
                actionView.setOnClickListener(null);
                actionView.setVisibility(View.GONE);
                actionView.setText("");
            }

            if (0 != backgroundColor) {
                if (persistent) {
                    mPersistentBackgroundColor = backgroundColor;
                }
                getParentView.setBackgroundColor(backgroundColor);
            }

            if (0 != textColor) {
                if (persistent) {
                    mPersistentTextColor = textColor;
                }
                textView.setTextColor(textColor);
            }

            if (null != messageBarDrawable) {
                if (-1 != messageBarDrawableId) {
                    if (persistent) {
                        mPersistentMessageBarDrawableId = messageBarDrawableId;
                    }
                    messageBarDrawable.setImageResource(messageBarDrawableId);
                    messageBarDrawable.setVisibility(View.VISIBLE);
                } else {
                    messageBarDrawable.setVisibility(View.GONE);
                }
            }

            if (persistent) {
                mWasPersistent = true;
            }

            animateViewIn();

            if (!persistent) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateViewOut();
                        if (mWasPersistent) {
                            createMessage(mPersistentText, mPersistentActionText, mPersistentOnClickListener,
                                    mPersistentBackgroundColor, mPersistentTextColor, true, -1,
                                    mPersistentMessageBarDrawableId);
                        }
                    }
                }, (messageBarDuration == -1) ? MESSAGE_DELAY_MILLIS : messageBarDuration);
            }
        }
    }

    private void animateViewIn() {
        final View messageBarView = mToolbarMessageView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.setTranslationY(messageBarView, -messageBarView.getHeight());
            ViewCompat.setAlpha(messageBarView, 0.0f);
            ViewCompat.animate(messageBarView)
                    .translationY(0.0f)
                    .alpha(1.0f)
                    .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            if (mContainerView == null) {
                                messageBarView.setVisibility(View.VISIBLE);
                            }
                        }
                    }).start();
            if (null != mContainerView) {
                ViewCompat.animate(mContainerView)
                        .translationY(0.0f)
                        .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(new ViewPropertyAnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(View view) {
                                messageBarView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(View view) {
                            }
                        }).start();
            }
        }
    }

    private void animateViewOut() {
        final View messageBarView = mToolbarMessageView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.animate(messageBarView)
                    .translationY(-messageBarView.getHeight())
                    .alpha(0.0f)
                    .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            ViewCompat.setTranslationY(messageBarView, -view.getHeight());
                            if (mContainerView == null) {
                                messageBarView.setVisibility(View.GONE);
                            }
                        }
                    }).start();
            if (null != mContainerView) {
                ViewCompat.animate(mContainerView)
                        .translationY(-messageBarView.getHeight())
                        .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(new ViewPropertyAnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(View view) {
                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                messageBarView.setVisibility(View.GONE);
                            }
                        }).start();
            }
        }
    }

    public void hideMessage() {
        mPersistentText = "";
        mPersistentOnClickListener = null;
        mPersistentActionText = "";
        mWasPersistent = false;
        mIsErrorMessage = false;
        animateViewOut();
    }

    public TextView getMessageTextView() {
        return mMessageTextView;
    }

    public boolean isMessageBarShowing() {
        return null != mToolbarMessageView && mToolbarMessageView.getVisibility() == View.VISIBLE;
    }

    public void rollbackMessagebar() {
        if (!TextUtils.isEmpty(mPersistentText) && !TextUtils.isEmpty(mPersistentActionText)) {
            createMessage(mPersistentText, mPersistentActionText, mPersistentOnClickListener,
                    mPersistentBackgroundColor, mPersistentTextColor, true, -1, mPersistentMessageBarDrawableId);
        }
    }
}
