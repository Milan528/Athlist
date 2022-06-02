package com.example.athlist.enums;

public enum StravaConnectionStatus {
    CONNECTED(0,"Connected"), NOT_CONNECTED(1,"Not connected");


    private final int value;
    private final String name;
    private StravaConnectionStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName(){return name; }

}