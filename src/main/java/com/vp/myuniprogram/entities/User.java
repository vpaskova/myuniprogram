package com.vp.myuniprogram.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_token")
    private String sessionToken;

    @Email
    @Column(unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "user_role")
    private String role;

    @OneToOne(mappedBy="user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Admin admin = new Admin();

    @OneToOne(mappedBy="user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Student student = new Student();

    @OneToOne(mappedBy="user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Teacher teacher = new Teacher();

}
