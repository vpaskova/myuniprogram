package com.vp.myuniprogram.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO extends UserDTO {
    private Integer universityMajor;
    private Integer faculty;
    private String year;
    private String semester;
    private String facultyNumber;
    private String group;
}
