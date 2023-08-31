package com.vp.myuniprogram.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseEventDTO {
    private String info;
    private String location;
    private Integer mainCourseEventId;
    private String start;
    private String end;
    private Integer timetableId;
}
