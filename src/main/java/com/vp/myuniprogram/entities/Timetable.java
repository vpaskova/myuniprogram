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
@Table(name = "timetable")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "university_major_id")
    private UniversityMajor universityMajor;

    @Column(name = "year")
    private String year;

    @Column(name = "semester")
    private String semester;

    @Column(name = "group_name")
    private String group;

    @ManyToOne()
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy="timetable")
    @JsonIgnore
    private Set<MainCourseEvent> MainCourseEventSet = new HashSet();

    @OneToMany(mappedBy="timetable")
    @JsonIgnore
    private Set<CourseEvent> CourseEventSet = new HashSet();

    @OneToMany(mappedBy="timetable")
    @JsonIgnore
    private Set<Student> studentSet = new HashSet();
}
