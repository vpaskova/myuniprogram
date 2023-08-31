package com.vp.myuniprogram.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vp.myuniprogram.entities.*;
import com.vp.myuniprogram.enums.Role;
import com.vp.myuniprogram.repositories.*;
import com.vp.myuniprogram.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final StudentRepository studentRepository;
    final AdminRepository adminRepository;
    final TeacherRepository teacherRepository;
    final FacultyRepository facultyRepository;
    final UniversityMajorRepository universityMajorRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseEntity getUserById(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null){
            return new ResponseEntity<>("Неправилно id.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity deleteUserById(Integer id) {
        User userToBeDeleted = userRepository.findUserById(id);
        if(userToBeDeleted == null){
            return new ResponseEntity<>("Неправилно id.", HttpStatus.BAD_REQUEST);
        }
        userRepository.delete(userToBeDeleted);

        return new ResponseEntity<>("Потребителят е изтрит!",HttpStatus.OK);
    }

    public ResponseEntity updateUserPassword(Integer id, ObjectNode oldPasswordAndNewPasswordInJson) {
        String oldPassword = oldPasswordAndNewPasswordInJson.get("oldPassword").asText();
        String newPassword = oldPasswordAndNewPasswordInJson.get("newPassword").asText();
        User user = userRepository.findUserById(id);
        if(encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return new ResponseEntity<>("Паролата е успешно сменена!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Грешно въведена парола!", HttpStatus.OK);
    }

    public User createUser(UserDTO userDTO, Role role) throws Exception {
        if(checkUser(userDTO).equals("") && userRepository.findUserByEmail(userDTO.getEmail()) == null) {
            User newUser = User.builder()
                    .email(userDTO.getEmail())
                    .password(encoder.encode(userDTO.getPassword()))
                    .role(role.name())
                    .build();

            userRepository.save(newUser);

            return newUser;
        } else {
            throw new Exception();
        }
    }
    private String checkUser(UserDTO userDTO){
        String message = "";
        if(userDTO.getFirstName() == null || userDTO.getFirstName().equals("")){
            message = "First Name must not be empty.";
        }else if(userDTO.getLastName() == null || userDTO.getLastName().equals("")){
            message = "Last Name must not be empty.";
        }else if(userDTO.getEmail() == null || userDTO.getEmail().equals("")){
            message = "Email must not be empty.";
        }else if(userDTO.getPassword() == null || userDTO.getPassword().equals("")){
            message = "Password must not be empty.";
        }
        return message;
    }

//    private ResponseEntity<String> executeIfYouAreAdmin (String sessionToken) {
//        User admin = userRepository.findUserBySessionToken(sessionToken);
//        if(admin == null){
//            return new ResponseEntity<>("Incorrect sessionToken", HttpStatus.BAD_REQUEST);
//        }
//        if(!admin.getRole().equals(Role.ADMIN)) {
//            return new ResponseEntity<>("Only admins can create admin accounts",HttpStatus.BAD_REQUEST);
//        }
//
//        return null;
//    }

}
