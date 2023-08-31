package com.vp.myuniprogram.controllers;

import com.vp.myuniprogram.dtos.AdminDTO;
import com.vp.myuniprogram.entities.Admin;
import com.vp.myuniprogram.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/admin")
public class AdminController {

    final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminDTO adminDTO) {
        return adminService.addAdmin(adminDTO);
    }

    @GetMapping(value="/all")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping(value="/user/{id}")
    public Admin getAdminByUser(@PathVariable Integer id){
        return adminService.getAdminByUser(id);
    }

    @GetMapping(value="/{id}")
    public Admin getAdminDetails(@PathVariable Integer id){
        return adminService.getAdminDetails(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable Integer id, @RequestBody AdminDTO adminDTO) {
        return adminService.updateAdmin(id, adminDTO);
    }
}
