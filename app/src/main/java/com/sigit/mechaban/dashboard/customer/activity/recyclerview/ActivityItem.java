package com.sigit.mechaban.dashboard.customer.activity.recyclerview;

public class ActivityItem {
    private final String date, title, desc;

    public ActivityItem(String date, String title, String desc) {
        this.date = date;
        this.title = title;
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
