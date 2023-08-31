package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.FacultyDTO;
import com.vp.myuniprogram.entities.Faculty;
import com.vp.myuniprogram.services.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/faculty")
public class FacultyController {

    final FacultyService facultyService;

    @GetMapping("/all")
    public List<Faculty> addFaculty() {
        return facultyService.getAllFaculties();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFaculty(@RequestBody FacultyDTO facultyDTO) {
        return facultyService.addFaculty(facultyDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFaculty(@PathVariable Integer id, @RequestBody FacultyDTO facultyDTO) {
        return facultyService.updateFaculty(id, facultyDTO);
    }
}
