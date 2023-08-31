package com.vp.myuniprogram.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimetableDTO {
    private Integer universityMajor;
    private String year;
    private String semester;
    private Integer faculty;
    private String group;
}
