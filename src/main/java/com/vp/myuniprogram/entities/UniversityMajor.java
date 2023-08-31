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
@Table(name = "university_major")
public class UniversityMajor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne()
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy="universityMajor")
    @JsonIgnore
    private Set<Course> courseSet = new HashSet();

    @OneToMany(mappedBy="universityMajor")
    @JsonIgnore
    private Set<Timetable> timetableSet = new HashSet();

    @OneToMany(mappedBy="universityMajor")
    @JsonIgnore
    private Set<Student> studentSet = new HashSet();

}
