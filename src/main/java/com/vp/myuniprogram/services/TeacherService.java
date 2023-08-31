package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.TeacherDTO;
import com.vp.myuniprogram.entities.*;
import com.vp.myuniprogram.enums.Role;
import com.vp.myuniprogram.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherService {

    final StudentRepository studentRepository;
    final TimetableRepository timetableRepository;
    final FacultyRepository facultyRepository;
    final UniversityMajorRepository universityMajorRepository;
    final UserRepository userRepository;
    final UserService userService;
    final TeacherRepository teacherRepository;
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
    public Set<CourseEvent> getAllCourseEventsByTeacher(Integer id) {
        Teacher teacher = teacherRepository.findTeacherById(id);
        Set<MainCourseEvent> mainCourseEventSet = teacher.getMainCourseEventSet();
        Set<CourseEvent> teacherCourseEventSet = new HashSet<>();
        for (MainCourseEvent mainCourseEvent : mainCourseEventSet) {
            for(CourseEvent courseEvent : mainCourseEvent.getCourseEventSet())
            teacherCourseEventSet.add(courseEvent);
        }
        return teacherCourseEventSet;
    }
    public ResponseEntity<String> addTeacher(TeacherDTO teacherDTO) {
        try {
            User newUser = userService.createUser(teacherDTO, Role.TEACHER);
            return createTeacher(teacherDTO, newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Неуспешно добавяне на преподавател!");
        }
    }
    public Teacher getTeacherByUser(Integer id) {
        return teacherRepository.findTeacherByUserId(id);
    }

    public Teacher getTeacherDetails(Integer id) {
        return teacherRepository.findTeacherById(id);
    }

    private ResponseEntity<String> createTeacher(TeacherDTO teacherDTO, User newUser) {
        Teacher newTeacher = Teacher.builder()
                .user(userRepository.findUserById(newUser.getId()))
                .firstName(teacherDTO.getFirstName())
                .lastName(teacherDTO.getLastName())
                .build();;

        teacherRepository.save(newTeacher);

        return new ResponseEntity<>("Успешно създаване на преподавател!",HttpStatus.OK);
    }
}
