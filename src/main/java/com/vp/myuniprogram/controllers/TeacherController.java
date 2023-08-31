package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.TeacherDTO;
import com.vp.myuniprogram.entities.CourseEvent;
import com.vp.myuniprogram.entities.Teacher;
import com.vp.myuniprogram.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/teacher")
public class TeacherController {

    final TeacherService teacherService;

    @GetMapping(value="/all")
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping(value="/{id}/timetable/events")
    public Set<CourseEvent> getUsers(@PathVariable Integer id){
        return  teacherService.getAllCourseEventsByTeacher(id);
    }

    @GetMapping(value="/user/{id}")
    public Teacher getTeacherByUser(@PathVariable Integer id){
        return teacherService.getTeacherByUser(id);
    }

    @GetMapping(value="/{id}")
    public Teacher getTeacherDetails(@PathVariable Integer id){
        return teacherService.getTeacherDetails(id);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerTeacher(@RequestBody TeacherDTO teacherDTO) {
        return teacherService.addTeacher(teacherDTO);
    }

}
