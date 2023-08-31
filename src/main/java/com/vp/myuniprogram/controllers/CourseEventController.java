package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.CourseEventDTO;
import com.vp.myuniprogram.entities.CourseEvent;
import com.vp.myuniprogram.services.CourseEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/course/event")
public class CourseEventController {

    final CourseEventService courseEventService;

    @GetMapping(value="/all")
    public List<CourseEvent> getAllCourseEvents(){
        return  courseEventService.getAllCourseEvents();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCourseEvent(@RequestBody CourseEventDTO courseEventDTO) {
        return courseEventService.addCourseEvent(courseEventDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourseEvent(@PathVariable Integer id, @RequestBody CourseEventDTO courseEventDTO) {
        return courseEventService.updateCourseEvent(id, courseEventDTO);
    }
}
