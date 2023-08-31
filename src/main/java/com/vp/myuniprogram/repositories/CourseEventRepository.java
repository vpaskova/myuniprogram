package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.CourseEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CourseEventRepository extends JpaRepository<CourseEvent, Integer> {

    @Query("SELECT c FROM CourseEvent c WHERE c.id = ?1")
    CourseEvent findCourseEventById(Integer id);

    @Query("SELECT c FROM CourseEvent c WHERE c.mainCourseEvent.id = ?1")
    List<CourseEvent> findAllCourseEventsByMainCourseEventId(Integer id);
}
