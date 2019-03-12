package com.clstephenson.mywgutracker.data;

public enum AssessmentType {
    OBJECTIVE("Objective Assessment"),
    PERFORMANCE("Performance Assessment");

    private final String friendlyName;

    AssessmentType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}