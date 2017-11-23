package com.android.vilakshansaxena.androidunittesting.espressocode.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.vilakshansaxena.androidunittesting.R;

import java.util.ArrayList;


public class WidgetsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Fragment mFragmentContainer0;
    private Fragment mFragmentContainer1;
    private Fragment mFragmentContainer2;
    private Fragment mFragmentContainer3;
    private Fragment mFragmentContainer4;
    private Fragment mFragmentContainer5;

    //Random promo
    private Fragment mFragmentContainer6;

    private static final int MAX_PRODUCT_COUNT = 5;
    private static final String NO_PROMO_CARD = "no_promo_card";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_widgets, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        refreshWidgets(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpWidgetDataSet();
    }

    private void setUpWidgetDataSet() {
        //Classify widgets as info cards or promo cards
        FragmentManager fragmentManager = getChildFragmentManager();
        mFragmentContainer0 = fragmentManager.findFragmentById(R.id.widget_container_0);
        mFragmentContainer2 = fragmentManager.findFragmentById(R.id.widget_container_2);
        mFragmentContainer3 = fragmentManager.findFragmentById(R.id.widget_container_3);
        mFragmentContainer4 = fragmentManager.findFragmentById(R.id.widget_container_4);
        mFragmentContainer5 = fragmentManager.findFragmentById(R.id.widget_container_5);

        ArrayList<String> infoCardsList = new ArrayList<>();
        ArrayList<String> promoCardsList = new ArrayList<>();
        //1. Check Ray card
        if (isRayInfoCardAvailable()) {
            infoCardsList.add(CalendarWidgetFragment.class.getName());
            promoCardsList.add(NO_PROMO_CARD);
        } else {
            addPromoCardAfterProfileCheck(promoCardsList, CalendarWidgetFragment.class.getName());
        }
        //2. Check Consult card

        //3. Check Reach card

        //4. Check Healthfeed card

        //5. Check Feedback card

        organizeWidgets(getContext(), infoCardsList, promoCardsList);
    }

    private void addPromoCardAfterProfileCheck(ArrayList<String> promoCardsList, String className) {
        // Consult, Reach and Health feed promos are only for users who has profile
        if (true) {
            promoCardsList.add(className);
        } else {
            promoCardsList.add(NO_PROMO_CARD);
        }
    }

    private void organizeWidgets(Context context, ArrayList<String> infoCards, ArrayList<String> promoCards) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        //Profile card

        //Ray card
        if (isCardAvailable(infoCards, CalendarWidgetFragment.class.getName())) {
            if (!(mFragmentContainer0 instanceof CalendarWidgetFragment)) {
                mFragmentContainer0 = Fragment.instantiate(context, CalendarWidgetFragment.class.getName(),
                        null);
                fragmentTransaction.replace(R.id.widget_container_0, mFragmentContainer0);
            }
        } else if (mFragmentContainer0 != null) {
            fragmentTransaction.remove(mFragmentContainer0);
        }

        //Consult card

        //Reach card

        //Health feed card

        //Feedback card

        //Logic for promo cards
        if (isPromoListEmpty(promoCards)) {
            //If promo card list is empty then remove last fragment
            if (mFragmentContainer6 != null) {
                fragmentTransaction.remove(mFragmentContainer6);
            }
        } else {
            //If even one promo card is present then replace with random promo card
            mFragmentContainer6 = Fragment.instantiate(getContext(),
                    promoCards.get(getPromoCard(context, promoCards)), null);
            fragmentTransaction.replace(R.id.widget_container_6, mFragmentContainer6);
        }
        fragmentTransaction.commit();
    }

    @VisibleForTesting
    public CalendarWidgetFragment getRayCard() {
        return (CalendarWidgetFragment) mFragmentContainer0;
    }

    private boolean isCardAvailable(ArrayList<String> infoCards, String targetCardName) {
        boolean isInfoCardPresent = false;
        for (String cardName : infoCards) {
            if (cardName.equals(targetCardName)) {
                isInfoCardPresent = true;
                break;
            }
        }
        return isInfoCardPresent;
    }

    private int getPromoCard(Context context, ArrayList<String> promoCardsList) {
        int cardPosition = -1;
        //Applying this check to differ the scenarios between user is just logged in or not, as
        // accountPreferenceUtils.getPromoCardPosition() will return 0 in both cases where
        // either preference does not exist or user has just seen the promo card at 0 position
        cardPosition = 1;
        while (cardPosition < MAX_PRODUCT_COUNT) {
            cardPosition = (cardPosition + 1) % MAX_PRODUCT_COUNT;
            if (!promoCardsList.get(cardPosition).equals(NO_PROMO_CARD)) {
                //Set card position in preference
                break;
            }
        }
        return cardPosition;
    }

    private boolean isPromoListEmpty(ArrayList<String> promoCardsList) {
        boolean isPromoEmpty = true;
        for (String promoCard : promoCardsList) {
            if (!promoCard.equals(NO_PROMO_CARD)) {
                isPromoEmpty = false;
            }
        }
        return isPromoEmpty;
    }

    public void refreshWidgets(boolean isSwipeToRefresh) {
        BaseWidgetFragment baseWidgetFragment;
        if (mFragmentContainer0 != null) {
            baseWidgetFragment = (BaseWidgetFragment) mFragmentContainer0;
            baseWidgetFragment.onRefresh(isSwipeToRefresh);
        }

        refreshProfileWidget(isSwipeToRefresh);
        refreshConsultWidget(isSwipeToRefresh);

        if (mFragmentContainer3 != null) {
            baseWidgetFragment = (BaseWidgetFragment) mFragmentContainer3;
            baseWidgetFragment.onRefresh(isSwipeToRefresh);
        }

        if (mFragmentContainer4 != null) {
            baseWidgetFragment = (BaseWidgetFragment) mFragmentContainer4;
            baseWidgetFragment.onRefresh(isSwipeToRefresh);
        }

        if (mFragmentContainer5 != null) {
            baseWidgetFragment = (BaseWidgetFragment) mFragmentContainer5;
            baseWidgetFragment.onRefresh(isSwipeToRefresh);
        }

        if (mFragmentContainer6 != null) {
            baseWidgetFragment = (BaseWidgetFragment) mFragmentContainer6;
            baseWidgetFragment.onRefresh(isSwipeToRefresh);
        }
    }

    private boolean isRayInfoCardAvailable() {
        return true;
    }

    private boolean isConsultInfoCardAvailable() {
        return false;
    }

    private boolean isReachInfoCardAvailable() {
        return false;
    }

    private boolean isHealthfeedInfoCardAvailable() {
        return false;
    }

    private boolean isFeedbackInfoCardAvailable() {
        return false;
    }

    public void refreshConsultWidget(boolean isSwipeToRefresh) {
        if (mFragmentContainer2 != null) {
            BaseWidgetFragment baseWidgetFragment = (BaseWidgetFragment) mFragmentContainer2;
            baseWidgetFragment.onRefresh(isSwipeToRefresh);
        }
    }

    public void refreshProfileWidget(boolean isSwipeToRefresh) {
        if (mFragmentContainer1 != null) {
            BaseWidgetFragment baseWidgetFragment = (BaseWidgetFragment) mFragmentContainer1;
            baseWidgetFragment.onRefresh(isSwipeToRefresh);
        }
    }
}
