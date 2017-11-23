package com.android.vilakshansaxena.androidunittesting.espressocode.model;

public class EventItem extends CalendarItem implements Cloneable {
    public long eventPractoId;
    public String title;
    public boolean allDayEvent;
    public boolean doctorAvailable;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
