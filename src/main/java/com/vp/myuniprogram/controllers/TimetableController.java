package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.TimetableDTO;
import com.vp.myuniprogram.entities.Timetable;
import com.vp.myuniprogram.services.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/timetable")
public class TimetableController {

    final TimetableService timetableService;

    @GetMapping(value="/all")
    public List<Timetable> getAllTimetables(){
        return  timetableService.getAllTimetables();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCourse(@RequestBody TimetableDTO timetableDTO) {
        return timetableService.addTimetable(timetableDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Integer id, @RequestBody TimetableDTO timetableDTO) {
        return timetableService.updateTimetable(id, timetableDTO);
    }
}
