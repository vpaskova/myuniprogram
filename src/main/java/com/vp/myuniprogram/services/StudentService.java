package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.StudentDTO;
import com.vp.myuniprogram.entities.*;
import com.vp.myuniprogram.enums.Role;
import com.vp.myuniprogram.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {

    final StudentRepository studentRepository;
    final TimetableRepository timetableRepository;
    final FacultyRepository facultyRepository;
    final UniversityMajorRepository universityMajorRepository;
    final UserRepository userRepository;
    final UserService userService;
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public Set<CourseEvent> getAllCourseEventsByStudent(Integer id) {
        Student student = studentRepository.findStudentById(id);
        return student.getTimetable().getCourseEventSet();
    }

    public Student getStudentByUser(Integer id) {
        return studentRepository.findStudentByUserId(id);
    }

    public Student getStudentDetails(Integer id) {
       return studentRepository.findStudentById(id);
    }
    public ResponseEntity<String> addStudent(StudentDTO studentDTO) {
        try {
            User newUser = userService.createUser(studentDTO, Role.STUDENT);
            return createStudent(studentDTO, newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Неуспешно добавяне на студент!");
        }
    }

    public ResponseEntity<String> updateStudent(Integer id, StudentDTO studentDTO) {
        try{
            Student existingStudent = studentRepository.findStudentById(id);
            if(existingStudent == null)
                return new ResponseEntity<>("Студентът не е открит!", HttpStatus.BAD_REQUEST);

            if(studentDTO.getFirstName() != null)
                existingStudent.setFirstName(studentDTO.getFirstName());

            if(studentDTO.getLastName() != null)
                existingStudent.setLastName(studentDTO.getLastName());

            if(studentDTO.getFacultyNumber() != null)
                existingStudent.setFacultyNumber(studentDTO.getFacultyNumber());

            if(studentDTO.getFaculty() != null)
                existingStudent.setFaculty(facultyRepository.findFacultyById(studentDTO.getFaculty()));

            if(studentDTO.getUniversityMajor() != null)
                existingStudent.setUniversityMajor(universityMajorRepository.findUniversityMajorById(studentDTO.getUniversityMajor()));

            if(studentDTO.getYear() != null)
                existingStudent.setYear(studentDTO.getYear());

            if(studentDTO.getSemester() != null)
                existingStudent.setSemester(studentDTO.getSemester());

            if(studentDTO.getGroup() != null)
                existingStudent.setGroup(studentDTO.getGroup());

            setStudentTimetable(existingStudent);

            studentRepository.save(existingStudent);

            return ResponseEntity.ok("Студентът е редактиран успешно!");

        }catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    private  ResponseEntity<String> createStudent(StudentDTO studentDTO, User newUser) {
        if(studentRepository.findStudentByFacultyNumber(studentDTO.getFacultyNumber()) == null) {
            Student newStudent = Student.builder()
                    .user(userRepository.findUserById(newUser.getId()))
                    .firstName(studentDTO.getFirstName())
                    .lastName(studentDTO.getLastName())
                    .facultyNumber(studentDTO.getFacultyNumber())
                    .group(studentDTO.getGroup())
                    .faculty(facultyRepository.findFacultyById(studentDTO.getFaculty()))
                    .universityMajor(universityMajorRepository.findUniversityMajorById(studentDTO.getUniversityMajor()))
                    .year(studentDTO.getYear())
                    .semester(studentDTO.getSemester())
                    .build();

            setStudentTimetable(newStudent);

            studentRepository.save(newStudent);

            return ResponseEntity.ok("Студентът е създаден успешно!");
        } else {
            userRepository.delete(newUser);
            return ResponseEntity.ok("Съществува студент с такъв факултетен номер!");
        }
    }

    private void setStudentTimetable(Student student) {
        Timetable timetable = timetableRepository.findTimeTableByFacultyAndUniMajorAndYearAndSemesterAndGroup(
                student.getFaculty().getId(), student.getUniversityMajor().getId(), student.getYear(), student.getSemester(), student.getGroup());
        student.setTimetable(timetable);
    }

    public Timetable getStudentTimetable (Integer id) {
        Student student = studentRepository.findStudentById(id);
        return  student.getTimetable();
    }
}
