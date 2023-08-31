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
@Table(name = "faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy="faculty")
    @JsonIgnore
    private Set<Timetable> timetableSet = new HashSet();

    @OneToMany(mappedBy="faculty")
    @JsonIgnore
    private Set<UniversityMajor> universityMajorSet = new HashSet();

    @OneToMany(mappedBy="faculty")
    @JsonIgnore
    private Set<Student> studentSet = new HashSet();
}
