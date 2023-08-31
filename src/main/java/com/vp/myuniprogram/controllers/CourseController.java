package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.CourseDTO;
import com.vp.myuniprogram.entities.Course;
import com.vp.myuniprogram.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/course")
public class CourseController {

    final CourseService courseService;

    @GetMapping("/major/{id}")
    List<Course> getCourseByMajor(@PathVariable Integer id) {
        return courseService.getALlCoursesByMajor(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Integer id, @RequestBody CourseDTO courseDTO) {
        return courseService.updateCourse(id, courseDTO);
    }
}
