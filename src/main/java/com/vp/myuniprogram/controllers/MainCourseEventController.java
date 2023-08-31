package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.MainCourseEventDTO;
import com.vp.myuniprogram.entities.MainCourseEvent;
import com.vp.myuniprogram.services.MainCourseEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/main/course/event")
public class MainCourseEventController {

    final MainCourseEventService mainCourseEventService;

    @GetMapping("/timetable/{id}")
    List<MainCourseEvent> getCourseByMajor(@PathVariable Integer id) {
        return mainCourseEventService.getAllMainCourseEventsByTimetable(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addMainCourseEvent(@RequestBody MainCourseEventDTO mainCourseEventDTO) {
        return mainCourseEventService.addMainCourseEvent(mainCourseEventDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMainCourseEvent(@PathVariable Integer id) {
        return mainCourseEventService.deleteMainCourseEventById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Integer id, @RequestBody MainCourseEventDTO mainCourseEventDTO) {
        return mainCourseEventService.updateMainCourseEvent(id, mainCourseEventDTO);
    }
}
