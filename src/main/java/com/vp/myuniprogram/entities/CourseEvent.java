package com.vp.myuniprogram.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course_event")
public class CourseEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "main_course_event_id")
    private MainCourseEvent mainCourseEvent;

    @Column(name = "start_time")
    private String start;

    @Column(name = "end_time")
    private String end;

    @Column(name = "location")
    private String location;

    @Column(name = "info")
    private String info;

    @ManyToOne()
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;
}
