package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c WHERE c.id = ?1")
    Course findCourseById(Integer id);

    @Query("SELECT c FROM Course c WHERE c.universityMajor.id = ?1")
    List<Course> findCourseByUniversityMajorId(Integer id);
}
