package com.clstephenson.mywgutracker.data;

public enum AssessmentType {
    OBJECTIVE("Objective Assessment"),
    PERFORMANCE("Performance Assessment");

    private final String friendlyName;

    private AssessmentType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}