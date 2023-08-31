package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.TimetableDTO;
import com.vp.myuniprogram.entities.Timetable;
import com.vp.myuniprogram.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableService {

    final TimetableRepository timetableRepository;
    final FacultyRepository facultyRepository;
    final UniversityMajorRepository universityMajorRepository;

    public List<Timetable> getAllTimetables() {
        return  timetableRepository.findAll();
    }

    public ResponseEntity<String> addTimetable(TimetableDTO timetableDTO) {
        try{
            Timetable timetable = Timetable.builder()
                    .faculty(facultyRepository.findFacultyById(timetableDTO.getFaculty()))
                    .universityMajor(universityMajorRepository.findUniversityMajorById(timetableDTO.getUniversityMajor()))
                    .group(timetableDTO.getGroup())
                    .semester(timetableDTO.getSemester())
                    .year(timetableDTO.getYear()).build();

            timetableRepository.save(timetable);

            return ResponseEntity.ok("Успешно добавяне на програма!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> updateTimetable(Integer id, TimetableDTO timetableDTO) {
        try{
            Timetable existingTimetable = timetableRepository.findTimetableById(id);
            if(existingTimetable == null)
                return new ResponseEntity<>("Timetable not found",HttpStatus.BAD_REQUEST);

            if(timetableDTO.getFaculty() != null)
                existingTimetable.setFaculty(facultyRepository.findFacultyById(timetableDTO.getFaculty()));

            if(timetableDTO.getGroup() != null)
                existingTimetable.setGroup(timetableDTO.getGroup());

            if(timetableDTO.getSemester() != null)
                existingTimetable.setSemester(timetableDTO.getSemester());

            if(timetableDTO.getYear() != null)
                existingTimetable.setYear(timetableDTO.getYear());

            if(timetableDTO.getUniversityMajor() != null)
                existingTimetable.setUniversityMajor(universityMajorRepository.findUniversityMajorById(timetableDTO.getUniversityMajor()));

            timetableRepository.save(existingTimetable);

            return ResponseEntity.ok("Програмата е успешно редактирана!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
