package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.FacultyDTO;
import com.vp.myuniprogram.entities.Faculty;
import com.vp.myuniprogram.repositories.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyService {

    final FacultyRepository facultyRepository;
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
    public ResponseEntity<String> addFaculty(FacultyDTO facultyDTO) {
        try{
            Faculty faculty = Faculty.builder()
                    .name(facultyDTO.getName()).build();

            facultyRepository.save(faculty);

            return ResponseEntity.ok("Факултетът е добавен успешно!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> updateFaculty(Integer id, FacultyDTO facultyDTO) {
        try{
            Faculty existingFaculty = facultyRepository.findFacultyById(id);
            if(existingFaculty == null)
                return new ResponseEntity<>("Факултетът не е намерен!",HttpStatus.BAD_REQUEST);

            if(facultyDTO.getName() != null)
                existingFaculty.setName(facultyDTO.getName());

            facultyRepository.save(existingFaculty);

            return ResponseEntity.ok("Факултетът е редактиран успешно!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
