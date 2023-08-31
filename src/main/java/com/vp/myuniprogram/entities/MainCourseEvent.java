package com.vp.myuniprogram.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "main_course_event")
public class  MainCourseEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "start_time")
    private String start;

    @Column(name = "end_time")
    private String end;

    @Column(name = "location")
    private String location;

    @Column(name = "day")
    private String day;

    @ManyToOne()
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne()
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @OneToMany(mappedBy="mainCourseEvent")
    @JsonIgnore
    private Set<CourseEvent> courseEventSet = new HashSet();
}
