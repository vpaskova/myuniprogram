package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.UniversityMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UniversityMajorRepository extends JpaRepository<UniversityMajor, Integer> {

    @Query("SELECT u FROM UniversityMajor u WHERE u.id = ?1")
    UniversityMajor findUniversityMajorById(Integer id);

    @Query("SELECT u FROM UniversityMajor u WHERE u.faculty.id = ?1")
    List<UniversityMajor> findUniversityMajorByFacultyId(Integer id);
}
