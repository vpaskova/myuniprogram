package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.StudentDTO;
import com.vp.myuniprogram.entities.CourseEvent;
import com.vp.myuniprogram.entities.Student;
import com.vp.myuniprogram.entities.Timetable;
import com.vp.myuniprogram.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/student")
public class StudentController {

    final StudentService studentService;

    @GetMapping(value="/all")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping(value="/{id}/timetable")
    public Timetable getStudentTimetable(@PathVariable Integer id) {
        return studentService.getStudentTimetable(id);
    }

    @GetMapping(value="/{id}/timetable/events")
    public Set<CourseEvent> getAllCourseEventsForStudent(@PathVariable Integer id){
        return studentService.getAllCourseEventsByStudent(id);
    }

    @GetMapping(value="/user/{id}")
    public Student getStudentByUser(@PathVariable Integer id){
        return studentService.getStudentByUser(id);
    }

    @GetMapping(value="/{id}")
    public Student getStudentDetails(@PathVariable Integer id){
        return studentService.getStudentDetails(id);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestBody StudentDTO studentDTO) {
        return studentService.addStudent(studentDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Integer id, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(id, studentDTO);
    }
}
