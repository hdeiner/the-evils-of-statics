package com.solutionsiq.badcode.microcontroller;

import java.time.LocalDateTime;

public class Microcontroller {
    public static String getDayQuadrant() {
        int hour = LocalDateTime.now().getHour();
        if (hour >= 0 && hour < 6) return "Night";
        if (hour >= 6 && hour < 12) return "Morning";
        if (hour >= 12 && hour < 18) return "Afternoon";
        return "Evening";
    }
}