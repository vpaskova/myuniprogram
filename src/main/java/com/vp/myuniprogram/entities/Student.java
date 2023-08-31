package com.vp.myuniprogram.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne()
    @JoinColumn(name = "university_major_id")
    private UniversityMajor universityMajor;

    @ManyToOne()
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @Column(name = "year")
    private String year;

    @Column(name = "semester")
    private String semester;

    @ManyToOne()
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @Column(name = "faculty_number")
    private String facultyNumber;

    @Column(name = "group_name")
    private String group;
}
