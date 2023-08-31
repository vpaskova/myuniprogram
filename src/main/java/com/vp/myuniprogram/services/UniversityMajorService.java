package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.UniversityMajorDTO;
import com.vp.myuniprogram.entities.UniversityMajor;
import com.vp.myuniprogram.repositories.FacultyRepository;
import com.vp.myuniprogram.repositories.UniversityMajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityMajorService {

    final UniversityMajorRepository universityMajorRepository;
    final FacultyRepository facultyRepository;

    public List<UniversityMajor> getAllUniversityMajorsByFaculty(Integer id) {
        return universityMajorRepository.findUniversityMajorByFacultyId(id);
    }
    public ResponseEntity<String> addUniversityMajor(UniversityMajorDTO universityMajorDTO) {
        try{
            UniversityMajor universityMajor = UniversityMajor.builder()
                    .name(universityMajorDTO.getName())
                    .faculty(facultyRepository.findFacultyByName(universityMajorDTO.getFaculty()))
                    .build();

            universityMajorRepository.save(universityMajor);

            return ResponseEntity.ok("Успешно добавяне на специалност!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> updateUniversityMajor(Integer id, UniversityMajorDTO universityMajorDTO) {
        try{
            UniversityMajor existingUniversityMajor = universityMajorRepository.findUniversityMajorById(id);
            if(existingUniversityMajor == null)
                return new ResponseEntity<>("Специалността не е намерена!",HttpStatus.BAD_REQUEST);

            if(universityMajorDTO.getName() != null)
                existingUniversityMajor.setName(universityMajorDTO.getName());

            if(universityMajorDTO.getFaculty() != null)
                existingUniversityMajor.setFaculty(facultyRepository.findFacultyByName(universityMajorDTO.getFaculty()));

            universityMajorRepository.save(existingUniversityMajor);

            return ResponseEntity.ok("Успешно редактиране на специалност!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
