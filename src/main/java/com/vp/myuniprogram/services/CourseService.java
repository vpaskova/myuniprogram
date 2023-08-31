package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.CourseDTO;
import com.vp.myuniprogram.entities.Course;
import com.vp.myuniprogram.repositories.CourseRepository;
import com.vp.myuniprogram.repositories.TeacherRepository;
import com.vp.myuniprogram.repositories.UniversityMajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    final CourseRepository courseRepository;
    final UniversityMajorRepository universityMajorRepository;
    final TeacherRepository teacherRepository;

    public List<Course> getALlCoursesByMajor(Integer id) {
        return courseRepository.findCourseByUniversityMajorId(id);
    }
    public ResponseEntity<String> addCourse(CourseDTO courseDTO) {
        try{
            Course course = Course.builder()
                    .name(courseDTO.getName())
                    .universityMajor(universityMajorRepository.findUniversityMajorById(courseDTO.getUniversityMajor()))
                    .build();

            courseRepository.save(course);

            return ResponseEntity.ok("Успешно добавяне на учебна дисциплина!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> updateCourse(Integer id, CourseDTO courseDTO) {
        try{
            Course existingCourse = courseRepository.findCourseById(id);
            if(existingCourse == null)
                return new ResponseEntity<>("Учебна дисциплина не е намерена!",HttpStatus.BAD_REQUEST);

            if(courseDTO.getName() != null)
                existingCourse.setName(courseDTO.getName());

            if(courseDTO.getUniversityMajor() != null)
                existingCourse.setUniversityMajor(universityMajorRepository.findUniversityMajorById(courseDTO.getUniversityMajor()));

            courseRepository.save(existingCourse);

            return ResponseEntity.ok("Успешно редактиране на учебна дисциплина!");
        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
