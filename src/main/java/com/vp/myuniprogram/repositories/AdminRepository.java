package com.vp.myuniprogram.repositories;

import com.vp.myuniprogram.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT a FROM Admin a WHERE a.id = ?1")
    Admin findAdminById(Integer id);

    @Query("SELECT a FROM Admin a WHERE a.user.id = ?1")
    Admin findAdminByUserId(Integer id);
}
