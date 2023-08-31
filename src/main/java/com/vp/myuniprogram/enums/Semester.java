package com.vp.myuniprogram.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Semester {
    SUMMER("летен"),
    WINTER("зимен");

    private String value;
}
