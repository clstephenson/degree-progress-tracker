package com.clstephenson.mywgutracker.data;

public enum CourseStatus {
    NOT_STARTED("Not Started"),
    STARTED("Started"),
    COMPLETED("Completed");

    private final String friendlyName;

    private CourseStatus(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}