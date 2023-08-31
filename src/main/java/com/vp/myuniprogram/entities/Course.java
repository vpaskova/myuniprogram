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
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne()
    @JoinColumn(name = "university_major_id")
    private UniversityMajor universityMajor;

    @OneToMany(mappedBy="course")
    @JsonIgnore
    private Set<MainCourseEvent> mainCourseEventSet = new HashSet<>();
}
