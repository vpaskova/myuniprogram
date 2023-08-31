package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TimetableRepository extends JpaRepository<Timetable, Integer> {

    @Query("SELECT t FROM Timetable t WHERE t.id = ?1")
    Timetable findTimetableById(Integer id);

    @Query("SELECT t FROM Timetable t WHERE t.faculty.id = ?1 AND t.universityMajor.id = ?2 AND t.year = ?3 AND t.semester = ?4 AND t.group = ?5")
    Timetable findTimeTableByFacultyAndUniMajorAndYearAndSemesterAndGroup(Integer facultyId, Integer universityMajorId, String year, String semester, String group);
}
