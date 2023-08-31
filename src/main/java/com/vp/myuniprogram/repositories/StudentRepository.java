package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student s WHERE s.id = ?1")
    Student findStudentById(Integer id);

    @Query("SELECT s FROM Student s WHERE s.facultyNumber = ?1")
    Student findStudentByFacultyNumber(String facultyNumber);

    @Query("SELECT s FROM Student s WHERE s.user.id = ?1")
    Student findStudentByUserId(Integer id);
}
