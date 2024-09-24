package com.sigit.mechaban.onboarding;

public class ScreenItem {
    private final String title, description;
    private final int img;

    public ScreenItem(String title, String description, int img) {
        this.title = title;
        this.description = description;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImg() {
        return img;
    }

}
