package com.vp.myuniprogram.services;

import com.vp.myuniprogram.dtos.AdminDTO;
import com.vp.myuniprogram.entities.*;
import com.vp.myuniprogram.enums.Role;
import com.vp.myuniprogram.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    final UserRepository userRepository;
    final UserService userService;
    final AdminRepository adminRepository;
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminByUser(Integer id) {
        return adminRepository.findAdminByUserId(id);
    }

    public Admin getAdminDetails(Integer id) {
       return adminRepository.findAdminById(id);
    }

    public ResponseEntity<String> addAdmin(AdminDTO adminDTO) {
        try{
            User newUser = userService.createUser(adminDTO, Role.ADMIN);
            return createAdmin(adminDTO, newUser);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Неуспешно регистриране на администратор!");
        }
    }

    public ResponseEntity<String> updateAdmin(Integer id, AdminDTO adminDTO) {
        try{
            Admin existingAdmin = adminRepository.findAdminById(id);
            if(existingAdmin == null){
                return new ResponseEntity<>("Администраторът не е намерен", HttpStatus.BAD_REQUEST);
            }

            if(adminDTO.getFirstName() != null)
                existingAdmin.setFirstName(adminDTO.getFirstName());

            if(adminDTO.getLastName() != null)
                existingAdmin.setLastName(adminDTO.getLastName());

            return ResponseEntity.ok("Успешно редактиране на администратор!");

        } catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<String> createAdmin(AdminDTO adminDTO, User newUser) {
        Admin newAdmin = Admin.builder()
                .user(userRepository.findUserById(newUser.getId()))
                .firstName(adminDTO.getFirstName())
                .lastName(adminDTO.getLastName())
                .build();

        adminRepository.save(newAdmin);

        return new ResponseEntity<>("Успешно регистриране на администратор!", HttpStatus.OK);
    }
}
