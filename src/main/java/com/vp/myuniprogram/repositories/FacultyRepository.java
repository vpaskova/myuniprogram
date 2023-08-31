package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    @Query("SELECT f FROM Faculty f WHERE f.id = ?1")
    Faculty findFacultyById(Integer id);

    @Query("SELECT f FROM Faculty f WHERE f.name = ?1")
    Faculty findFacultyByName(String name);
}
