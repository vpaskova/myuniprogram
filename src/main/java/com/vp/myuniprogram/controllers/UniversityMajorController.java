package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.UniversityMajorDTO;
import com.vp.myuniprogram.entities.UniversityMajor;
import com.vp.myuniprogram.services.UniversityMajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/unimajor")
public class UniversityMajorController {

    final UniversityMajorService universityMajorService;

    @GetMapping("/all/faculty/{id}")
    public List<UniversityMajor> getAllUniversityMajorByFaculty(@PathVariable Integer id) {
        return universityMajorService.getAllUniversityMajorsByFaculty(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUniversityMajor(@RequestBody UniversityMajorDTO universityMajorDTO) {
        return universityMajorService.addUniversityMajor(universityMajorDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUniversityMajor(@PathVariable Integer id, @RequestBody UniversityMajorDTO universityMajorDTO) {
        return universityMajorService.updateUniversityMajor(id, universityMajorDTO);
    }
}
