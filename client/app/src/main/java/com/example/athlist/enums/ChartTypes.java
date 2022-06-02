package com.example.athlist.enums;

public enum ChartTypes {
    BAR_CHART(0,"Bar Chart"),PIE_CHART(1,"Pie Chart");


    private final int value;
    private final String name;
    private ChartTypes(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName(){return name; }
}
