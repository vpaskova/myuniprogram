package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("SELECT t FROM Teacher t WHERE t.id = ?1")
    Teacher findTeacherById(Integer id);

    @Query("SELECT t FROM Teacher t WHERE t.user.id = ?1")
    Teacher findTeacherByUserId(Integer id);
}
