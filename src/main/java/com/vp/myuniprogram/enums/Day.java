package com.vp.myuniprogram.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Day {
    MONDAY("понеделник"),
    TUESDAY("вторник"),
    WEDNESDAY("сряда"),
    THURSDAY("четвъртък"),
    FRIDAY("петък");

    private String dayName;

    public static Day valueOfDayName(String dayName) {
        for (Day e : values()) {
            if (e.dayName.equals(dayName)) {
                return e;
            }
        }
        return null;
    }
}
