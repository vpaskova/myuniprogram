package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.MainCourseEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MainCourseEventRepository extends JpaRepository<MainCourseEvent, Integer> {

    @Query("SELECT m FROM MainCourseEvent m WHERE m.id = ?1")
    MainCourseEvent findMainCourseEventById(Integer id);

    @Query("SELECT c FROM MainCourseEvent c WHERE c.timetable.id = ?1")
    List<MainCourseEvent> findMainCourseEventByTimetableId(Integer id);
}
