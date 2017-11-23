package com.android.vilakshansaxena.androidunittesting.espressocode.model;

public abstract class CalendarItem {

    public static final int ITEM_TYPE_EVENT = 0;
    public static final int ITEM_TYPE_APPOINTMENT = 1;

    public int id;
    public String scheduledAt;
    public String scheduledTill;
    public String doctorName;
    public int itemType;
    public String doctorColor;
}
