package com.solutionsiq.goodcode.microcontroller;

public class Microcontroller {
    public String getDayQuadrant(int hour) throws Exception {
        if ((hour < 0) || (hour > 23)) throw new Exception("Microcontroller given an hour of " + hour + " and that's not possible");
        if (hour >= 0 && hour < 6) return "Night";
        if (hour >= 6 && hour < 12) return "Morning";
        if (hour >= 12 && hour < 18) return "Afternoon";
        return "Evening";
    }
}