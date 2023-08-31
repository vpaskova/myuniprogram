package com.vp.myuniprogram.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainCourseEventDTO {
    private String location;
    private Integer courseId;
    private String start;
    private String end;
    private String day;
    private Integer timetableId;
    private Integer teacherId;
}
